package org.freeeed.ServiceDao;

import org.freeeed.Entity.Project;

import java.util.List;


public class ProjectService {

    private static volatile ProjectService mInstance;

    private ProjectService() {
    }

    public static ProjectService getInstance() {
        if (mInstance == null) {
            synchronized (ProjectService.class) {
                if (mInstance == null) {
                    mInstance = new ProjectService();
                }
            }
        }
        return mInstance;
    }

    public Project createProject(Project project) {
        return ProjectDao.getInstance().createProject(project);
    }

    public List<Project> getProjects() {
        return ProjectDao.getInstance().getProjects();
    }

    public Project getProject(int projectId) {
        return ProjectDao.getInstance().getProject(projectId);
    }

    public void deleteProject(int projectId) {
        ProjectDao.getInstance().deleteProject(getProject(projectId));
    }

    public void updatePorject(Project prj) {
        ProjectDao.getInstance().updateProject(prj);
    }

}
