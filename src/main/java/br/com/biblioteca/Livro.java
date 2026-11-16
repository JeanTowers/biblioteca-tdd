package br.com.biblioteca;

/**
 * Titulo do acervo; controla quantos exemplares estao disponiveis (RN01).
 */
public class Livro {

    private final String isbn;
    private final String titulo;
    private final int quantidadeExemplares;
    private int exemplaresDisponiveis;

    public Livro(String isbn, String titulo, int quantidadeExemplares) {
        validar(isbn, quantidadeExemplares);
        this.isbn = isbn;
        this.titulo = titulo;
        this.quantidadeExemplares = quantidadeExemplares;
        this.exemplaresDisponiveis = quantidadeExemplares;
    }

    private static void validar(String isbn, int quantidadeExemplares) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN obrigatorio");
        }
        if (quantidadeExemplares <= 0) {
            throw new IllegalArgumentException("Quantidade de exemplares deve ser positiva");
        }
    }

    public boolean estaDisponivel() {
        return exemplaresDisponiveis > 0;
    }

    public int getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }

    public void emprestarExemplar() {
        if (!estaDisponivel()) {
            throw new LivroIndisponivelException("Nao ha exemplar disponivel de: " + titulo);
        }
        exemplaresDisponiveis--;
    }

    public void devolverExemplar() {
        if (exemplaresDisponiveis == quantidadeExemplares) {
            throw new IllegalStateException("Todos os exemplares ja estao disponiveis");
        }
        exemplaresDisponiveis++;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }
}
