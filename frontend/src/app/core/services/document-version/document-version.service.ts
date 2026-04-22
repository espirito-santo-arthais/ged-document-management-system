import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// LISTA (versão simples)
export interface DocumentVersion {
  id: number;
  fileName: string;
  version: number;
  createdAt: string;
}

// DETALHE COMPLETO (metadata)
export interface DocumentVersionMetadata {
  id: number;
  version: number;
  fileName: string;
  contentType?: string;
  size?: number;
  createdAt: string;
  storagePath?: string;
}

@Injectable({
  providedIn: 'root'
})
export class DocumentVersionService {

  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // =========================================
  // LISTAR VERSÕES
  // =========================================
  getVersions(documentId: string): Observable<DocumentVersion[]> {
    return this.http.get<DocumentVersion[]>(
      `${this.apiUrl}/documents/${documentId}/versions`
    );
  }

  // =========================================
  // BUSCAR METADATA DA VERSÃO
  // =========================================
  getVersionMetadata(
    documentId: string,
    version: number
  ): Observable<DocumentVersionMetadata> {
    return this.http.get<DocumentVersionMetadata>(
      `${this.apiUrl}/documents/${documentId}/versions/${version}/metadata`
    );
  }

  // =========================================
  // UPLOAD DE NOVA VERSÃO
  // =========================================
  upload(documentId: string, file: File) {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(
      `${this.apiUrl}/documents/${documentId}/versions`,
      formData
    );
  }

  // =========================================
  // DOWNLOAD DA VERSÃO
  // =========================================
  download(documentId: string, version: number) {
    return this.http.get(
      `${this.apiUrl}/documents/${documentId}/versions/${version}`,
      {
        responseType: 'blob'
      }
    );
  }

  // =========================================
  // DELETE DA VERSÃO
  // =========================================
  delete(documentId: string, version: number) {
    return this.http.delete(
      `${this.apiUrl}/documents/${documentId}/versions/${version}`
    );
  }
}