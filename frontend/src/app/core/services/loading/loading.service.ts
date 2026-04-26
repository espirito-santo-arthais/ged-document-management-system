import { Injectable, signal, computed } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LoadingService {

  // Signal privado para contar requisições ativas
  private activeRequests = signal<number>(0);

  // Signal que controla a exibição (público e apenas leitura)
  isLoading = signal<boolean>(false);

  show() {
    // Atualiza o contador imediatamente
    this.activeRequests.update(v => v + 1);

    // O setTimeout garante que o sinal de 'true' seja disparado 
    // fora do ciclo de checagem atual, matando o erro NG0100.
    setTimeout(() => {
      this.isLoading.set(true);
    });
  }

  hide() {
    this.activeRequests.update(v => Math.max(0, v - 1));

    // Só esconde quando todas as requisições terminarem
    if (this.activeRequests() === 0) {
      setTimeout(() => {
        // Verificação dupla para evitar esconder se uma nova request 
        // começou entre o timeout e a execução
        if (this.activeRequests() === 0) {
          this.isLoading.set(false);
        }
      });
    }
  }
}