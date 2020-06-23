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
    public static int DATA_SOURCE_EDISCOVERY = 0;
    @Transient
    public static int DATA_SOURCE_LOAD_FILE = 1;
    @Transient
    public static int DATA_SOURCE_BLOCKCHAIN = 2;
    @Transient
    public static int DATA_SOURCE_QB = 3;

    @Transient
    private static final String STAGING = "staging";
    @Transient
    private static final String OUTPUT = "output";
    @Transient
    private static final String RESULTS = "results";
    @Transient
    public static String METADATA_FILE_NAME = "metadata";

    private static Project activeProject=null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    private String name;

    private Timestamp createTime;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectPath> projectPaths = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectCustodian> projectCustodians = new ArrayList<>();

    public List<ProjectCustodian> getProjectCustodians() {
        return projectCustodians;
    }

    public void addProjectFile(ProjectPath projectPath) {
        projectPaths.add(projectPath);
    }

    private int dataSource;

    private int outputType;

    private int loadFileType;

    private boolean doOcr;

    private boolean doSearch;

    private boolean doDenist;

    public void setLoadFileType(int loadFileType) {
        this.loadFileType = loadFileType;
    }

    public int getLoadFileType() {
        return loadFileType;
    }

    public boolean isDoDenist() {
        return doDenist;
    }

    public void setDoDenist(boolean doDenist) {
        this.doDenist = doDenist;
    }

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dateSource) {
        this.dataSource = dateSource;
    }

    public int getOutputType() {
        return outputType;
    }

    public void setOutputType(int outputType) {
        this.outputType = outputType;
    }

    public boolean isDoOcr() {
        return doOcr;
    }

    public void setDoOcr(boolean doOcr) {
        this.doOcr = doOcr;
    }

    public boolean isDoSearch() {
        return doSearch;
    }

    public void setDoSearch(boolean doSearch) {
        this.doSearch = doSearch;
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
