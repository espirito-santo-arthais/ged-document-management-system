/**
 * Define a direção da ordenação.
 */
export type SortDirection = 'asc' | 'desc';

/**
 * Representa uma opção de ordenação por campo.
 */
export interface SortOption {
    field: string;
    direction: SortDirection;
}

/**
 * Interface genérica para respostas paginadas do Spring Boot.
 * O T representa o tipo de dado que a lista contém (ex: Document, User).
 */
export interface Page<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}