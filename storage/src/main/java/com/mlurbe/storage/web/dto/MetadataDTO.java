package com.mlurbe.storage.web.dto;

import com.mlurbe.storage.models.Metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDTO {

    private long metadataId;
    private long documentId;
    private String metaKey;
    private String metaValue;

    public MetadataDTO(Metadata metadata) {
        super();
        this.metadataId = metadata.getMetadataId();
        this.documentId = metadata.getDocumentId();
        this.metaKey = metadata.getKeyMeta();
        this.metaValue = metadata.getValueMeta();
        
    }

    public long getMetadataId () {
        return this.metadataId;
    }

    public void setMetadataId (long metadataId) {
        this.metadataId = metadataId;
    }

    public long getDocumentId () {
        return this.documentId;
    }

    public void setDocumentId (long documentId) {
        this.documentId = documentId;
    }

    public String getMetaKey () {
        return this.metaKey;
    }

    public void setMetaKey (String metaKey) {
        this.metaKey = metaKey;
    }

    public String getMetaValue () {
        return this.metaValue;
    }

    public void setMetaValue (String metaValue) {
        this.metaValue = metaValue;
    }

}
