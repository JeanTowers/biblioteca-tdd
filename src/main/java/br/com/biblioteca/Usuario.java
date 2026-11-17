package br.com.biblioteca;

public class Usuario {

    private final TipoUsuario tipo;
    private int emprestimosAtivos;

    public Usuario(String nome, TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public int getLimiteEmprestimos() {
        return tipo.getLimiteEmprestimos();
    }

    public int getPrazoDevolucaoEmDias() {
        return tipo.getPrazoDevolucaoEmDias();
    }

    public boolean podeEmprestar() {
        return emprestimosAtivos < getLimiteEmprestimos();
    }

    public void registrarEmprestimo() {
        if (!podeEmprestar()) {
            throw new UsuarioBloqueadoException("Usuario nao pode fazer novo emprestimo");
        }
        emprestimosAtivos++;
    }

    public int getEmprestimosAtivos() {
        return emprestimosAtivos;
    }
}
