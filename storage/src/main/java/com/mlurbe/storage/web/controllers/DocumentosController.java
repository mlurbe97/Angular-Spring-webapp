package com.mlurbe.storage.web.controllers;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mlurbe.storage.models.Document;
import com.mlurbe.storage.models.Metadata;
import com.mlurbe.storage.service.DocumentService;
import com.mlurbe.storage.service.MetadataService;
import com.mlurbe.storage.web.dto.DocumentDTO;
import com.mlurbe.storage.web.dto.MetadataDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/documentos")
@Slf4j
public class DocumentosController {

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    public DocumentosController(DocumentService documentService, MetadataService metadataService) {
        this.documentService = documentService;
        this.metadataService = metadataService;
    }

    @GetMapping(value = "")
    public String Hello () {
        return "Página principal para ver los documentos";
    }

    @PostMapping("/upload")
    @ResponseStatus(CREATED)
    public void uploadFile (@RequestParam("file") MultipartFile file) {

        try {
            documentService.saveAttachment(file);
        } catch (Exception e) {
            log.info("INFO: uploadFile error.");
        }

    }

    @GetMapping("/download/{id}")
    public byte[] downloadDocument (@PathVariable Long id) {
        log.info("process=get-document, doc_id={}", id);
        byte[] res = null;
        Optional<Document> documento = documentService.getDocumentById(id);
        if(documento.isPresent()) {
            Document doc = documento.get();
            return doc.getData();
        }
        return res;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DocumentDTO>> getAllFiles () {
        List<Document> documentos = documentService.getAllDocuments();
        List<DocumentDTO> responseClasses = documentos.stream()
                .map(document -> {
                    String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/documentos/download/")
                            .path(document.getDocumentId()
                                    .toString())
                            .toUriString();
                    return new DocumentDTO(
                            document.getDocumentId(),
                            document.getFileName(),
                            downloadURL,
                            document.getData().length,
                            document.getFileType(),
                            document.getDescription(), document.getMetadata());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(responseClasses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocument (@PathVariable Long id) {
        log.info("process=get-document, doc_id={}", id);
        Optional<Document> documento = documentService.getDocumentById(id);
        if (documento.isPresent()) {
            Document documentoRecup = documento.get();
            String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/documentos/download/")
                    .path(documentoRecup.getDocumentId()
                            .toString())
                    .toUriString();
            DocumentDTO sendingDoc = new DocumentDTO(documentoRecup.getDocumentId(), documentoRecup.getFileName(), downloadURL, documentoRecup.getFileSize(),
                    documentoRecup.getFileType(), documentoRecup.getDescription(), documentoRecup.getMetadata());
            return ResponseEntity.ok(sendingDoc);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<DocumentDTO> updateDocument (@PathVariable Long id, @RequestBody DocumentDTO document) {
        log.info("process=update-document, doc_id={}", id);
        // ocument.setDocumentId(id);
        Optional<Document> doc = documentService.getDocumentById(id);
        if (doc.isPresent()) {
            Document documentoRecup = doc.get();
            documentoRecup.setFileName(document.getFileName());
            documentoRecup.setDescription(document.getDescription());
            documentService.updateDocument(documentoRecup);

            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(ACCEPTED)
    public void deleteDocument (@PathVariable Long id) {
        log.info("process=delete-document, document_id={}", id);
        documentService.deleteDocument(id);
    }

    @PostMapping("/metadata/{doc_id}")
    @ResponseStatus(CREATED)
    public ResponseEntity<MetadataDTO> newDocumentMetadata (@PathVariable Long doc_id, @RequestBody MetadataDTO metadata) {
        log.info("process=create-metadata, metadata_id={}", metadata.getMetadataId());
        log.info("process=create-metadata, metadata_document_id={}", metadata.getDocumentId());
        log.info("process=create-metadata, metadata_key={}", metadata.getMetaKey());
        log.info("process=create-metadata, metadata_value={}", metadata.getMetaValue());
        Metadata new_metadata = new Metadata(metadata.getDocumentId(), metadata.getMetaKey(), metadata.getMetaValue());
        metadataService.save(new_metadata);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/metadata/{doc_id}")
    public ResponseEntity<List<MetadataDTO>> getAllMetadatas (@PathVariable Long doc_id) {
        List<Metadata> metadatas = metadataService.getAllMetadatasForDoc(doc_id);// documentService.getMetadatas(doc_id);
        List<MetadataDTO> responseClasses = metadatas.stream()
                .map(metadata -> {
                    log.info("ID DEL METADATO:" + metadata.getMetadataId()
                            .toString());
                    log.info("ID DEL DOCUMENTO:" + metadata.getDocumentId()
                            .toString());
                    return new MetadataDTO(
                            metadata.getMetadataId(),
                            metadata.getDocumentId(),
                            metadata.getKeyMeta(),
                            metadata.getValueMeta());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(responseClasses);
    }

    @PutMapping("/metadata/{metaId}")
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<MetadataDTO> updateDocumentMetadata (@PathVariable Long metaId, @RequestBody MetadataDTO metadata) {
        log.info("process=update-document-metadata, doc_id={}", metaId);
        metadataService.getMetadataById(metaId);
        String newValue = metadata.getMetaValue();
        String newKey = metadata.getMetaKey();

        Optional<Metadata> metadataRecup = metadataService.getMetadataById(metaId);

        if (metadataRecup.isPresent()) {
            Metadata actualizable = metadataRecup.get();
            actualizable.setKeyMeta(newKey);
            actualizable.setValueMeta(newValue);
            metadataService.updateMetadata(actualizable);
            return ResponseEntity.ok(metadata);
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @DeleteMapping("/metadata/{metaId}")
    @ResponseStatus(ACCEPTED)
    public void deleteMetadata (@PathVariable Long metaId) {
        log.info("process=delete-metadata, metadata_id={}", metaId);
        metadataService.deleteMetadata(metaId);
    }

}