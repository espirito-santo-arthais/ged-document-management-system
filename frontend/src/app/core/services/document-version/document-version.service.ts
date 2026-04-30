import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { DocumentVersion, DocumentVersionMetadata } from '../../models/document.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentVersionService {
  // Centralizamos no recurso pai 'documents'
  private readonly apiUrl = `${environment.apiUrl}/documents`;

  constructor(private http: HttpClient) { }

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
   * Remove uma versão específica.
   */
  delete(documentId: string, version: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${documentId}/versions/${version}`);
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
   * Download de uma versão específica em formato binário (Blob).
   */
  downloadVersion(documentId: string, version: number): Observable<HttpResponse<Blob>> {
    return this.http.get(`${this.apiUrl}/${documentId}/versions/${version}/download`, {
      responseType: 'blob',
      observe: 'response'
    });
  }

  /**
   * Busca os metadados da versão mais recente de um documento.
   */
  getLatestVersionMetadata(documentId: string): Observable<DocumentVersionMetadata> {
    return this.http.get<DocumentVersionMetadata>(
      `${this.apiUrl}/${documentId}/versions/latest/metadata`
    );
  }

  /**
   * Download da versão mais recente em formato binário (Blob).
   */
  downloadLatestVersion(documentId: string): Observable<HttpResponse<Blob>> {
    return this.http.get(`${this.apiUrl}/${documentId}/versions/latest/download`, {
      responseType: 'blob',
      observe: 'response'
    });
  }

  /**
   * Lista todas as versões de um documento.
   */
  getVersions(documentId: string): Observable<DocumentVersion[]> {
    return this.http.get<DocumentVersion[]>(`${this.apiUrl}/${documentId}/versions`);
  }
}