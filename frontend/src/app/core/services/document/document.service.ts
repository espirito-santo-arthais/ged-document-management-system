import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DocumentSearchRequest {
  title?: string;
  searchType?: 'CONTAINS' | 'STARTS_WITH';
  status?: string;
  owner?: string;
  createdAfter?: string;
  createdBefore?: string;
  updatedAfter?: string;
  updatedBefore?: string;
  tags?: string[];
}

export interface Document {
  id: string;
  title: string;
  description: string;
  owner: string;
  status: string;
  createdAt: string;
  updatedAt: string;
  tags: string[];
}

export interface SortOption {
  field: string;
  direction: 'asc' | 'desc';
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root',
})
export class DocumentService {

  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  /**
   * Realiza a busca paginada de documentos com suporte a ordenação múltipla.
   * Valores padrão: página 0, tamanho 10, ordenado por título ascendente.
   */
  search(
    filters: DocumentSearchRequest = {},
    page: number = 0,
    size: number = 20,
    sorts: SortOption[] = [{ field: 'title', direction: 'asc' }]
  ): Observable<Page<Document>> {

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    // Mantendo a lógica de append para múltiplos campos de ordenação
    sorts.forEach(s => {
      params = params.append('sort', `${s.field},${s.direction}`);
    });

    return this.http.post<Page<Document>>(
      `${this.apiUrl}/documents/search`,
      filters,
      { params }
    );
  }
}