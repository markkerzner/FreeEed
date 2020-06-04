package org.freeeed.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProjectCustodian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int custodianId;

    private String name;

    @OneToMany(mappedBy = "custodian", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectPath> projectPaths = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;


    public ProjectCustodian(String name, Project project) {
        this.name = name;
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public List<ProjectPath> getProjectPaths() {
        return projectPaths;
    }

    public Project getProject() {
        return project;
    }

    public int getCustodianId() {
        return custodianId;
    }
}
