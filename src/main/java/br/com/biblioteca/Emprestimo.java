package br.com.biblioteca;

import java.time.LocalDate;

public class Emprestimo {

    private final LocalDate dataPrevistaDevolucao;

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo) {
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(usuario.getPrazoDevolucaoEmDias());
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }
}
