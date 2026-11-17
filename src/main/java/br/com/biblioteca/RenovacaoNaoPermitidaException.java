package br.com.biblioteca;

public class RenovacaoNaoPermitidaException extends RuntimeException {

    public RenovacaoNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}
