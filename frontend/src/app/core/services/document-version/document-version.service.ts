import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { DocumentVersion, DocumentVersionMetadata } from '../..//models/document.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentVersionService {
  // Centralizamos no recurso pai 'documents'
  private readonly apiUrl = `${environment.apiUrl}/documents`;

  constructor(private http: HttpClient) { }

  /**
   * Lista todas as versões de um documento.
   */
  getVersions(documentId: string): Observable<DocumentVersion[]> {
    return this.http.get<DocumentVersion[]>(`${this.apiUrl}/${documentId}/versions`);
  }

  /**
   * Busca os metadados detalhados de uma versão específica.
   */
  getVersionMetadata(documentId: string, version: number): Observable<DocumentVersionMetadata> {
    return this.http.get<DocumentVersionMetadata>(
      `${this.apiUrl}/${documentId}/versions/${version}/metadata`
    );
  }

  /**
   * Upload de nova versão utilizando FormData.
   * O backend retorna o DocumentVersion completo após o sucesso.
   */
  upload(documentId: string, file: File): Observable<DocumentVersion> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<DocumentVersion>(
      `${this.apiUrl}/${documentId}/versions`,
      formData
    );
  }

  /**
   * Download da versão em formato binário (Blob).
   */
  download(documentId: string, version: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${documentId}/versions/${version}`, {
      responseType: 'blob'
    });
  }

  /**
   * Remove uma versão específica.
   */
  delete(documentId: string, version: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${documentId}/versions/${version}`);
  }
}