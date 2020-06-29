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

    public ProjectFile createProjectFile(ProjectFile projectFile) {
        try {
            String hash = Util.createKeyHash(projectFile.getFile(), null);
            projectFile.setHash(hash);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        projectFile.setFileSize(projectFile.getFile().length());
        projectFile.setExtension(Util.getExtension(projectFile.getFile().getName()));
        if (!ProjectFileService.getInstance().isFileExists(projectFile)) {
            int id = ProjectFileDao.getInstance().createProjectFile(projectFile);
            return ProjectFileDao.getInstance().getProjectFilebyId(id);
        }
        return null;
    }

    public List<ProjectFile> getProjectFilesByProject(Project project) {
        return ProjectFileDao.getInstance().getProjectFilesByProject(project);
    }

    public List<ProjectFile> getProjectFilesByProject(Project project, String extension) {
        return ProjectFileDao.getInstance().getProjectFilesByProject(project, extension);
    }


    private boolean isFileExists(ProjectFile projectFile) {
        return ProjectFileDao.getInstance().isFileExists(projectFile);
    }

    public long getProjectSize(Project project) {
        return ProjectFileDao.getInstance().getProjectSize(project);
    }

    public int getProjectFileCount(Project project) {
        return ProjectFileDao.getInstance().getProjectFileCount(project);
    }

    public void updateProjectFile(ProjectFile projectFile){
        ProjectFileDao.getInstance().updateProjectFile(projectFile);
    }

}
