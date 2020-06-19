package org.freeeed.ServiceDao;

import org.freeeed.Entity.MetadataHeader;
import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectPath;
import org.freeeed.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

    List<ProjectPath> getPathList(Project prj) {
        return currentSession.createQuery("from ProjectPath where project=:pr").setParameter("pr", prj).list();
    }

    int getProjectPatchCountByPathAndProject(String path, Project project) {
        return currentSession.createQuery("from ProjectPath where path=:path and project=:prj")
                .setParameter("path", path)
                .setParameter("prj", project)
                .list().size();
    }

    void deleteAllPath(Project prj) {
        Transaction t = currentSession.beginTransaction();
        currentSession.createQuery("delete from ProjectPath where project=:prj").setParameter("prj", prj);
        t.commit();
    }

    void deletePath(ProjectPath path) {
        Transaction t = currentSession.beginTransaction();
        currentSession.delete(path);
        t.commit();
    }

    ProjectPath getProjectPathbyPath(String path) {
        return (ProjectPath) currentSession.createQuery("from ProjectPath where path=:n").setParameter("n", path).getSingleResult();
    }

}
