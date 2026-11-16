package br.com.biblioteca;

public class Livro {

    private final int exemplaresDisponiveis;

    public Livro(String isbn, String titulo, int quantidadeExemplares) {
        this.exemplaresDisponiveis = quantidadeExemplares;
    }

    public int getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }
}
