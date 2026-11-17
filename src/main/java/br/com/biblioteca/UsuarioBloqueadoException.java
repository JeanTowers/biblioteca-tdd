package br.com.biblioteca;

public class UsuarioBloqueadoException extends RuntimeException {

    public UsuarioBloqueadoException(String mensagem) {
        super(mensagem);
    }
}
