package br.com.biblioteca;

public class Livro {

    private int exemplaresDisponiveis;

    public Livro(String isbn, String titulo, int quantidadeExemplares) {
        this.exemplaresDisponiveis = quantidadeExemplares;
    }

    public boolean estaDisponivel() {
        return true;
    }

    public int getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }

    public void emprestarExemplar() {
        exemplaresDisponiveis--;
    }
}
