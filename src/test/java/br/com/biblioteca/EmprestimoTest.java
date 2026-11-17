package br.com.biblioteca;

import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    private static final LocalDate DATA_EMPRESTIMO = LocalDate.of(2026, 3, 2);

    private Livro livro;
    private Usuario aluno;
    private Usuario professor;

    @BeforeEach
    void setUp() {
        livro = new Livro("978-85-7522-000-1", "Clean Code", 2);
        aluno = new Usuario("Ana", TipoUsuario.ALUNO);
        professor = new Usuario("Carlos", TipoUsuario.PROFESSOR);
    }

    @Test
    @DisplayName("deve prever devolucao em 7 dias para aluno e 14 para professor (RN04)")
    void deveCalcularDataPrevistaConformeTipoDeUsuario() {
        Emprestimo doAluno = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        Emprestimo doProfessor = new Emprestimo(livro, professor, DATA_EMPRESTIMO);

        assertEquals(LocalDate.of(2026, 3, 9), doAluno.getDataPrevistaDevolucao());
        assertEquals(LocalDate.of(2026, 3, 16), doProfessor.getDataPrevistaDevolucao());
    }

    @Test
    @DisplayName("deve reservar exemplar e contar emprestimo do usuario ao emprestar")
    void deveReservarExemplarEContarEmprestimoQuandoCriado() {
        new Emprestimo(livro, aluno, DATA_EMPRESTIMO);

        assertEquals(1, livro.getExemplaresDisponiveis());
        assertEquals(1, aluno.getEmprestimosAtivos());
    }

    @Test
    @DisplayName("nao deve contar emprestimo do usuario quando livro estiver indisponivel (RN01)")
    void naoDeveAlterarUsuarioQuandoLivroEstiverIndisponivel() {
        Livro esgotado = new Livro("111", "Refactoring", 1);
        esgotado.emprestarExemplar();

        assertThrows(LivroIndisponivelException.class,
                () -> new Emprestimo(esgotado, aluno, DATA_EMPRESTIMO));
        assertEquals(0, aluno.getEmprestimosAtivos());
    }

    @Test
    @DisplayName("nao deve reservar exemplar quando usuario estiver bloqueado (RN03)")
    void naoDeveAlterarLivroQuandoUsuarioEstiverBloqueado() {
        aluno.adicionarMulta(2.0);

        assertThrows(UsuarioBloqueadoException.class,
                () -> new Emprestimo(livro, aluno, DATA_EMPRESTIMO));
        assertEquals(2, livro.getExemplaresDisponiveis());
    }

    @Test
    @DisplayName("nao deve estar atrasado ate a data prevista")
    void naoDeveEstarAtrasadoQuandoHojeForADataPrevista() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        assertFalse(emprestimo.estaAtrasado(LocalDate.of(2026, 3, 9)));
    }

    @Test
    @DisplayName("deve estar atrasado depois da data prevista")
    void deveEstarAtrasadoQuandoHojeForDepoisDaDataPrevista() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        assertTrue(emprestimo.estaAtrasado(LocalDate.of(2026, 3, 10)));
    }

    @Test
    @DisplayName("deve retornar zero dias de atraso quando dentro do prazo")
    void deveRetornarZeroDiasDeAtrasoQuandoDentroDoPrazo() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        assertEquals(0, emprestimo.getDiasAtraso(LocalDate.of(2026, 3, 5)));
    }

    @Test
    @DisplayName("deve contar dias entre a data prevista e a data informada")
    void deveContarDiasDeAtrasoQuandoPassarDoPrazo() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        assertEquals(4, emprestimo.getDiasAtraso(LocalDate.of(2026, 3, 13)));
    }

    @Test
    @DisplayName("deve estender o prazo pelo mesmo numero de dias ao renovar")
    void deveEstenderPrazoQuandoRenovar() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        emprestimo.renovar(LocalDate.of(2026, 3, 8));
        assertEquals(LocalDate.of(2026, 3, 16), emprestimo.getDataPrevistaDevolucao());
    }

    @Test
    @DisplayName("nao deve permitir renovar duas vezes (RN06)")
    void naoDevePermitirRenovarQuandoJaRenovado() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        emprestimo.renovar(LocalDate.of(2026, 3, 8));

        assertThrows(RenovacaoNaoPermitidaException.class,
                () -> emprestimo.renovar(LocalDate.of(2026, 3, 10)));
        assertEquals(LocalDate.of(2026, 3, 16), emprestimo.getDataPrevistaDevolucao());
    }

    @Test
    @DisplayName("nao deve permitir renovar emprestimo atrasado (RN06)")
    void naoDevePermitirRenovarQuandoAtrasado() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);

        assertThrows(RenovacaoNaoPermitidaException.class,
                () -> emprestimo.renovar(LocalDate.of(2026, 3, 10)));
        assertEquals(LocalDate.of(2026, 3, 9), emprestimo.getDataPrevistaDevolucao());
    }

    @Test
    @DisplayName("deve liberar exemplar e vaga do usuario ao devolver no prazo")
    void deveLiberarExemplarEVagaQuandoDevolverNoPrazo() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        emprestimo.devolver(LocalDate.of(2026, 3, 9));

        assertTrue(emprestimo.isDevolvido());
        assertEquals(2, livro.getExemplaresDisponiveis());
        assertEquals(0, aluno.getEmprestimosAtivos());
        assertEquals(0.0, aluno.getMultaPendente(), 0.001);
    }

    @Test
    @DisplayName("deve gerar multa no usuario ao devolver com atraso (RN05)")
    void deveGerarMultaQuandoDevolverComAtraso() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        emprestimo.devolver(LocalDate.of(2026, 3, 12));

        assertEquals(6.0, aluno.getMultaPendente(), 0.001);
        assertFalse(aluno.podeEmprestar());
    }

    @Test
    @DisplayName("deve limitar a multa ao teto quando atraso for muito longo (RN05)")
    void deveLimitarMultaQuandoAtrasoForMuitoLongo() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        emprestimo.devolver(LocalDate.of(2026, 5, 1));

        assertEquals(50.0, aluno.getMultaPendente(), 0.001);
    }

    @Test
    @DisplayName("deve lancar excecao ao devolver emprestimo ja devolvido")
    void deveLancarExcecaoQuandoDevolverDuasVezes() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        emprestimo.devolver(LocalDate.of(2026, 3, 5));

        assertThrows(IllegalStateException.class,
                () -> emprestimo.devolver(LocalDate.of(2026, 3, 6)));
    }

    @Test
    @DisplayName("deve lancar excecao ao devolver com data anterior ao emprestimo")
    void deveLancarExcecaoQuandoDataDeDevolucaoForAnteriorAoEmprestimo() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);

        assertThrows(IllegalArgumentException.class,
                () -> emprestimo.devolver(LocalDate.of(2026, 3, 1)));
        assertFalse(emprestimo.isDevolvido());
        assertEquals(1, livro.getExemplaresDisponiveis());
    }

    @Test
    @DisplayName("nao deve estar atrasado depois de devolvido")
    void naoDeveEstarAtrasadoQuandoJaDevolvido() {
        Emprestimo emprestimo = new Emprestimo(livro, aluno, DATA_EMPRESTIMO);
        emprestimo.devolver(LocalDate.of(2026, 3, 5));

        assertFalse(emprestimo.estaAtrasado(LocalDate.of(2026, 4, 1)));
    }
}
