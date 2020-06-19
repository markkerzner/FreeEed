package org.freeeed.ServiceDao;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.freeeed.Entity.MetadataHeader;
import org.freeeed.Entity.ProjectMetadata;
import org.freeeed.Entity.ProjectFile;

import java.util.HashMap;


public class MetadataService {

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

    public void putMetaHeaderInCache(String metaHeader) {
        if (MetadataService.headerWorkingOn.get(metaHeader) == null) {
            headerWorkingOn.put(metaHeader, true);
            if (headerHashMap.get(metaHeader) == null) {
                if (MetadataDao.getInstance().getMetadataHeaderCountByName(metaHeader) > 0) {
                    headerHashMap.put(metaHeader,MetadataDao.getInstance().getMetadataHeaderByName(metaHeader));
                } else {
                    MetadataHeader m =  MetadataDao.getInstance().createMetadataHeader(new MetadataHeader(metaHeader));
                    headerHashMap.put(metaHeader,m);
                }
            }
        }
    }


    public void newMetaData(String metaHeader, String metaValue, ProjectFile file) {
         ProjectMetadata m = new ProjectMetadata(metaValue, headerHashMap.get(metaHeader), file);
         MetadataDao.getInstance().createMetaData(m);
    }
}
