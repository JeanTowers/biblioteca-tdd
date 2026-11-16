package br.com.biblioteca;

public class CalculadoraMulta {

    public double calcular(int diasAtraso) {
        return Math.min(diasAtraso * 2.0, 50.0);
    }
}
