package org.freeeed.ServiceDao;

import org.freeeed.Entity.MetadataHeader;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.Entity.ProjectMetadata;
import org.freeeed.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.TimeZone;

public class MetadataDao {

    private static volatile MetadataDao mInstance;
    private Session currentSession;

    private MetadataDao() {
        currentSession = HibernateUtil.
                getInstance().
                getSessionFactory()
                .withOptions()
                .jdbcTimeZone(TimeZone.getTimeZone("UTC"))
                .openSession();
    }

    public static MetadataDao getInstance() {
        if (mInstance == null) {
            synchronized (MetadataDao.class) {
                if (mInstance == null) {
                    mInstance = new MetadataDao();
                }
            }
        }
        return mInstance;
    }

    MetadataHeader createMetadataHeader(MetadataHeader header) {
        Transaction transaction = currentSession.beginTransaction();
        currentSession.save(header);
        int metaId = header.getMetadataId();
        transaction.commit();
        return getMetaHeader(metaId);
    }

    MetadataHeader getMetaHeader(int metaId) {
        return currentSession.load(MetadataHeader.class, metaId);
    }

    MetadataHeader getMetadataHeaderByName(String header) throws NoResultException {
        return (MetadataHeader) currentSession.createQuery("from MetadataHeader where name=:n").setParameter("n", header).getSingleResult();
    }

    List<MetadataHeader> metadataHeaderList() {
        return (List<MetadataHeader>) currentSession.createQuery("from MetadataHeader").list();
    }

    int getMetadataHeaderCountByName(String header) {
        return currentSession.createQuery("from MetadataHeader where name=:n").setParameter("n", header).list().size();
    }

    int createMetaData(ProjectMetadata meta) {
        Session s = HibernateUtil.
                getInstance().
                getSessionFactory()
                .withOptions()
                .jdbcTimeZone(TimeZone.getTimeZone("UTC"))
                .openSession();
        Transaction transaction = s.beginTransaction();
        s.save(meta);
        transaction.commit();
        return 1;
    }

    List<MetadataHeader> getAllMetadataHeader(boolean sorted) {
        if (sorted) {
            return currentSession.createQuery("from MetadataHeader order by orderBy").list();
        }
        return currentSession.createQuery("from MetadataHeader").list();
    }

    ProjectMetadata getMetaValueByFileAndHeader(ProjectFile file, MetadataHeader header) {
        return (ProjectMetadata) currentSession.createQuery("from ProjectMetadata where header=:h and file =:f ")
                .setParameter("h", header)
                .setParameter("f", file)
                .getSingleResult();
    }


}
