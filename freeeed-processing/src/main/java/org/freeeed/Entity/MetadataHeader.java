package org.freeeed.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MetadataHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int metadataId;

    private String name;

    @Column(columnDefinition = "integer default 20")
    private int orderBy;

    @Column(columnDefinition = "integer default 0")
    private int FieldType;

    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMetadata> projectPaths = new ArrayList<>();

    public MetadataHeader() {
    }

    public List<ProjectMetadata> getProjectPaths() {
        return projectPaths;
    }

    public MetadataHeader(String name) {
        this.name = name;
        orderBy = 10;
        FieldType = 0;
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

    public int getOrderBy() {
        return orderBy;
    }

    public int getFieldType() {
        return FieldType;
    }
}
