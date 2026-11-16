package br.com.biblioteca;

public class LivroIndisponivelException extends RuntimeException {

    public LivroIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
