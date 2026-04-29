import { Component, ChangeDetectorRef, OnInit, inject } from "@angular/core";
import { Router } from '@angular/router';
import { CommonModule } from "@angular/common";
import { FormsModule } from '@angular/forms';

// SHARED & CORE
import { ShortIdPipe } from '../../shared/pipes/short-id-pipe';
import { AuthService } from '../../core/services/auth/auth.service';
import { LoadingService } from '../../core/services/loading/loading.service';
import { DocumentService } from '../../core/services/document/document.service';

// MODELS - IMPORTAÇÃO CORRETA DOS MODELOS
import {
  Document,
  DocumentSearchRequest
} from '../../core/models/document.model';
import {
  Page,
  SortOption
} from '../../core/models/pagination.model';

import { PaginationComponent } from '../../shared/components/pagination/pagination.component';

@Component({
  selector: "app-document-list",
  standalone: true,
  imports: [CommonModule, FormsModule, ShortIdPipe, PaginationComponent],
  templateUrl: "./document-list.component.html",
  styleUrls: ["./document-list.component.css"],
})
export class DocumentListComponent implements OnInit {
  // Injeções Modernas
  private authService = inject(AuthService);
  private router = inject(Router);
  private documentService = inject(DocumentService);
  private cdr = inject(ChangeDetectorRef);
  protected loadingService = inject(LoadingService);

  // Estado da Lista
  page: Page<Document> = {
    content: [],
    totalElements: 0,
    totalPages: 0,
    size: 20,
    number: 0
  };

  documents: Document[] = [];

  // Configurações de Exibição
  showFields = {
    title: true, // sempre true
    description: false,
    status: true,
    owner: false,
    createdAt: false,
    updatedAt: false,
    tags: true
  };

  columns = [
    { key: 'title', label: 'Título', required: true },
    { key: 'description', label: 'Descrição' },
    { key: 'status', label: 'Status' },
    { key: 'owner', label: 'Proprietário' },
    { key: 'createdAt', label: 'Criado em' },
    { key: 'updatedAt', label: 'Atualizado em' },
    { key: 'tags', label: 'Tags' }
  ];

  columnMap: { [key: string]: string } = {
    'title': 'Título',
    'description': 'Descrição',
    'status': 'Status',
    'owner': 'Proprietário',
    'createdAt': 'Criado em',
    'updatedAt': 'Atualizado em'
  };

  sortableFields = Object.keys(this.columnMap);
  activeSorts: SortOption[] = [{ field: 'title', direction: 'asc' }];

  // Filtros
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

  ngOnInit(): void {
    this.loadPage();
  }

  // =========================================
  // ORDENAÇÃO (SORT)
  // =========================================

  addSort(field: string, direction: 'asc' | 'desc'): void {
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

  toggleSort(field: string): void {
    const index = this.activeSorts.findIndex(s => s.field === field);

    if (index > -1) {
      if (this.activeSorts[index].direction === 'asc') {
        this.activeSorts[index].direction = 'desc';
      } else {
        this.activeSorts.splice(index, 1);
      }
    } else {
      this.activeSorts.push({ field, direction: 'asc' });
    }
    this.applyFilters();
  }

  // =========================================
  // TAGS
  // =========================================
  addTag(): void {
    const value = this.tagInput.trim();
    if (value && !this.filters.tags?.includes(value)) {
      if (!this.filters.tags) this.filters.tags = [];
      this.filters.tags.push(value);
    }
    this.tagInput = '';
  }

  removeTag(tag: string): void {
    this.filters.tags = this.filters.tags?.filter(t => t !== tag);
  }

  // =========================================
  // FILTERS & PAGINAÇÃO
  // =========================================
  private buildFilters(): DocumentSearchRequest {
    const f: DocumentSearchRequest = { ...this.filters };

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

  loadPage(): void {
    this.documentService.search(
      this.buildFilters(),
      this.currentPage,
      this.pageSize,
      this.activeSorts
    ).subscribe({
      next: (page) => {
        this.page = page;
        this.documents = page.content;
        this.cdr.detectChanges();
      },
      error: (err) => {
        // Erro já vem formatado pelo interceptor
        console.error('Erro na busca:', err.message);
      }
    });
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
    let value = Number(input.value);

    if (isNaN(value)) return;

    if (value < 1) {
      value = 1;
    }

    if (this.page && value > this.page.totalPages) {
      value = this.page.totalPages;
    }

    // 🔄 atualiza o input visualmente
    input.value = value.toString();

    this.currentPage = value - 1;
    this.loadPage();
  }

  // =========================================
  // AÇÕES
  // =========================================
  createDocument(event: Event): void {
    this.router.navigate(['/documents/new']);
  }

  viewDocument(event: Event, documentId: string): void {
    this.router.navigate(['/documents', documentId]);
  }

  updateDocument(event: Event, documentId: string): void {
    this.router.navigate(['/documents', documentId, 'edit']);
  }

  deleteDocument(event: Event, documentId: string): void {
    if (!confirm('Confirma exclusão do documento?')) return;

    this.documentService.delete(documentId).subscribe({
      next: () => {
        this.documents = this.documents.filter(doc => doc.id !== documentId);
        if (this.page) {
          this.page.totalElements--;
        }
        if (this.documents.length === 0 && this.currentPage > 0) {
          this.currentPage--;
          this.loadPage();
        }
      },
      error: (err) => {
        // O interceptor já formatou a mensagem em um objeto Error
        alert(err.message);
        console.error('Erro ao deletar:', err.message);
      }
    });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadPage();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadPage();
  }
}