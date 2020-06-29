package org.freeeed.Processor;

import org.freeeed.Entity.ProjectMetadata;
import org.freeeed.ServiceDao.MetadataService;
import org.freeeed.export.LoadFileWriter;
import org.freeeed.mr.FreeEedMR;
import org.freeeed.services.ProcessingStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;
import java.util.Queue;

public class CacheWriter extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheWriter.class);
    public static volatile Queue<ProjectMetadata> projectMetadataList = new LinkedList<>();
    public static volatile int addedMeta = 0;
    private static volatile int doneMeta = 0;
    @Override
    public void run() {
        while (true) {
            if (projectMetadataList.size() > 0) {
                ProjectMetadata m = projectMetadataList.poll();
                MetadataService.getInstance().newMetaData(m);
                ProcessingStats.getInstance().setSecondBarValue(++doneMeta);
            }
            if (!FreeEedMR.isProcessing && projectMetadataList.size() == 0) {
                LOGGER.info("Cache Done");
                LoadFileWriter.getInstance().createLoadFile();
                break;
            }
        }
    }
}
