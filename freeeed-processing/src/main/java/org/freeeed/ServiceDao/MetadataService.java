package org.freeeed.ServiceDao;

import org.freeeed.Entity.MetadataHeader;
import org.freeeed.Entity.ProjectMetadata;
import org.freeeed.Entity.ProjectFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;


public class MetadataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataService.class);

    private static volatile MetadataService mInstance;

    public static volatile HashMap<String, MetadataHeader> headerHashMap = new HashMap<>();

    public static volatile HashMap<String, Boolean> headerWorkingOn = new HashMap<>();


    private MetadataService() {
        System.out.println("Hello");
        MetadataDao.getInstance().metadataHeaderList().forEach(header -> headerHashMap.put(header.getName(), header));
    }

    public static MetadataService getInstance() {
        if (mInstance == null) {
            synchronized (MetadataService.class) {
                if (mInstance == null) {
                    mInstance = new MetadataService();
                }
            }
        }


        return mInstance;
    }

    public List<MetadataHeader> getAllMetadataHeader(boolean sorted) {
        return MetadataDao.getInstance().getAllMetadataHeader(sorted);
    }


    public void putMetaHeaderInCache(String metaHeader) {
        if (MetadataService.headerWorkingOn.get(metaHeader) == null) {
            headerWorkingOn.put(metaHeader, true);
            if (headerHashMap.get(metaHeader) == null) {
                if (MetadataDao.getInstance().getMetadataHeaderCountByName(metaHeader) > 0) {
                    headerHashMap.put(metaHeader, MetadataDao.getInstance().getMetadataHeaderByName(metaHeader));
                } else {
                    LOGGER.info("Creating Metadata Header: {}", metaHeader);
                    MetadataHeader m = MetadataDao.getInstance().createMetadataHeader(new MetadataHeader(metaHeader));
                    headerHashMap.put(metaHeader, m);
                }
            }
        }
    }


    public void newMetaData(String metaHeader, String metaValue, ProjectFile file) {
        ProjectMetadata m = new ProjectMetadata(metaValue, headerHashMap.get(metaHeader), file);
        MetadataDao.getInstance().createMetaData(m);
    }

    public ProjectMetadata getMetaValueByFileAndHeader(ProjectFile file, MetadataHeader header) {
        return MetadataDao.getInstance().getMetaValueByFileAndHeader(file, header);
    }


}
