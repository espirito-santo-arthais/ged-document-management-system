import { Component, OnInit, inject, ChangeDetectorRef } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HttpErrorResponse } from "@angular/common/http";
import { ActivatedRoute } from "@angular/router";

import { of, throwError } from "rxjs";
import { switchMap, catchError } from "rxjs/operators";


import { ShortIdPipe } from '../../shared/pipes/short-id-pipe';
import { DocumentService } from "../../core/services/document/document.service";
import { DocumentVersionService } from "../../core/services/document-version/document-version.service";

import { Document, DocumentVersionMetadata } from "../../core/models/document.model";


@Component({
  selector: "app-document-view",
  standalone: true,
  imports: [CommonModule, ShortIdPipe],
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
  versions: DocumentVersionMetadata[] = [];
  latestVersion?: DocumentVersionMetadata;
  selectedVersion?: DocumentVersionMetadata;
  closingVersion?: DocumentVersionMetadata;

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

        return this.versionService.getVersions(this.documentId).pipe(
          catchError((error: HttpErrorResponse) => {

            if (error.status === 404) {
              // Documento sem versões → caso normal
              this.latestVersion = undefined;
              this.selectedVersion = undefined;
              return of([]);
            }

            // Re-lança qualquer outro tipo de erro. Apesar de que os interceptors
            // tratam todos os outros tipos de erros menos o erro 404 (Not Found).
            return throwError(() => error);
          })
        );
      })
    ).subscribe({
      next: (versions) => {
        this.versions = versions || [];

        this.latestVersion = this.versions[0] ?? undefined;
        this.selectedVersion = undefined;

        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.loading = false;

        console.error('Erro ao carregar dados:', error);

        // 👉 aqui você pode:
        // this.toastService.error("Erro ao carregar documento");
        // this.router.navigate(['/erro']);
      }
    });
  }

  // subir versão
  uploadNewVersion(): void {
    alert("Implementar upload aqui");
  }

  // Baixar versão mais recente 
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

  // visualizar metados de versão específica
  selectVersion(v: DocumentVersionMetadata): void {

    if (this.selectedVersion === v) {
      // iniciar fechamento
      this.closingVersion = v;

      setTimeout(() => {
        this.selectedVersion = undefined;
        this.closingVersion = undefined;
      }, 300); // mesmo tempo do CSS

    } else {
      this.selectedVersion = v;
      this.closingVersion = undefined;
    }
  }

  // baixar versão específica
  downloadVersion(version: number): void {
    this.versionService.downloadVersion(this.documentId, version).subscribe({
      next: (res) => {
        if (!res.body) return;

        const blob = res.body;
        const fileName = res.headers.get('content-disposition')
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

  // excluir versão específica
  deleteVersion(version: number): void {
    if (!confirm("Confirma a exclusão da versão do documento?")) return;

    this.versionService.delete(this.documentId, version).subscribe({
      next: () => {
        this.versions = this.versions.filter(v => v.id !== this.selectedVersion?.id);
      },
      error: (err) => {
        // O interceptor já formatou a mensagem em um objeto Error
        alert(err.userMessage);
        console.error('Erro ao deletar:', err.message);
      }
    });
  }
}