package org.freeeed.Entity;

import javax.persistence.*;

@Entity
public class ProjectMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int metadataId;

    @Lob
    private String name;


    @ManyToOne
    @JoinColumn(name = "metaheaderId")
    private MetadataHeader header;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private ProjectFile file;

    public ProjectMetadata() {
    }

    public ProjectMetadata(String name, MetadataHeader header, ProjectFile file) {
        this.name = name;
        this.header = header;
        this.file = file;
    }

    public ProjectFile getFile() {
        return file;
    }

    public MetadataHeader getHeader() {
        return header;
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
