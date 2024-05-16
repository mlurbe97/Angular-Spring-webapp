
export class Metadata {
    private metadataId:number = -1;
    private documentId:number = -1;
    private metaKey : string = "";
    private metaValue: string = "";

    constructor(metadataId:number, documentId:number, metaKey : string, metaValue: string) {
      this.metadataId = metadataId;
      this.documentId = documentId;
      this.metaKey = metaKey;
      this.metaValue = metaValue;
    }

    getMetadataId(): number{
      return this.metadataId;
    }

    getDocumentId(): number{
      return this.documentId;
    }

    getKey(): string{
      return this.metaKey;
    }

    getValue(): string{
      return this.metaValue;
    }
  }