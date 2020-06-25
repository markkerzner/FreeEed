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
package org.freeeed.export;

import org.apache.commons.io.FileUtils;
import org.freeeed.Entity.MetadataHeader;
import org.freeeed.Entity.Project;
import org.freeeed.ServiceDao.MetadataService;
import org.freeeed.ServiceDao.ProjectFileService;
import org.freeeed.mr.MetadataWriter;
import org.freeeed.services.ProcessingStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.NoResultException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

public class LoadFileWriter {
    private static Project project;
    private static volatile LoadFileWriter mInstance;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadFileWriter.class);
    private File metadataFile;
    private String preFix, postFix, middleFix;
    private List<MetadataHeader> metadataHeaders = MetadataService.getInstance().getAllMetadataHeader(true);

    private LoadFileWriter() {
    }

    public static LoadFileWriter getInstance() {
        if (mInstance == null) {
            synchronized (LoadFileWriter.class) {
                if (mInstance == null) {
                    mInstance = new LoadFileWriter();
                }
            }
        }
        return mInstance;
    }

    private void setup() {
        project = Project.getActiveProject();
        if (project.getOutputType() == Project.OUTPUT_DAT) {
            preFix = "\u00FE";
            postFix = "\u00FE";
            middleFix = "¶";
        } else if (project.getOutputType() == Project.OUTPUT_TAB) {
            preFix = "";
            postFix = "";
            middleFix = "\t";
        } else if (project.getOutputType() == Project.OUTPUT_ASCII) {
            preFix = "";
            postFix = "";
            middleFix = "\u0001";
        } else if (project.getOutputType() == Project.OUTPUT_CARRET) {
            preFix = "";
            postFix = "";
            middleFix = "^";
        } else if (project.getOutputType() == Project.OUTPUT_PIPE) {
            preFix = "";
            postFix = "";
            middleFix = "|";
        }
    }

    private void prepareMetadataFile() {
        String metadataFileName =
                project.getResultsDir()
                        + System.getProperty("file.separator")
                        + Project.METADATA_FILE_NAME
                        + "."
                        + project.getOutputExtension();

        new File(project.getResultsDir()).mkdir();
        new File(metadataFileName).delete();
        metadataFile = new File(metadataFileName);
        try {
            metadataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.debug("Filename: {}", metadataFileName);
    }

    private void createMetadataFileHeader() {
        String lineToAdd = "";
        for (Iterator<MetadataHeader> i = metadataHeaders.iterator(); i.hasNext(); ) {
            MetadataHeader header = i.next();
            lineToAdd += preFix + header.getName() + postFix;
            if (i.hasNext()) {
                lineToAdd += middleFix;
            }
        }
        lineToAdd += "\n";
        appendMetadata(lineToAdd);
    }

    private void populateMetadataFile() {
        ProjectFileService.getInstance().getProjectFilesByProject(project).forEach(projectFile -> {
            String lineToAdd = "";
            for (Iterator<MetadataHeader> i = metadataHeaders.iterator(); i.hasNext(); ) {
                MetadataHeader header = i.next();
                if (header.getFieldType() == 1) {
                    lineToAdd += preFix + projectFile.getFileId() + postFix;
                } else if (header.getFieldType() == 2) {
                    lineToAdd += preFix + projectFile.getCustodian().getName() + postFix;
                } else if (header.getFieldType() == 3) {
                    lineToAdd += preFix + projectFile.getHash() + postFix;
                } else if (header.getFieldType() == 4) {
                    lineToAdd += 0 + postFix;
                } else if (header.getFieldType() == 5) {
                    if (projectFile.getParent() != null) {
                        lineToAdd += preFix + projectFile.getParent().getFileId() + postFix;
                    } else {
                        lineToAdd += preFix + projectFile.getFileId() + postFix;
                    }
                } else if (header.getFieldType() == 6) {
                    lineToAdd += preFix + projectFile.getFile().getName() + postFix;
                } else if (header.getFieldType() == 7) {
                    if (projectFile.getMaster() != null) {
                        lineToAdd += preFix + projectFile.getMaster().getFileId() + postFix;
                    } else {
                        lineToAdd += preFix + "" + postFix;
                    }
                } else if (header.getFieldType() == 0) {
                    try {
                        lineToAdd += preFix + MetadataService.getInstance().getMetaValueByFileAndHeader(projectFile, header).getName() + postFix;
                    } catch (NoResultException e) {
                        lineToAdd += preFix + "" + postFix;
                    }
                }
                if (i.hasNext()) {
                    lineToAdd += middleFix;
                }
            }
            lineToAdd += "\n";
            appendMetadata(lineToAdd);
        });
        NativeCreator.getInstance().packNative();
    }

    private void appendMetadata(String string) {
        try {
            FileUtils.writeStringToFile(metadataFile, string, Charset.defaultCharset(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLoadFile() {
        ProcessingStats.getInstance().taskIsLoadCreator();
        setup();
        prepareMetadataFile();
        createMetadataFileHeader();
        populateMetadataFile();
    }
}
