/*
 *
 * Copyright SHMsoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.freeeed.mr;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.Processor.EmlFileProcessor;
import org.freeeed.Processor.FileProcessor;
import org.freeeed.Processor.SystemFileProcessor;
import org.freeeed.ServiceDao.ProjectFileService;
import org.freeeed.extractor.PstExtractor;
import org.freeeed.extractor.ZipFileExtractor;
import org.freeeed.main.*;
import org.freeeed.services.ProcessingStats;
import org.freeeed.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class FreeEedMR {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreeEedMR.class);
    private static Project project;
    private static File stagingFolder;
    private static volatile FreeEedMR mInstance;
    private int zipFileToExtract = 0, pstFileToExtract = 0;

    private FreeEedMR() {
    }

    public static FreeEedMR getInstance() {
        if (mInstance == null) {
            synchronized (FreeEedMR.class) {
                if (mInstance == null) {
                    mInstance = new FreeEedMR();
                }
            }
        }
        return mInstance;
    }

    public void run() {
        project = Project.getActiveProject();
        stagingFolder = new File(project.getStagingDir());
        ProcessingStats.getInstance().setJobStarted(project.getName());
        populateDatabase();
    }

    private void populateDatabase() {
        LOGGER.info("Starting Main Process");
        List<File> files = (List<File>) FileUtils.listFiles(stagingFolder, new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
        files.forEach(temp -> {
            ProjectFile file = new ProjectFile(temp.getAbsolutePath(), project);
            ProjectFileService.getInstance().createProjectFile(file);
        });
        decideNextJob();
    }

    public void reduceZipFile() {
        ProcessingStats.getInstance().addzipFilExtracted();
        zipFileToExtract--;
        if (zipFileToExtract == 0) {
            decideNextJob();
        }
    }

    private void processStageZipFiles(List<ProjectFile> fileZip) {
        zipFileToExtract = fileZip.size();
        ProcessingStats.getInstance().taskIsZip();
        fileZip.forEach(temp -> ExecutorPool.getInstance().getExecutorService().execute(new ZipFileExtractor(temp)));
    }

    public void reducePSTFile() {
        ProcessingStats.getInstance().addpstFilExtracted();
        pstFileToExtract--;
        if (pstFileToExtract == 0) {
            decideNextJob();
        }
    }

    private void processStagePSTFile(List<ProjectFile> filesPst) {
        ProcessingStats.getInstance().taskIsPST();
        pstFileToExtract = filesPst.size();
        filesPst.forEach(temp -> ExecutorPool.getInstance().getExecutorService().execute(new PstExtractor(temp)));
    }

    /**
     * Decide what to do first
     */
    private void decideNextJob() {
        List<ProjectFile> filesPst = ProjectFileService.getInstance().getProjectFilesByProject(project, "pst");
        List<ProjectFile> filesZip = ProjectFileService.getInstance().getProjectFilesByProject(project, "zip");
        if (filesZip.size() > 0) {
            processStageZipFiles(filesZip);
        } else if (filesPst.size() > 0) {
            processStagePSTFile(filesPst);
        } else {
            mainProcess();
        }
    }

    private void mainProcess() {
        ProcessingStats.getInstance().taskIsTika();

        ProcessingStats.getInstance().setTotalItem(ProjectFileService.getInstance().getProjectFileCount(project));
        ProcessingStats.getInstance().setTotalSize(ProjectFileService.getInstance().getProjectSize(project));

        List<ProjectFile> projectFiles = ProjectFileService.getInstance().getProjectFilesByProject(project);
        projectFiles.forEach(temp -> {
            Runnable fileProcessor = null;

            if ("eml".equalsIgnoreCase(temp.getExtension())) {
                fileProcessor = new EmlFileProcessor(temp);
            } else if (Util.isSystemFile(temp.getFile())) {
                fileProcessor = new SystemFileProcessor(temp);
            } else {
                fileProcessor = new FileProcessor(temp);
            }
            ExecutorPool.getInstance().getExecutorService().execute(fileProcessor);
        });
    }

}
