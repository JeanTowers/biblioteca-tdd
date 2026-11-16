package br.com.biblioteca;

public class Livro {

    private final int quantidadeExemplares;
    private int exemplaresDisponiveis;

    public Livro(String isbn, String titulo, int quantidadeExemplares) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN obrigatorio");
        }
        if (quantidadeExemplares <= 0) {
            throw new IllegalArgumentException("Quantidade de exemplares deve ser positiva");
        }
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
