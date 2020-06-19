package org.freeeed.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MetadataHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int metadataId;

    @Column(unique=true)
    private String name;

    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMetadata> projectPaths = new ArrayList<>();


    public MetadataHeader() {
    }

    public List<ProjectMetadata> getProjectPaths() {
        return projectPaths;
    }

    public MetadataHeader(String name) {
        this.name = name;
    }

    public int getMetadataId() {
        return metadataId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
