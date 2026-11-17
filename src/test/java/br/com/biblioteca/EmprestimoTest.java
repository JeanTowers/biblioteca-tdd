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
}
