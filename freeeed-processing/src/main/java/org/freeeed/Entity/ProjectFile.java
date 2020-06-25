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
    @JoinColumn(nullable = true, columnDefinition = "default null")
    private ProjectFile master;

    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectFile> masterList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = true, columnDefinition = "default null")
    private ProjectFile parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectFile> parentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "custodianId")
    private ProjectCustodian custodian;

    private String extension;

    private long fileSize;

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
        return extension;
    }

    public ProjectFile getMaster() {
        return master;
    }

    public void setMaster(ProjectFile master) {
        this.master = master;
    }

    public ProjectFile getParent() {
        return parent;
    }

    public List<ProjectFile> getMasterList() {
        return masterList;
    }

    public List<ProjectFile> getParentList() {
        return parentList;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
