
import { Metadata } from './metadata.type';

export class Document {
     documentId:number = -1;
     fileName:string = "";
     downloadUrl:string = "";
     fileSize:number = 0;
     fileType:string = "";
     description: string = "";
     metadatas: Metadata[] = [];

    constructor(
      documentId:number,
      fileName:string,
      downloadUrl:string,
      fileSize:number,
      fileType:string,
      description:string)
    {
      this.documentId = documentId;
      this.fileName = fileName;
      this.downloadUrl = downloadUrl;
      this.fileSize = fileSize;
      this.fileType = fileType;
      this.description = description;
    }
    /*constructor(fileName : string, description: string,metadatas: Metadata[]) {  (3)
      this.fileName = fileName;
      this.description = description;
      this.metadatas = metadatas;
    }*/

    getDocId(): number {
      return this.documentId;
    }

    getFileName(): string {
      return this.fileName;
    }

    getDescription(): string {
      return this.description;
    }

    getFileType(): string {
      return this.fileType;
    }

    getFileSize(): number {
      return this.fileSize;
    }

    getDownloadURL(): string{
      return this.downloadUrl;
    }

    getMetadatas(): Metadata[]{
      return this.metadatas;
    }

    setMetadatas(metadatas: Metadata){
      this.metadatas.push(metadatas);
    }
}