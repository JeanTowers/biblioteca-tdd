package br.com.biblioteca;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    private final Usuario usuario;
    private LocalDate dataPrevistaDevolucao;
    private boolean renovado;

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo) {
        if (!usuario.podeEmprestar()) {
            throw new UsuarioBloqueadoException("Usuario nao pode fazer novo emprestimo");
        }
        livro.emprestarExemplar();
        usuario.registrarEmprestimo();
        this.usuario = usuario;
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
        return (int) ChronoUnit.DAYS.between(dataPrevistaDevolucao, data);
    }

    public void renovar(LocalDate hoje) {
        if (renovado) {
            throw new RenovacaoNaoPermitidaException("Emprestimo ja foi renovado");
        }
        if (estaAtrasado(hoje)) {
            throw new RenovacaoNaoPermitidaException("Emprestimo atrasado nao pode ser renovado");
        }
        renovado = true;
        dataPrevistaDevolucao = dataPrevistaDevolucao.plusDays(usuario.getPrazoDevolucaoEmDias());
    }
}
