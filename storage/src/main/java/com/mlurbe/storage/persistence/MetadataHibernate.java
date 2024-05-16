package com.mlurbe.storage.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlurbe.storage.models.Metadata;

@Repository
public interface MetadataHibernate extends JpaRepository<Metadata, Long> {

    List<Metadata> findByDocumentId (Long documentId);

}
