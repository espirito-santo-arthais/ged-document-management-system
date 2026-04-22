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
  documentsPage$: Observable<Page<Document>>;

  constructor(
    private authService: AuthService,
    private router: Router,
    private documentService: DocumentService
  ) {
    this.documentsPage$ = this.documentService.search();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}