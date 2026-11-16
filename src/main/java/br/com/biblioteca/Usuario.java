package br.com.biblioteca;

public class Usuario {

    private final TipoUsuario tipo;

    public Usuario(String nome, TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public int getLimiteEmprestimos() {
        return tipo == TipoUsuario.ALUNO ? 3 : 5;
    }
}
