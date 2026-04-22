import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Document {
  id: string;
  title: string;
  description: string;
  status: string;
  tags: string[];
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

  search(page: number = 0, size: number = 10): Observable<Page<Document>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', 'title,asc');

    return this.http.post<Page<Document>>(
      `${this.apiUrl}/documents/search`,
      {},
      { params }
    );
  }
}