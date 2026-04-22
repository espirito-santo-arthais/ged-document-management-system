import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';

import { AuthService } from '../../core/services/auth/auth.service';
import { DocumentService, Document, Page } from '../../core/services/document/document.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {

  documentsPage$!: Observable<Page<Document>>;

  currentPage = 0;
  pageSize = 20;

  constructor(
    private authService: AuthService,
    private router: Router,
    private documentService: DocumentService
  ) {
    this.loadPage();
  }

  loadPage(): void {
    this.documentsPage$ = this.documentService.search(this.currentPage, this.pageSize);
  }

  nextPage(): void {
    this.currentPage++;
    this.loadPage();
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadPage();
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}