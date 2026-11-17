package br.com.biblioteca;

import java.time.LocalDate;

public class Emprestimo {

    private final LocalDate dataPrevistaDevolucao;

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo) {
        if (!usuario.podeEmprestar()) {
            throw new UsuarioBloqueadoException("Usuario nao pode fazer novo emprestimo");
        }
        livro.emprestarExemplar();
        usuario.registrarEmprestimo();
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(usuario.getPrazoDevolucaoEmDias());
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public boolean estaAtrasado(LocalDate hoje) {
        return hoje.isAfter(dataPrevistaDevolucao);
    }

    public int getDiasAtraso(LocalDate data) {
        if (!estaAtrasado(data)) {
            return 0;
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(dataPrevistaDevolucao, data);
    }
}
