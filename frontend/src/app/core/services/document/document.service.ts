import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

// Importações limpas dos nossos modelos centralizados
import { Document, DocumentSearchRequest } from '../../models/document.model';
import { Page, SortOption } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  // Padronizamos a URL base para evitar repetição do sufixo /documents
  private readonly apiUrl = `${environment.apiUrl}/documents`;

  constructor(private httpClient: HttpClient) { }

  /**
   * Remove um documento permanentemente.
   * O Token, o Spinner e o Erro são tratados automaticamente pela infraestrutura.
   * @param id Identificador único do documento.
  */
  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Busca um documento pelo seu ID.
   * Retorna os metadados completos do documento.
   * O Token, o Spinner e o Erro são tratados automaticamente pela infraestrutura.
   * @param id Identificador único do documento.
   */
  findById(id: string): Observable<Document> {
    return this.httpClient.get<Document>(`${this.apiUrl}/${id}`);
  }

  /**
   * Realiza a busca paginada de documentos com suporte a ordenação múltipla.
   * Retorna os metadados completos dos documentos contidos na página retornada.
   * O Token, o Spinner e o Erro são tratados automaticamente pela infraestrutura.
   * @param filters Critérios de busca.
   * @param page Índice da página (zero-based).
   * @param size Quantidade de itens por página.
   * @param sorts Configurações de ordenação.
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

    // Abordagem limpa para concatenar múltiplos sorts para o Spring Data
    sorts.forEach(s => {
      params = params.append('sort', `${s.field},${s.direction}`);
    });

    return this.httpClient.post<Page<Document>>(
      `${this.apiUrl}/search`,
      filters,
      { params }
    );
  }
}