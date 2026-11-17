package br.com.biblioteca;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    private final Livro livro;
    private final Usuario usuario;
    private final LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private boolean renovado;
    private boolean devolvido;

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo) {
        if (!usuario.podeEmprestar()) {
            throw new UsuarioBloqueadoException("Usuario nao pode fazer novo emprestimo");
        }
        livro.emprestarExemplar();
        usuario.registrarEmprestimo();
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(usuario.getPrazoDevolucaoEmDias());
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public boolean estaAtrasado(LocalDate hoje) {
        return !devolvido && hoje.isAfter(dataPrevistaDevolucao);
    }

    public int getDiasAtraso(LocalDate data) {
        if (!data.isAfter(dataPrevistaDevolucao)) {
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

    public void devolver(LocalDate dataDevolucao) {
        if (devolvido) {
            throw new IllegalStateException("Emprestimo ja foi devolvido");
        }
        if (dataDevolucao.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("Data de devolucao anterior ao emprestimo");
        }
        devolvido = true;
        livro.devolverExemplar();
        usuario.registrarDevolucao();
        double multa = new CalculadoraMulta().calcular(getDiasAtraso(dataDevolucao));
        if (multa > 0) {
            usuario.adicionarMulta(multa);
        }
    }

    public boolean isDevolvido() {
        return devolvido;
    }
}
