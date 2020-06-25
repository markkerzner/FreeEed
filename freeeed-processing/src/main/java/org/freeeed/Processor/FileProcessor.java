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
package org.freeeed.Processor;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.mr.MetadataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Opens the file, creates Lucene index and searches, then updates Hadoop map
 */
public class FileProcessor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);
    private Project project = Project.getActiveProject();
    private String singleFileName;
    ProjectFile projectFile;
    protected Metadata metadata = new Metadata();
    private final String[] imageExt = new String[]{"jpg", "jpeg", "png", "bmp", "tiff"};
    List<String> imageExtList = Arrays.asList(imageExt);
    protected Tika tika;
    String exceptionMessage = null;
    private String text = null;
    private static final String EMPTY = "";
    private static final Tika TIKA = new Tika();

    FileProcessor() {
    }

    public String getSingleFileName() {
        return singleFileName;
    }

    public FileProcessor(ProjectFile projectFile) {
        this.projectFile = projectFile;
        tika = new Tika();
        tika.setMaxStringLength(10 * 1024 * 1024);
    }

    private void writeMetadata() {
        MetadataWriter.getInstance().processMap(projectFile, metadata, text);
    }

    private String ocrFile(String file) {
        String simpleParse = null;
        if (project.isDoOcr()) {

            try {
                simpleParse = tika.parseToString(new File(file));
            } catch (IOException | TikaException e) {
                LOGGER.error("Error during Tika ORC {}", e.getMessage());
            }

            if (simpleParse != null && simpleParse.length() == 0) {
                LOGGER.info("processing with tesseract {}", file);
                ITesseract instance = new Tesseract();
                instance.setDatapath("tessdata");
                try {
                    File File = new File(file);
                    simpleParse = instance.doOCR(File);
                } catch (TesseractException ex) {
                    LOGGER.error("Error during tesseract ORC {}", ex.getMessage());
                }
            }
        }
        return simpleParse;
    }


    @Override
    public void run() {
        LOGGER.trace("Processing file: {} - {}", projectFile.getFile().getName(), project.getName());
        try {
            TikaInputStream inputStream = TikaInputStream.get(projectFile.getFile().toPath());
            if ("pdf".equalsIgnoreCase(projectFile.getExtension()) || imageExtList.contains(projectFile.getExtension())) {
                tika.parseToString(inputStream, metadata);
                text = ocrFile(projectFile.getFile().getPath());
            } else {
                if (inputStream.available() > 0) {
                    text = tika.parseToString(inputStream, metadata);
                }
            }
            inputStream.close();
        } catch (Exception e) {
            //metadata.set(DocumentMetadataKeys.PROCESSING_EXCEPTION, e.getMessage());
            //LOGGER.error("Problem parsing file", e);
            e.printStackTrace();
        }
        writeMetadata();
    }
}