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
}
