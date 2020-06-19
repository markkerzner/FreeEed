package org.freeeed.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProjectPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pathId;

    private String path;

    @ManyToOne
    @JoinColumn(name = "custodianId")
    private ProjectCustodian custodian;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    public ProjectCustodian getCustodian() {
        return custodian;
    }

    public ProjectPath() {
    }

    public ProjectPath(String path, ProjectCustodian custodian, Project project) {
        this.path = path;
        this.custodian = custodian;
        this.project = project;
    }

    public int getPathId() {
        return pathId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String file) {
        this.path = file;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
