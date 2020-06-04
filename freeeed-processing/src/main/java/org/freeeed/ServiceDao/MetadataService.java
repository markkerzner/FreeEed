package org.freeeed.ServiceDao;

import org.freeeed.Entity.MetadataHeader;
import org.freeeed.Entity.ProjectMetadata;
import org.freeeed.Entity.ProjectFile;


public class MetadataService {

    private static volatile MetadataService mInstance;


    private MetadataService() {
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


    public void newMetaData(String metaHeader, String metaValue, ProjectFile file) {
        MetadataHeader header = null;
        try {
            header = MetadataDao.getInstance().getMetadataHeaderByName(metaHeader);
        } catch (Exception e) {
            header = MetadataDao.getInstance().createMetadataHeader(new MetadataHeader(metaHeader));
        }
        ProjectMetadata m = new ProjectMetadata(metaValue, header,file);
        MetadataDao.getInstance().createMetaData(m);
    }
}
