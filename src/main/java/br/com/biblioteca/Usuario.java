package br.com.biblioteca;

public class Usuario {

    private final TipoUsuario tipo;
    private int emprestimosAtivos;
    private double multaPendente;

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
        return emprestimosAtivos < getLimiteEmprestimos() && multaPendente == 0;
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

    public void adicionarMulta(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor da multa deve ser positivo");
        }
        multaPendente += valor;
    }

    public double getMultaPendente() {
        return multaPendente;
    }

    public void pagarMulta(double valor) {
        if (valor <= 0 || valor > multaPendente) {
            throw new IllegalArgumentException("Valor de pagamento invalido");
        }
        multaPendente -= valor;
    }
}
