package br.com.biblioteca;

public class CalculadoraMulta {

    public double calcular(int diasAtraso) {
        if (diasAtraso < 0) {
            throw new IllegalArgumentException("Dias de atraso nao podem ser negativos");
        }
        return Math.min(diasAtraso * 2.0, 50.0);
    }
}
