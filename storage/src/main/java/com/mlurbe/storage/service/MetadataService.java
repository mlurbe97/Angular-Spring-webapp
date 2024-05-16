package com.mlurbe.storage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mlurbe.storage.models.Metadata;
import com.mlurbe.storage.persistence.MetadataHibernate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetadataService {

    @Autowired
    private final MetadataHibernate metadataRepository;

    @Autowired
    public MetadataService(MetadataHibernate metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    public Optional<Metadata> getMetadataById (Long id) {
        log.info("service=getMetadataById");
        return metadataRepository.findById(id);
    }

    public List<Metadata> getAllMetadatas () {
        log.info("service=getAllMetadatas");
        return metadataRepository.findAll();
    }

    @Transactional
    public Metadata newMetadata (Metadata metadata) {
        log.info("service=newMetadata");
        return metadataRepository.save(metadata);
    }

    @Transactional
    public Metadata updateMetadata (Metadata metadata) {
        log.info("service=updateMetadata");
        return metadataRepository.save(metadata);
    }

    @Transactional
    public void deleteMetadata (Long metadataId) {
        log.info("service=deleteMetadata");
        metadataRepository.deleteById(metadataId);
    }

    @Transactional
    public void save (Metadata metadata) {
        log.info("service=save");
        metadataRepository.save(metadata);
    }

    public List<Metadata> getAllMetadatasForDoc (Long documentId) {
        // TODO:CAMBIAR EL FIND
        return metadataRepository.findByDocumentId(documentId);
    }

}
