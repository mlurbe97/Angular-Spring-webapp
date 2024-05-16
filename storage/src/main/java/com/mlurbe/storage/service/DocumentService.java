package com.mlurbe.storage.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.mlurbe.storage.models.Document;
import com.mlurbe.storage.models.Metadata;
import com.mlurbe.storage.persistence.DocumentHibernate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentService {

    @Autowired
    private final DocumentHibernate documentRepository;

    @Autowired
    public DocumentService(DocumentHibernate documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Optional<Document> getDocumentById (Long id) {
        log.info("service=getDocumentById");
        return documentRepository.findById(id);
    }

    public List<Document> getAllDocuments () {
        log.info("service=getAllDocuments");
        return documentRepository.findAll();
    }

    @Transactional
    public Document createDocument (Document document) {
        log.info("service=createDocument");
        return documentRepository.save(document);
    }

    @Transactional
    public Document updateDocument (Document document) {
        log.info("service=updateDocument");
        return documentRepository.save(document);
    }

    @Transactional
    public void deleteDocument (Long documentId) {
        log.info("service=deleteDocument");
        documentRepository.deleteById(documentId);
    }

    @Transactional
    public void save (Document document) {
        log.info("service=save");
        documentRepository.save(document);
    }

    public ArrayList<Metadata> getMetadatas (Long documentId) {
        return documentRepository.findById(documentId)
                .get()
                .getMetadata();
    }

    public Metadata setMetadato (Metadata meta) {
        ArrayList<Metadata> metas = documentRepository.findById(meta.getDocumentId())
                .get()
                .getMetadata();
        metas.add(meta);
        return meta;
    }

    @Transactional
    public Document saveAttachment (MultipartFile file) throws Exception {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {

            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence " + fileName);
            }
            if (file.getBytes().length > (1024 * 1024)) {
                throw new Exception("File size exceeds maximum limit");
            }
            Document attachment = new Document(fileName, file.getContentType(), file.getBytes());
            return documentRepository.save(attachment);
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(file.getSize());
        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }
    /*
     * @Transactional public MultipartFile getAttachment (Long docId) {
     * 
     * MultipartFile file = new MultipartFile(); Optional<Document> attachment = documentRepository.findById(docId); if (attachment.isPresent()) { Document documento =
     * attachment.get(); file = documento.getData(); } return file; }
     */

    @Transactional
    public void saveFiles (MultipartFile[] files) {

        Arrays.asList(files)
                .forEach(file -> {
                    try {
                        saveAttachment(file);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    /*
     * public List<Document> getAllFiles () { return documentRepository.findAll(); }
     */


}
