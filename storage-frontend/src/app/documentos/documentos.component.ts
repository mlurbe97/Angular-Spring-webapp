import { Component, OnInit } from '@angular/core';
import { StorageService } from '../_services/storage.service';

import { Metadata } from '../_interfaces/metadata.type';
import { Document } from '../_interfaces/document.type';

@Component({
  selector: 'app-documentos',
  templateUrl: './documentos.component.html',
  styleUrls: ['./documentos.component.css']
})
export class DocumentosComponent implements OnInit {
  content?: string;
  filteredDocuments: Document[] = [];
  searchTerm: string = '';
  selectedDocument: Document | null = null;
  selectedMetadata: Metadata | null = null;
  selectedMetadataID:number = -1;
  selectedMetadataKey:string = "";
  selectedMetadataValue:string = "";
  showMetadataSel:boolean = false;
  showProperty:boolean = true;
  showMetadata:boolean = false;
  documents: Document[] = [];
  metadatas: Metadata[] = [];

  constructor(private storageService: StorageService) { }

  ngOnInit(): void {
    this.getDocumentos();
  }

  goToAdd(){
    this.showMetadataSel = false;
    this.resetMetadata();
  }

  getDocumentos() {
    this.storageService.getDocumentos().subscribe(
      data => {
        this.documents = new Array<Document>();
        Object.keys(data).forEach(file => {
          var doc = new Document(data[file].documentId,data[file].fileName,data[file].downloadUrl,data[file].fileSize,data[file].fileType,data[file].description)
          this.documents.push(doc);
        });
        this.filteredDocuments = [...this.documents];
      },
      err => { console.log(err) }
    );    
  }

  searchDocuments() {
    this.filteredDocuments = this.documents.filter(doc =>
      doc.getFileName().toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  showProperties(){
    this.showProperty = true;
    this.showMetadata = false;
  }
  showMetadatas(){
    this.showProperty = false;
    this.showMetadata = true;
  }

  selectDocument(documento: Document) {
    this.selectedDocument = documento;
    this.updateMetadataList(documento);
  }

  selectMetadata(metadata: Metadata){
    this.showMetadataSel = true;
    this.selectedMetadataID = metadata.getMetadataId();
    this.selectedMetadataKey = metadata.getKey();
    this.selectedMetadataValue = metadata.getValue();
    this.selectedMetadata = metadata;
  }

  /*
    DOCUMENTOS
  */

  uploadDocument(event: any ) {
    const newDocument = event?.target.files[0];
    console.log(newDocument);
    if (newDocument){
      const formData = new FormData();
      formData.append("file",newDocument);

      this.storageService.subirDocumento(formData).subscribe(response => {
        console.log('response',response);
        location.reload();
      } )
    }
  }

  downloadDocument(documento: Document) {
    if(documento != null)
      this.storageService.descargarDocumento(documento.getDownloadURL());
    //location.reload();
  }

  deleteDocument(documento: Document) {
    if(documento != null)
      console.log("INFO: DocumentosComponent deleteDocument()");
      this.storageService.borrarDocumento(documento.getDocId());
    location.reload();
  }

  updateDocument(documento: Document) {
    if(documento != null)
      this.storageService.actualizarDocumento(documento);
    location.reload();
  }

  /*
    METADATAS
  */

    resetMetadata(){
      this.selectedMetadata = null;
      this.selectedMetadataKey = "";
      this.selectedMetadataID = -1;
      this.showMetadataSel = false;
      this.selectedMetadataValue = "";
    }

    updateMetadataList(documento: Document){   
      this.storageService.getDocumentMetadatos(documento.getDocId()).subscribe(
        data => {
          this.metadatas = new Array<Metadata>();
          Object.keys(data).forEach(file => {
            var doc = new Metadata(data[file].metadataId,data[file].documentId,data[file].metaKey,data[file].metaValue)
            this.metadatas.push(doc);
            console.log(doc);
            //location.reload();
          });
        },
        err => { console.log(err) }
      );
    }

  addMetadata(documento: Document,selectedMetadataKey:string,selectedMetadataValue:string) {
    if(documento != null && selectedMetadataKey != "" && selectedMetadataValue != ""){
      var new_meta = new Metadata(this.selectedMetadataID,documento.getDocId(),selectedMetadataKey,selectedMetadataValue);
      this.metadatas.push(new_meta);
      this.storageService.añadirMetadatos(documento,new_meta);
      this.resetMetadata();
      location.reload();
    }else {
      console.log("Sin documento seleccionado o el campo key o value estan vacíos.")
    }
  }

  deleteMetadata(documento: Document,selectedMetadataKey:string,selectedMetadataValue:string) {
    if(documento != null){
      var new_meta = new Metadata(this.selectedMetadataID,documento.getDocId(),selectedMetadataKey,selectedMetadataValue);
      this.storageService.borrarMetadatos(new_meta);
      this.resetMetadata();
      location.reload();
    }
  }

  updateMetadata(documento: Document,selectedMetadataKey:string,selectedMetadataValue:string) {
    if(documento != null){
      var actual_meta = new Metadata(this.selectedMetadataID,documento.getDocId(),selectedMetadataKey,selectedMetadataValue);
      this.storageService.actualizarMetadatos(actual_meta);
      this.resetMetadata();
      location.reload();
    }
  }
}
