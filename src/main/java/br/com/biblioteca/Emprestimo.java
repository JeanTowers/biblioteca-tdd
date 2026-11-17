package br.com.biblioteca;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Retirada de um livro por um usuario: prazos (RN04), atraso, renovacao (RN06)
 * e devolucao com multa (RN05).
 */
public class Emprestimo {

    private final Livro livro;
    private final Usuario usuario;
    private final LocalDate dataEmprestimo;
    private final CalculadoraMulta calculadoraMulta;
    private LocalDate dataPrevistaDevolucao;
    private boolean renovado;
    private boolean devolvido;

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo) {
        this(livro, usuario, dataEmprestimo, new CalculadoraMulta());
    }

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo,
                      CalculadoraMulta calculadoraMulta) {
        // Valida o usuario antes de mexer no livro para nao deixar estado pela metade.
        if (!usuario.podeEmprestar()) {
            throw new UsuarioBloqueadoException("Usuario nao pode fazer novo emprestimo");
        }
        livro.emprestarExemplar();
        usuario.registrarEmprestimo();

        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.calculadoraMulta = calculadoraMulta;
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(prazo());
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
        dataPrevistaDevolucao = dataPrevistaDevolucao.plusDays(prazo());
    }

    public void devolver(LocalDate dataDevolucao) {
        if (devolvido) {
            throw new IllegalStateException("Emprestimo ja foi devolvido");
        }
        if (dataDevolucao.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("Data de devolucao anterior ao emprestimo");
        }
        double multa = calculadoraMulta.calcular(getDiasAtraso(dataDevolucao));

        devolvido = true;
        livro.devolverExemplar();
        usuario.registrarDevolucao();
        if (multa > 0) {
            usuario.adicionarMulta(multa);
        }
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    private int prazo() {
        return usuario.getPrazoDevolucaoEmDias();
    }
}
