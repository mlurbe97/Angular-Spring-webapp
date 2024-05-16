package com.mlurbe.storage.web.dto;

import java.util.ArrayList;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mlurbe.storage.models.Document;
import com.mlurbe.storage.models.Metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    private long documentId;
    private String fileName;
    private String downloadUrl;
    private long fileSize;
    private String fileType;
    private String description;
    private ArrayList<Metadata> metadatas;

    public DocumentDTO(Document document) {
        super();
        this.documentId = document.getDocumentId();
        this.fileName = document.getFileName();
        this.downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/documentos/download/")
                .path(document.getDocumentId()
                        .toString())
                .toUriString();
        ;
        this.fileSize = document.getFileSize();
        this.fileType = document.getFileType();
        this.description = document.getDescription();
        this.metadatas = document.getMetadata();
    }

    public long getDocumentId () {
        return this.documentId;
    }

    public void setDocumentId (long documentId) {
        this.documentId = documentId;
    }

    public String getFileName () {
        return this.fileName;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl () {
        return this.downloadUrl;
    }

    public void setDownloadUrl (String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public long getFileSize () {
        return this.fileSize;
    }

    public void setFileSize (long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType () {
        return this.fileType;
    }

    public void setFileType (String fileType) {
        this.fileType = fileType;
    }

    public String getDescription () {
        return this.description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public ArrayList<Metadata> getMetadata () {
        return this.metadatas;
    }

    public void setMetadatas (ArrayList<Metadata> metadatas) {
        this.metadatas = metadatas;
    }

}
