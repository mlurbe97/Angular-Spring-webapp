package com.mlurbe.storage.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_id_generator")
    @SequenceGenerator(name = "document_id_generator", sequenceName = "document_id_seq", allocationSize = 1)
    @Column(name = "document_uuid", nullable = false, unique = true)
    private Long documentId;

    @Column(name = "user_uuid")
    private Long userId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private int fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "description")
    private String description;

    /*
     * @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
     * 
     * @JoinTable( name = "documents_contents", joinColumns = @JoinColumn(name = "document_content_uuid", referencedColumnName = "document_uuid"), inverseJoinColumns
     * = @JoinColumn(name = "content_document_uuid", referencedColumnName = "content_uuid")) private ArrayList<Content> documentContent;
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "documents_metadatas",
            joinColumns = @JoinColumn(name = "document_content_uuid", referencedColumnName = "document_uuid"),
            inverseJoinColumns = @JoinColumn(name = "metadata_document_uuid", referencedColumnName = "metadata_uuid"))
    private Set<Metadata> documentMetadata;

    @Lob
    private byte[] data;

    public Document(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.description = "";
    }

    public Document(String fileName, String fileType, byte[] data, String description) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.description = description;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public String getFileName () {
        return fileName;
    }

    public Long getDocumentId () {
        return documentId;
    }

    public void setDocumentId (Long documentId) {
        this.documentId = documentId;
    }

    public Long getUserId () {
        return userId;
    }

    public void setUserId (Long userId) {
        this.userId = userId;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public int getFileSize () {
        return fileSize;
    }

    /*
     * public ArrayList<Content> getContent () { return documentContent; }
     * 
     * public void setContent (ArrayList<Content> documentContent) { this.documentContent = documentContent; }
     */

    public ArrayList<Metadata> getMetadata () {
        ArrayList<Metadata> result = new ArrayList<Metadata>(documentMetadata);
        return result;
    }

    public void setMetadata (ArrayList<Metadata> documentMetadata)
    {
        Set<Metadata> result = new HashSet<>(documentMetadata);
        this.documentMetadata = result;
    }

}
