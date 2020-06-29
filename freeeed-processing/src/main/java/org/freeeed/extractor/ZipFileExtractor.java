package org.freeeed.extractor;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.ServiceDao.ProjectFileService;
import org.freeeed.mr.FreeEedMR;
import org.freeeed.services.ProcessingStats;
import org.freeeed.services.UniqueIdGenerator;
import java.io.File;
import java.util.List;

public class ZipFileExtractor implements Runnable {

    private ProjectFile projectFile;
    private String tmpFolder;

    public ZipFileExtractor(ProjectFile projectFile) {
        this.projectFile = projectFile;
        String fileId = UniqueIdGenerator.INSTANCE.getNextDocumentId();
        tmpFolder = projectFile.getFile().getParent() + System.getProperty("file.separator") + fileId + "_" + projectFile.getFile().getName() + System.getProperty("file.separator");
        new File(tmpFolder).mkdirs();
    }

    @Override
    public void run() {
        ZipFile zipFile = new ZipFile(projectFile.getFile());
        try {
            List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
            for (Object o : fileHeaderList) {
                FileHeader fileHeader = (FileHeader) o;
                zipFile.extractFile(fileHeader, tmpFolder);
                ProcessingStats.getInstance().addzipFilExtractedSize(fileHeader.getUncompressedSize());
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }

        List<File> files = (List<File>) FileUtils.listFiles(new File(tmpFolder), new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
        files.forEach(temp -> {
            ProjectFile file = new ProjectFile(temp.getAbsolutePath(), Project.getActiveProject());
            file.setParent(projectFile);
            ProjectFileService.getInstance().createProjectFile(file);
        });

        projectFile.setAsProcessed();
        ProjectFileService.getInstance().updateProjectFile(projectFile);
        FreeEedMR.getInstance().reduceZipFile();
    }
}
