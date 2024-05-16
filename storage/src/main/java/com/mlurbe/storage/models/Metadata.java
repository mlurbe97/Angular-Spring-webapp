package com.mlurbe.storage.models;

import java.sql.Timestamp;

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
@Table(name = "metadatas")
public class Metadata {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadata_id_seq")
    @SequenceGenerator(name = "metadata_id_generator", sequenceName = "metadata_id_seq", allocationSize = 1)
    @Column(name = "metadata_uuid", nullable = false, unique = true)
    private Long metadataId;

    @Column(name = "document_uuid", nullable = false)
    private Long documentId;

    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

    @Column(name = "key_meta")
    private String keyMeta;

    @Column(name = "value_meta")
    private String valueMeta;

    public Metadata(long documentId, String key, String value) {
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.updateTime = this.createTime;
        this.keyMeta = key;
        this.valueMeta = value;
        this.documentId = documentId;
    }

    public Long getMetadataId () {
        return metadataId;
    }

    public Long getDocumentId () {
        return documentId;
    }

    public Timestamp getCreateTime () {
        return createTime;
    }

    public void setCreateTime (Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime () {
        return updateTime;
    }

    public void getUpdateTime (Timestamp updateTime) {
        this.updateTime = updateTime;
    }

}
