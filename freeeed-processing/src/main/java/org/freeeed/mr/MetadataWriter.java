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

import org.apache.tika.metadata.Metadata;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.Entity.ProjectMetadata;
import org.freeeed.Processor.CacheWriter;
import org.freeeed.ServiceDao.MetadataService;
import org.freeeed.services.ProcessingStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


public class MetadataWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataWriter.class);
    private static volatile MetadataWriter mInstance;

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

    public synchronized void processMap(ProjectFile projectFile, Metadata metadata) {
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
            CacheWriter.projectMetadataList.add( new ProjectMetadata(metadata.get(name), MetadataService.headerHashMap.get(name), projectFile) );
            ProcessingStats.getInstance().taskIsTika(projectFile.getFile().getName(), name);
            ProcessingStats.getInstance().setSecondBarMax(++CacheWriter.addedMeta);
        }
        LOGGER.info(projectFile.getFile() + " Done");
        ProcessingStats.getInstance().increaseItemCount(projectFile.getFile().length());
    }
}
