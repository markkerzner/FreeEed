package org.freeeed.ServiceDao;

import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ProjectFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectFileService.class);
    private static volatile ProjectFileService mInstance;

    public static ProjectFileService getInstance() {
        if (mInstance == null) {
            synchronized (ProjectFileService.class) {
                if (mInstance == null) {
                    mInstance = new ProjectFileService();
                }
            }
        }
        return mInstance;
    }

    public void createProjectFile(ProjectFile projectFile) {
        try {
            String hash = Util.createKeyHash(projectFile.getFile(), null);
            projectFile.setHash(hash);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        ProjectFileDao.getInstance().createProjectFile(projectFile);
    }

    public List<ProjectFile> getProjectFilesByeProject(Project project) {
        return ProjectFileDao.getInstance().getProjectFilesByeProject(project);
    }

}
