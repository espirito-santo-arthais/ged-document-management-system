import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "shortId",
  standalone: true,
  pure: true // Garantia de performance: só reexecuta se a referência do valor mudar
})
export class ShortIdPipe implements PipeTransform {

  /**
   * Transforma um UUID/ID longo em um formato amigável (ex: asdf...a43f)
   * @param value O valor a ser formatado (aceita qualquer tipo para evitar crash, mas processa apenas strings)
   * @param chars Quantidade de caracteres visíveis em cada extremidade
   * @returns String formatada ou o valor original caso não seja processável
   */
  transform(value: any, chars: number = 4): string {
    // 1. Proteção contra valores nulos, vazios ou não-strings (comum em retornos de APIs)
    if (value === null || value === undefined || typeof value !== 'string') {
      return "";
    }

    const trimmedValue = value.trim();

    // 2. Validação de segurança: se a string for menor que o dobro de chars + o separador, 
    // não faz sentido encurtar pois a string original já é curta o suficiente.
    if (trimmedValue.length <= (chars * 2) + 3) {
      return trimmedValue;
    }

    try {
      const start = trimmedValue.substring(0, chars);
      const end = trimmedValue.substring(trimmedValue.length - chars);

      return `${start}...${end}`;
    } catch (error) {
      // 3. Fail-safe: Em caso de erro inesperado na manipulação da string, 
      // retorna o valor original para não quebrar a UI
      console.error(`ShortIdPipe Error: Falha ao processar o valor ${value}`, error);
      return trimmedValue;
    }
  }
}