package br.com.biblioteca;

public class Livro {

    private final int quantidadeExemplares;
    private int exemplaresDisponiveis;

    public Livro(String isbn, String titulo, int quantidadeExemplares) {
        this.quantidadeExemplares = quantidadeExemplares;
        this.exemplaresDisponiveis = quantidadeExemplares;
    }

    public boolean estaDisponivel() {
        return exemplaresDisponiveis > 0;
    }

    public int getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }

    public void emprestarExemplar() {
        if (!estaDisponivel()) {
            throw new LivroIndisponivelException("Nao ha exemplar disponivel");
        }
        exemplaresDisponiveis--;
    }

    public void devolverExemplar() {
        if (exemplaresDisponiveis == quantidadeExemplares) {
            throw new IllegalStateException("Todos os exemplares ja estao disponiveis");
        }
        exemplaresDisponiveis++;
    }
}
