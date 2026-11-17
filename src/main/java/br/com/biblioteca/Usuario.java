package br.com.biblioteca;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Aluno ou professor. Controla emprestimos ativos (RN02) e multa pendente (RN03).
 * A multa e guardada em BigDecimal para evitar residuos de arredondamento do double.
 */
public class Usuario {

    private final String nome;
    private final TipoUsuario tipo;
    private int emprestimosAtivos;
    private BigDecimal multaPendente = BigDecimal.ZERO;

    public Usuario(String nome, TipoUsuario tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getLimiteEmprestimos() {
        return tipo.getLimiteEmprestimos();
    }

    public int getPrazoDevolucaoEmDias() {
        return tipo.getPrazoDevolucaoEmDias();
    }

    public boolean podeEmprestar() {
        return !atingiuLimite() && !temMultaPendente();
    }

    private boolean atingiuLimite() {
        return emprestimosAtivos >= getLimiteEmprestimos();
    }

    private boolean temMultaPendente() {
        return multaPendente.signum() > 0;
    }

    public void registrarEmprestimo() {
        if (!podeEmprestar()) {
            throw new UsuarioBloqueadoException("Usuario " + nome + " nao pode fazer novo emprestimo");
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
        multaPendente = multaPendente.add(emCentavos(valor));
    }

    public void pagarMulta(double valor) {
        BigDecimal pagamento = emCentavos(valor);
        if (pagamento.signum() <= 0 || pagamento.compareTo(multaPendente) > 0) {
            throw new IllegalArgumentException("Valor de pagamento invalido");
        }
        multaPendente = multaPendente.subtract(pagamento);
    }

    public double getMultaPendente() {
        return multaPendente.doubleValue();
    }

    public String getNome() {
        return nome;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    private static BigDecimal emCentavos(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }
}
