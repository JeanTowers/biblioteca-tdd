package br.com.biblioteca;

/**
 * Calcula a multa por atraso na devolucao (RN05).
 */
public class CalculadoraMulta {

    static final double VALOR_POR_DIA = 2.0;
    static final double VALOR_MAXIMO = 50.0;

    public double calcular(int diasAtraso) {
        if (diasAtraso < 0) {
            throw new IllegalArgumentException("Dias de atraso nao podem ser negativos");
        }
        return Math.min(diasAtraso * VALOR_POR_DIA, VALOR_MAXIMO);
    }
}
