package org.freeeed.Processor;

import org.apache.tika.io.TikaInputStream;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.Processor.FileProcessor;

import java.io.IOException;

public class SystemFileProcessor extends FileProcessor {

    public SystemFileProcessor(ProjectFile projectFile) {
        this.projectFile = projectFile;
    }

    @Override
    public void run() {



        /*
        DocumentMetadata metadata = new DocumentMetadata();
        projectFile.setMetadata(metadata);
        metadata.setOriginalPath(getOriginalDocumentPath(projectFile));
        metadata.setHasAttachments(projectFile.isHasAttachments());
        metadata.setHasParent(projectFile.isHasParent());
        // MD5Hash hash = Util.createKeyHash(discoveryFile.getPath(), metadata);
        // metadata.setHash(hash.toString());
        metadata.acquireUniqueId();
        metadata.set(DocumentMetadataKeys.PROCESSING_EXCEPTION, "System File");
        writeMetadata();
*/
    }
}
