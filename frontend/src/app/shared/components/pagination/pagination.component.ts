import { Component, Input, Output, EventEmitter, OnChanges, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoadingService } from '../../../core/services/loading/loading.service';

import { Page } from '../../../core/models';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnChanges {

  protected loadingService = inject(LoadingService);

  @Input() page!: Page<any>;
  @Input() currentPage: number = 0;
  @Input() pageSize: number = 20;

  @Output() pageChange = new EventEmitter<number>();
  @Output() pageSizeChange = new EventEmitter<number>();

  pageInput: number = 1;

  ngOnChanges(): void {
    // Só atualiza se estiver diferente
    const expected = this.currentPage + 1;

    if (this.pageInput !== expected) {
      this.pageInput = expected;
    }
  }

  next(): void {
    if (!this.page || this.currentPage + 1 >= this.page.totalPages) return;
    this.pageChange.emit(this.currentPage + 1);
  }

  prev(): void {
    if (!this.page || this.currentPage === 0) return;
    this.pageChange.emit(this.currentPage - 1);
  }

  goToPage(): void {
    let value = Number(this.pageInput);

    if (isNaN(value)) return;

    if (value < 1) value = 1;

    if (this.page && value > this.page.totalPages) {
      value = this.page.totalPages;
    }

    this.pageInput = value;

    this.pageChange.emit(value - 1);
  }

  changePageSize(size: number): void {
    this.pageSizeChange.emit(size);
  }

  go(): void {
    this.goToPage();
  }

  onInputChange(): void {
    // evita disparo durante loading
    if (this.loadingService.isLoading()) return;

    // evita valores inválidos
    if (!this.pageInput || this.pageInput < 1) return;

    if (this.pageInput > this.page.totalPages) return;

    // dispara navegação
    this.pageChange.emit(this.pageInput - 1);
  }
}