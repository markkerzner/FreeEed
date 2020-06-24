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
import org.apache.tika.metadata.Metadata;
import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.ServiceDao.MetadataService;
import org.freeeed.main.*;
import org.freeeed.metadata.ColumnMetadata;
import org.freeeed.main.DiscoveryFile;
import org.freeeed.services.ProcessingStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class MetadataWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataWriter.class);
    private static volatile MetadataWriter mInstance;
    private File metadataFile;
    private String tmpFolder;

    private MetadataWriter() {
    }

    public static MetadataWriter getInstance() {
        if (mInstance == null) {
            synchronized (MetadataWriter.class) {
                if (mInstance == null) {
                    mInstance = new MetadataWriter();
                }
            }
        }
        return mInstance;
    }

    public synchronized void processMap(ProjectFile projectFile, Metadata metadata, String text) {
        String[] metadataNames = metadata.names();
        for (String name : metadataNames) {
            while (MetadataService.headerHashMap.get(name) == null) {
                try {
                    Random rnRandom = new Random();
                    long rnd = rnRandom.nextInt(2) * 1000;
                    rnd += 1000;
                    Thread.sleep(rnd);
                    MetadataService.getInstance().putMetaHeaderInCache(name);
                } catch (Exception ignored) {
                }
            }
            MetadataService.getInstance().newMetaData(name, metadata.get(name), projectFile);
        }
        //TODO: Write text to file immediately
        LOGGER.info(projectFile.getFile() + " Done");
        ProcessingStats.getInstance().increaseItemCount(projectFile.getFile().length());
    }

    public void packNative() {
/*
        ProcessingStats.getInstance().taskIsNative();
        int indexNativeLink = 0, indexStageFile = 0, indexExceptionLink = 0;
        boolean checkingHeader = true;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(metadataFile));
            String line = reader.readLine();
            while (line != null) {
                ArrayList<String> headers = new ArrayList<>();
                Collections.addAll(headers, line.split("\\|"));
                if (checkingHeader) {
                    if (headers.contains("native_link")) {
                        indexNativeLink = headers.indexOf("native_link");
                    }
                    if (headers.contains("exception_link")) {
                        indexExceptionLink = headers.indexOf("exception_link");
                    }
                    if (headers.contains("Source Path")) {
                        indexStageFile = headers.indexOf("Source Path");
                    }
                }
                if (!checkingHeader) {
                    copyNativeFile(indexStageFile, indexNativeLink, headers);
                    copyNativeFile(indexStageFile, indexExceptionLink, headers);
                }
                checkingHeader = false;
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProcessingStats.getInstance().taskIsCompressing();
        ResultCompressor.getInstance().process();
*/
    }

    private void copyNativeFile(int indexStageFile, int indexExceptionLink, ArrayList<String> headers) throws IOException {
/*
        String newFile;
        File f;
        File stage;
        if (!(newFile = headers.get(indexExceptionLink)).equals("")) {
            f = new File(tmpFolder + System.getProperty("file.separator") + newFile);
            stage = new File(headers.get(indexStageFile));
            f.getParentFile().mkdirs();
            FileUtils.copyFile(stage, f);
            ProcessingStats.getInstance().addNativeCopied(stage.length());
        }
*/
    }
}
