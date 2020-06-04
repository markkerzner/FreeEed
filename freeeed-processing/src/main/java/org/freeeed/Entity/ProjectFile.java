package org.freeeed.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;

    private String file;

    private boolean isProcessed;

    private String hash;

    @ManyToOne
    @JoinColumn(name = "pathId")
    private ProjectPath path;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMetadata> metadataList = new ArrayList<>();

    public ProjectPath getPath() {
        return path;
    }

    public List<ProjectMetadata> getMetadataList() {
        return metadataList;
    }

    public ProjectFile(String file, ProjectPath projectPath) {
        this.file = file;
        this.path = projectPath;
    }

    public String getFile() {
        return file;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public String getHash() {
        return hash;
    }
}
