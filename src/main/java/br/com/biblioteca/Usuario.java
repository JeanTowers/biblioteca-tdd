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

    public void registrarDevolucao() {
        if (emprestimosAtivos == 0) {
            throw new IllegalStateException("Usuario nao possui emprestimo ativo");
        }
        emprestimosAtivos--;
    }

    public int getEmprestimosAtivos() {
        return emprestimosAtivos;
    }
}
