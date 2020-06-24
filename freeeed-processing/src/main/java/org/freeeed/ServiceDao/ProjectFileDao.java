package org.freeeed.ServiceDao;

import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectCustodian;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.TimeZone;

class ProjectFileDao {
    private static volatile ProjectFileDao mInstance;
    private Session currentSession;


    private ProjectFileDao() {
        currentSession = HibernateUtil.
                getInstance().
                getSessionFactory()
                .withOptions()
                .jdbcTimeZone(TimeZone.getTimeZone("UTC"))
                .openSession();
    }

    public static ProjectFileDao getInstance() {
        if (mInstance == null) {
            synchronized (ProjectFileDao.class) {
                if (mInstance == null) {
                    mInstance = new ProjectFileDao();
                }
            }
        }
        return mInstance;
    }

    void createProjectFile(ProjectFile projectFile) {
        Transaction transaction = currentSession.beginTransaction();
        currentSession.save(projectFile);
        transaction.commit();
    }

    List<ProjectFile> getProjectFilesByProject(Project project) {
        return currentSession.createQuery("from ProjectFile where custodian in (from ProjectCustodian where project=:prj)").setParameter("prj", project).list();
    }


}
