import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Document {
  id: string;
  title: string;
  description: string;
  status: string;
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

  constructor(private http: HttpClient) {}

  search(): Observable<Page<Document>> {
    return this.http.post<Page<Document>>(
      `${this.apiUrl}/documents/search`,
      {}
    );
  }
}