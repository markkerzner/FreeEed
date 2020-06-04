package org.freeeed.ServiceDao;

import org.freeeed.Entity.MetadataHeader;
import org.freeeed.Entity.ProjectMetadata;
import org.freeeed.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
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
        transaction.commit();
        return header;
    }

    MetadataHeader getMetadataHeaderByName(String header) throws NoResultException {
        return (MetadataHeader) currentSession.createQuery("from MetadataHeader where name=:n").setParameter("n",header).getSingleResult();
    }

    int createMetaData(ProjectMetadata meta){

        Transaction transaction = currentSession.beginTransaction();
        currentSession.save(meta);
        transaction.commit();
        return 1;


    }




}
