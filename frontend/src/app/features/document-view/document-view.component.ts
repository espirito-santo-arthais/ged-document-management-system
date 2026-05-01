import { Component, OnInit, inject, ChangeDetectorRef } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ActivatedRoute } from "@angular/router";

import { of } from "rxjs";
import { switchMap, catchError } from "rxjs/operators";

import { DocumentService } from "../../core/services/document/document.service";
import { DocumentVersionService } from "../../core/services/document-version/document-version.service";

import { Document, DocumentVersionMetadata } from "../../core/models/document.model";

@Component({
  selector: "app-document-view",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./document-view.component.html",
  styleUrls: ["./document-view.component.css"],
})
export class DocumentViewComponent implements OnInit {

  // Injeções
  private cdr = inject(ChangeDetectorRef);
  private route = inject(ActivatedRoute);
  private documentService = inject(DocumentService);
  private versionService = inject(DocumentVersionService);

  // Estado
  documentId!: string;

  document?: Document;
  latestVersion?: DocumentVersionMetadata;

  loading = false;

  // Lifecycle
  ngOnInit(): void {
    this.documentId = this.route.snapshot.paramMap.get('id')!;
    this.loadData();
  }

  // Carregar dados
  private loadData(): void {
    this.loading = true;

    this.documentService.findById(this.documentId).pipe(

      switchMap((doc) => {
        this.document = doc;

        return this.versionService.getLatestVersionMetadata(this.documentId).pipe(

          catchError(() => {
            // Documento sem versão → caso normal
            this.latestVersion = undefined;
            return of(null);
          })
        );
      })
    ).subscribe({
      next: (version) => {
        this.latestVersion = version ?? undefined;
        this.loading = false;

        this.cdr.detectChanges();
      },

      error: () => {
        this.loading = false;
      }
    });
  }

  // Download
  downloadLatest(): void {
    this.versionService.downloadLatestVersion(this.documentId).subscribe({
      next: (res) => {
        if (!res.body) return;

        const blob = res.body;

        const contentDisposition = res.headers.get('content-disposition');
        const fileName = contentDisposition
          ?.split('filename=')[1]
          ?.replace(/"/g, '') || 'documento';

        const url = window.URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        a.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Erro ao baixar arquivo:', err);
      }
    });
  }
}