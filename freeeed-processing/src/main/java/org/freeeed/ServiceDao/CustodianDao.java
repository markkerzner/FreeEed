package org.freeeed.ServiceDao;

import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectCustodian;
import org.freeeed.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.NoResultException;
import java.util.TimeZone;

public class CustodianDao {
    private static volatile CustodianDao mInstance;
    private Session currentSession;


    private CustodianDao() {
        currentSession = HibernateUtil.
                getInstance().
                getSessionFactory()
                .withOptions()
                .jdbcTimeZone(TimeZone.getTimeZone("UTC"))
                .openSession();
    }
    public static CustodianDao getInstance() {
        if (mInstance == null) {
            synchronized (CustodianDao.class) {
                if (mInstance == null) {
                    mInstance = new CustodianDao();
                }
            }
        }
        return mInstance;
    }

    ProjectCustodian createCustodian(ProjectCustodian custodian) {
        Transaction transaction = currentSession.beginTransaction();
        currentSession.save(custodian);
        transaction.commit();
        return custodian;
    }

    ProjectCustodian getCustodianByName(String header, Project prj) throws NoResultException {
        return (ProjectCustodian) currentSession.createQuery("from ProjectCustodian where name=:n and project=:pr")
                .setParameter("n", header)
                .setParameter("pr", prj)
                .getSingleResult();
    }


}
