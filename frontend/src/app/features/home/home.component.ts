import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';

import { AuthService } from '../../core/services/auth/auth.service';
import {
  DocumentService,
  DocumentSearchRequest,
  Document,
  Page
} from '../../core/services/document/document.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {

  documentsPage$!: Observable<Page<Document>>;

  currentPage = 0;
  pageSize = 20;

  tagInput = '';

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

  constructor(
    private authService: AuthService,
    private router: Router,
    private documentService: DocumentService
  ) {
    this.loadPage();
  }

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

  private buildFilters(): DocumentSearchRequest {
    const f: DocumentSearchRequest = {};

    if (this.filters.title?.trim()) {
      f.title = this.filters.title;
      f.searchType = this.filters.searchType || 'CONTAINS';
    }

    if (this.filters.status) {
      f.status = this.filters.status;
    }

    if (this.filters.owner?.trim()) {
      f.owner = this.filters.owner;
    }

    if (this.filters.createdAfter) {
      f.createdAfter = this.filters.createdAfter;
    }

    if (this.filters.createdBefore) {
      f.createdBefore = this.filters.createdBefore;
    }

    if (this.filters.updatedAfter) {
      f.updatedAfter = this.filters.updatedAfter;
    }

    if (this.filters.updatedBefore) {
      f.updatedBefore = this.filters.updatedBefore;
    }

    if (this.filters.tags && this.filters.tags.length > 0) {
      f.tags = this.filters.tags;
    }

    return f;
  }

  loadPage(): void {
    this.documentsPage$ = this.documentService.search(
      this.buildFilters(),
      this.currentPage,
      this.pageSize
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

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}