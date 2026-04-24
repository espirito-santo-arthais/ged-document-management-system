import { Component } from "@angular/core";
import { Router } from '@angular/router';
import { CommonModule } from "@angular/common";
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';

import { ShortIdPipe } from '../../shared/pipes/short-id-pipe';
import { AuthService } from '../../core/services/auth/auth.service';
import {
  DocumentSearchRequest,
  Document,
  Page,
  SortOption,
  DocumentService,
} from '../../core/services/document/document.service';
import { Title } from "@angular/platform-browser";

@Component({
  selector: "app-document-list",
  standalone: true,
  imports: [CommonModule, FormsModule, ShortIdPipe],
  templateUrl: "./document-list.component.html",
  styleUrls: ["./document-list.component.css"],
})
export class DocumentListComponent {

  documentsPage$!: Observable<Page<Document>>;

  // Objeto para controlar a exibição das colunas
  showFields = {
    Title: true,
    description: true,
    status: true,
    owner: true,
    createdAt: true,
    updatedAt: true,
    tags: true
  };

  // Mapeamento de campos técnicos para nomes amigáveis
  columnMap: { [key: string]: string } = {
    'title': 'Título',
    'description': 'Descrição',
    'status': 'Status',
    'owner': 'Proprietário',
    'createdAt': 'Criado em',
    'updatedAt': 'Atualizado em'
  };

  // Array de chaves técnicas para iterar no HTML
  sortableFields = Object.keys(this.columnMap);

  // Array para controlar a ordenação múltipla
  activeSorts: SortOption[] = [];

  filters: DocumentSearchRequest = {
    title: '',
    searchType: 'CONTAINS',
    status: '',
    owner: '',
    createdAfter: '',
    createdBefore: '',
    updatedAfter: '',
    updatedBefore: '',
    tags: []
  };

  tagInput = '';

  currentPage = 0;
  pageSize = 20;

  constructor(
    private authService: AuthService,
    private router: Router,
    private documentService: DocumentService
  ) {
    this.loadPage();
  }

  // =========================================
  // ORDENAÇÃO (SORT)
  // =========================================

  addSort(field: string, direction: 'asc' | 'desc'): void {
    // Remove o campo se já existir para garantir a nova posição/sentido na fila
    this.activeSorts = this.activeSorts.filter(s => s.field !== field);

    this.activeSorts.push({ field, direction });
    this.loadPage();
  }

  removeSort(index: number): void {
    this.activeSorts.splice(index, 1);
    this.loadPage();
  }

  clearSorts(): void {
    this.activeSorts = [];
    this.loadPage();
  }

  // =========================================
  // ORDENAÇÃO (ADICIONAR/REMOVER)
  // =========================================

  toggleSort(field: string) {
    const index = this.activeSorts.findIndex(s => s.field === field);

    if (index > -1) {
      // Se já existe, inverte a ordem ou remove
      if (this.activeSorts[index].direction === 'asc') {
        this.activeSorts[index].direction = 'desc';
      } else {
        this.activeSorts.splice(index, 1); // Remove se clicar de novo após desc
      }
    } else {
      // Adiciona nova ordenação no final da fila
      this.activeSorts.push({ field, direction: 'asc' });
    }
    this.applyFilters();
  }

  // =========================================
  // TAGS (ADICIONAR/REMOVER)
  // =========================================
  addTag(): void {
    const value = this.tagInput.trim();

    if (value && !this.filters.tags?.includes(value)) {
      this.filters.tags?.push(value);
    }

    this.tagInput = '';
  }

  removeTag(tag: string): void {
    this.filters.tags = this.filters.tags?.filter(t => t !== tag);
  }

  // =========================================
  // FILTERS
  // =========================================
  private buildFilters(): DocumentSearchRequest {
    const f: DocumentSearchRequest = { ...this.filters };

    // Limpeza de campos vazios para não enviar sujeira na request
    if (!f.title?.trim()) delete f.title;
    if (!f.status) delete f.status;
    if (!f.owner?.trim()) delete f.owner;
    if (!f.createdAfter?.trim()) delete f.createdAfter;
    if (!f.createdBefore?.trim()) delete f.createdBefore;
    if (!f.updatedAfter?.trim()) delete f.updatedAfter;
    if (!f.updatedBefore?.trim()) delete f.updatedBefore;
    if (f.tags?.length === 0) delete f.tags;

    return f;
  }

  // =========================================
  // PAGINAÇÃO E CARREGAMENTO
  // =========================================
  loadPage(): void {
    this.documentsPage$ = this.documentService.search(
      this.buildFilters(),
      this.currentPage,
      this.pageSize,
      this.activeSorts
    );
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadPage();
  }

  nextPage(page: Page<Document>): void {
    if (page.number + 1 >= page.totalPages) return;
    this.currentPage++;
    this.loadPage();
  }

  prevPage(page: Page<Document>): void {
    if (page.number === 0) return;
    this.currentPage--;
    this.loadPage();
  }

  goToPage(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = Number(input.value);

    if (isNaN(value) || value < 1) return;

    this.currentPage = value - 1;
    this.loadPage();
  }


  createDocument(event: Event): void {
    console.log('createDocument foi acionado');
  }

  viewDocument(event: Event, documentId: string): void {
    console.log('viewDocument foi acionado');
  }

  updateDocument(event: Event, documentId: string): void {
    console.log('updateDocument foi acionado');
  }

  deleteDocument(event: Event, documentId: string): void {
    console.log('deleteDocument foi acionado');
  }

}