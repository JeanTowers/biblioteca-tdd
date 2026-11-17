package br.com.biblioteca;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario aluno;
    private Usuario professor;

    @BeforeEach
    void setUp() {
        aluno = new Usuario("Ana", TipoUsuario.ALUNO);
        professor = new Usuario("Carlos", TipoUsuario.PROFESSOR);
    }

    @Test
    @DisplayName("aluno deve ter limite de 3 emprestimos e professor de 5 (RN02)")
    void deveTerLimiteDeEmprestimosConformeTipo() {
        assertEquals(3, aluno.getLimiteEmprestimos());
        assertEquals(5, professor.getLimiteEmprestimos());
    }

    @Test
    @DisplayName("aluno deve ter prazo de 7 dias e professor de 14 dias (RN04)")
    void deveTerPrazoDeDevolucaoConformeTipo() {
        assertEquals(7, aluno.getPrazoDevolucaoEmDias());
        assertEquals(14, professor.getPrazoDevolucaoEmDias());
    }

    @Test
    @DisplayName("deve poder emprestar quando nao tem emprestimos nem multa")
    void devePoderEmprestarQuandoNaoTemEmprestimosNemMulta() {
        assertTrue(aluno.podeEmprestar());
    }

    @Test
    @DisplayName("deve contar emprestimo ativo ao registrar emprestimo")
    void deveContarEmprestimoAtivoQuandoRegistrarEmprestimo() {
        aluno.registrarEmprestimo();
        assertEquals(1, aluno.getEmprestimosAtivos());
    }

    @Test
    @DisplayName("nao deve poder emprestar quando aluno atingir 3 emprestimos (RN02)")
    void naoDevePoderEmprestarQuandoAlunoAtingirLimite() {
        for (int i = 0; i < 3; i++) {
            aluno.registrarEmprestimo();
        }
        assertFalse(aluno.podeEmprestar());
    }

    @Test
    @DisplayName("professor deve poder emprestar ate 5 livros (RN02)")
    void devePermitirCincoEmprestimosQuandoForProfessor() {
        for (int i = 0; i < 4; i++) {
            professor.registrarEmprestimo();
        }
        assertTrue(professor.podeEmprestar());
        professor.registrarEmprestimo();
        assertFalse(professor.podeEmprestar());
    }

    @Test
    @DisplayName("deve lancar excecao ao registrar emprestimo acima do limite")
    void deveLancarExcecaoQuandoRegistrarEmprestimoAcimaDoLimite() {
        for (int i = 0; i < 3; i++) {
            aluno.registrarEmprestimo();
        }
        assertThrows(UsuarioBloqueadoException.class, () -> aluno.registrarEmprestimo());
        assertEquals(3, aluno.getEmprestimosAtivos());
    }

    @Test
    @DisplayName("deve liberar vaga ao registrar devolucao")
    void deveLiberarVagaQuandoRegistrarDevolucao() {
        for (int i = 0; i < 3; i++) {
            aluno.registrarEmprestimo();
        }
        aluno.registrarDevolucao();
        assertEquals(2, aluno.getEmprestimosAtivos());
        assertTrue(aluno.podeEmprestar());
    }

    @Test
    @DisplayName("deve lancar excecao ao registrar devolucao sem emprestimo ativo")
    void deveLancarExcecaoQuandoRegistrarDevolucaoSemEmprestimoAtivo() {
        assertThrows(IllegalStateException.class, () -> aluno.registrarDevolucao());
    }

    @Test
    @DisplayName("deve acumular multa pendente")
    void deveAcumularMultaPendenteQuandoAdicionarMulta() {
        aluno.adicionarMulta(4.0);
        aluno.adicionarMulta(6.0);
        assertEquals(10.0, aluno.getMultaPendente(), 0.001);
    }

    @Test
    @DisplayName("deve lancar excecao ao adicionar multa nao positiva")
    void deveLancarExcecaoQuandoAdicionarMultaNaoPositiva() {
        assertThrows(IllegalArgumentException.class, () -> aluno.adicionarMulta(0));
        assertThrows(IllegalArgumentException.class, () -> aluno.adicionarMulta(-5));
    }

    @Test
    @DisplayName("nao deve poder emprestar quando tiver multa pendente (RN03)")
    void naoDevePoderEmprestarQuandoTiverMultaPendente() {
        aluno.adicionarMulta(2.0);
        assertFalse(aluno.podeEmprestar());
        assertThrows(UsuarioBloqueadoException.class, () -> aluno.registrarEmprestimo());
    }
}
