package org.freeeed.Entity;

import org.freeeed.main.ParameterProcessing;
import org.freeeed.services.Settings;
import javax.persistence.*;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Transient
    private static final String STAGING = "staging";
    @Transient
    private static final String OUTPUT = "output";
    @Transient
    private static final String RESULTS = "results";
    @Transient
    public static String METADATA_FILE_NAME = "metadata";

    private static Project activeProject;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    private String name;

    private Timestamp createTime;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectPath> projectPaths = new ArrayList<>();


    public void addProjectFile(ProjectPath projectPath) {
        projectPaths.add(projectPath);
    }

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProjectPath> getProjectPaths() {
        return projectPaths;
    }

    public void setProjectPaths(List<ProjectPath> projectPaths) {
        this.projectPaths = projectPaths;
    }

    public String getCreateTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(createTime);
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public static Project getActiveProject() {
        return activeProject;
    }

    public static void setActiveProject(Project activeProject) {
        Project.activeProject = activeProject;
    }

    public String getOut() {
        return Settings.getSettings().getOutputDir()
                + ParameterProcessing.OUTPUT_DIR;
    }

    public String getStagingDir() {
        return getOut() + File.separator
                + getProjectId() + File.separator
                + OUTPUT + File.separator
                + STAGING;
    }

    private String getOutputDir() {
        return getOut() + File.separator
                + getProjectId() + File.separator
                + OUTPUT;
    }

    public String getResultsDir() {
        return getOutputDir() + File.separator + RESULTS;
    }

}
