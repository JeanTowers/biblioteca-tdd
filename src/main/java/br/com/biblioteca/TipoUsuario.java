package br.com.biblioteca;

/**
 * Cada tipo de usuario conhece seu limite de emprestimos (RN02) e prazo (RN04).
 * Um novo tipo e adicionado aqui, sem alterar Usuario (principio aberto/fechado).
 */
public enum TipoUsuario {
    ALUNO(3, 7),
    PROFESSOR(5, 14);

    private final int limiteEmprestimos;
    private final int prazoDevolucaoEmDias;

    TipoUsuario(int limiteEmprestimos, int prazoDevolucaoEmDias) {
        this.limiteEmprestimos = limiteEmprestimos;
        this.prazoDevolucaoEmDias = prazoDevolucaoEmDias;
    }

    public int getLimiteEmprestimos() {
        return limiteEmprestimos;
    }

    public int getPrazoDevolucaoEmDias() {
        return prazoDevolucaoEmDias;
    }
}
