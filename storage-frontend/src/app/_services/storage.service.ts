import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Document } from '../_interfaces/document.type';
import { Metadata } from '../_interfaces/metadata.type';

const API_URL = 'http://localhost:8080/api';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    })
};

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  constructor(private http: HttpClient) {}

  status:string = "";
  errorMessage:string = "";

  getPublicContent(): Observable<any> {
    console.log("INFO: StorageService getPublicContent()");
    return this.http.get(API_URL + '/home', { responseType: 'text' });
  }

  /*
    DOCUMENTOS
  */

  getDocumentos(): Observable<any> {
    console.log("INFO: StorageService getDocumentos()");
    return this.http.get(API_URL + '/documentos/all', { responseType: 'json' });
  }

  subirDocumento(formData: FormData):Observable<any>{
    console.log("INFO: StorageService subirDocumento()");
    return this.http.post(API_URL+"/documentos/upload",formData);
  }

  actualizarDocumento(document:Document){
    var documentJSON = JSON.stringify(document);
    console.log(documentJSON);
    this.http.put(API_URL+"/documentos/"+document.getDocId().toString(),documentJSON,httpOptions)
        .subscribe({
            next: data => {
                this.status = 'Documento actualizado.';
                console.log("INFO: StorageService actualizarDocumento() ID="+document.getDocId().toString() +" "+ this.status);
            },
            error: error => {
                this.errorMessage = error.message;
                console.log("INFO: StorageService actualizarDocumento() ID="+document.getDocId().toString());
                console.error('There was an error!', error);
            }
        });
  }

  descargarDocumento(downloadUrl:string){
    console.log("INFO: StorageService descargarDocument() from "+downloadUrl);
    return this.http.get(downloadUrl, { responseType: 'text' });
  }

  borrarDocumento(documentId:number){
    this.http.delete(API_URL+"/documentos/"+documentId.toString(),httpOptions)
        .subscribe({
            next: data => {
                this.status = 'Documento borrado.';
                console.log("INFO: StorageService borrarDocumento() ID="+documentId.toString() +" "+ this.status);
            },
            error: error => {
                this.errorMessage = error.message;
                console.log("INFO: StorageService borrarDocumento() ID="+documentId.toString());
                console.error('There was an error!', error);
            }
        });
  }

  /*
    METADATOS
  */

  getDocumentMetadatos(documentId:number):  Observable<any> {
    console.log("INFO: StorageService getDocumentMetadatos()");
    return this.http.get(API_URL + '/documentos/metadata/'+documentId.toString(), { responseType: 'json' })
  }

  añadirMetadatos(document:Document,new_meta:Metadata){
    var new_meta_JSON = JSON.stringify(new_meta);
    console.log(new_meta_JSON);
    this.http.post(API_URL+"/documentos/metadata/"+document.getDocId().toString(),new_meta_JSON,httpOptions)
        .subscribe({
            next: data => {
                this.status = 'Documento actualizado.';
                console.log("INFO: StorageService añadirMetadatos() doc_ID="+document.getDocId().toString() +" "+ this.status);
            },
            error: error => {
                this.errorMessage = error.message;
                console.log("INFO: StorageService añadirMetadatos() doc_ID="+document.getDocId().toString());
                console.error('There was an error!', error);
            }
        });
  }

  borrarMetadatos(new_meta:Metadata){
    this.http.delete(API_URL+"/documentos/metadata/"+new_meta.getMetadataId(),httpOptions)
        .subscribe({
            next: data => {
                this.status = 'Metadato borrado.';
                console.log("INFO: StorageService borrarMetadatos() meta_ID="+new_meta.getMetadataId().toString() +" "+ this.status);
            },
            error: error => {
                this.errorMessage = error.message;
                console.log("INFO: StorageService borrarMetadatos() meta_ID="+new_meta.getMetadataId().toString());
                console.error('There was an error!', error);
            }
        });

  }

  actualizarMetadatos(new_meta:Metadata){
    var new_meta_JSON = JSON.stringify(new_meta);
    console.log(new_meta_JSON);
    this.http.put(API_URL+"/documentos/metadata/"+new_meta.getMetadataId(),new_meta_JSON,httpOptions)
        .subscribe({
            next: data => {
                this.status = 'Metadato actualizado.';
                console.log("INFO: StorageService actualizarMetadatos() ID="+new_meta.getMetadataId().toString() +" "+ this.status);
            },
            error: error => {
                this.errorMessage = error.message;
                console.log("INFO: StorageService actualizarMetadatos() ID="+new_meta.getMetadataId().toString());
                console.error('There was an error!', error);
            }
        });

  }

}
