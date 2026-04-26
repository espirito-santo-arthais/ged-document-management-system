/**
 * Representa a entidade de Documento principal.
 */
export interface Document {
    id: string;
    title: string;
    description: string;
    owner: string;
    status: string;
    createdAt: string;
    updatedAt: string;
    tags: string[];
}

/**
 * Interface para os filtros de pesquisa enviados ao backend.
 */
export interface DocumentSearchRequest {
    title?: string;
    searchType?: 'CONTAINS' | 'STARTS_WITH';
    status?: string;
    owner?: string;
    createdAfter?: string;
    createdBefore?: string;
    updatedAfter?: string;
    updatedBefore?: string;
    tags?: string[];
}

// Versão de documento - retorno do upload
export interface DocumentVersion {
  id: string; // UUID vindo do backend
  version: number;
  fileName: string;
  contentType: string;
  size: number;
  createdAt: string;
}

// Como o retorno do upload é idêntico ao Metadata, 
// podemos simplificar ou usar a mesma interface.
export interface DocumentVersionMetadata extends DocumentVersion {
  storagePath?: string;
}
