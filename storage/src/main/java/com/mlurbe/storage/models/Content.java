package com.mlurbe.storage.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "contents")
public class Content {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_id_generator")
    @SequenceGenerator(name = "content_id_generator", sequenceName = "content_id_seq", allocationSize = 1)
    @Column(name = "content_uuid", nullable = false, unique = true)
    private Long contentId;

    @Column(name = "document_uuid")
    private Long documentId;

    @Column(name = "content")
    private String contentText;

    public Long getContentId () {
        return contentId;
    }

    public Long getDocumentId () {
        return documentId;
    }

    public String getContentText () {
        return contentText;
    }

}
