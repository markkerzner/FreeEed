package org.freeeed.ServiceDao;

import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectPath;
import org.freeeed.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.TimeZone;

public class ProjectPathDao {

    private static volatile ProjectPathDao mInstance;
    private Session currentSession;



    private ProjectPathDao() {
        currentSession = HibernateUtil.
                getInstance().
                getSessionFactory()
                .withOptions()
                .jdbcTimeZone(TimeZone.getTimeZone("UTC"))
                .openSession();
    }

    public static ProjectPathDao getInstance() {
        if (mInstance == null) {
            synchronized (ProjectPathDao.class) {
                if (mInstance == null) {
                    mInstance = new ProjectPathDao();
                }
            }
        }
        return mInstance;
    }

    public List<ProjectPath> getPathList(Project prj) {
        return currentSession.createQuery("from ProjectPath where project=:pr").setParameter("pr",prj).list();
    }





}
