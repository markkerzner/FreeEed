package org.freeeed.ServiceDao;

import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectPath;

import java.util.List;

public class ProjectPathService {


    private static volatile ProjectPathService mInstance;

    private ProjectPathService() {
    }

    public static ProjectPathService getInstance() {
        if (mInstance == null) {
            synchronized (ProjectPathService.class) {
                if (mInstance == null) {
                    mInstance = new ProjectPathService();
                }
            }
        }
        return mInstance;
    }

    public List<ProjectPath> getPathList(Project prj) {
        return ProjectPathDao.getInstance().getPathList(prj);
    }


}
