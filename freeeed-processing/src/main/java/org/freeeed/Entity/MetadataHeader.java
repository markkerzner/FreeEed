package org.freeeed.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MetadataHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int metadataId;

    private String name;

    public MetadataHeader() {
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
