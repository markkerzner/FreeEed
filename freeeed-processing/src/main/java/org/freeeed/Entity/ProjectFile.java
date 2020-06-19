package org.freeeed.Entity;

import org.freeeed.ServiceDao.CustodianService;
import org.freeeed.util.Util;

import javax.persistence.*;
import java.io.File;
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
    @JoinColumn(name = "custodianId")
    private ProjectCustodian custodian;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMetadata> metadataList = new ArrayList<>();

    public ProjectFile() {
    }

    public ProjectFile(String file, ProjectCustodian custodian) {
        this.file = file;
        this.custodian = custodian;
    }

    public ProjectFile(String file, Project project) {
        this.file = file;
        custodian = CustodianService.getInstance().getCustodianByName(Util.getCustodianFromPath(new File(file)), project);
    }

    public int getFileId() {
        return fileId;
    }

    public ProjectCustodian getCustodian() {
        return custodian;
    }

    public void setCustodian(ProjectCustodian custodian) {
        this.custodian = custodian;
    }

    public List<ProjectMetadata> getMetadataList() {
        return metadataList;
    }

    public String getFilePath() {
        return file;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public File getFile() {
        return new File(file);
    }

    public String getExtension() {
        return Util.getExtension(this.getFile().getName());
    }
}
