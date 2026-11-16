package br.com.biblioteca;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class LivroTest {

    private Livro livro;

    @BeforeEach
    void setUp() {
        livro = new Livro("978-85-7522-000-1", "Clean Code", 2);
    }

    @Test
    @DisplayName("deve iniciar com todos os exemplares disponiveis")
    void deveIniciarComTodosOsExemplaresDisponiveis() {
        assertEquals(2, livro.getExemplaresDisponiveis());
    }

    @Test
    @DisplayName("deve estar disponivel quando houver exemplar")
    void deveEstarDisponivelQuandoHouverExemplar() {
        assertTrue(livro.estaDisponivel());
    }

    @Test
    @DisplayName("deve diminuir exemplares disponiveis ao emprestar")
    void deveDiminuirExemplaresQuandoEmprestar() {
        livro.emprestarExemplar();
        assertEquals(1, livro.getExemplaresDisponiveis());
    }

    @Test
    @DisplayName("nao deve estar disponivel quando todos os exemplares forem emprestados")
    void naoDeveEstarDisponivelQuandoTodosExemplaresEmprestados() {
        livro.emprestarExemplar();
        livro.emprestarExemplar();
        assertFalse(livro.estaDisponivel());
    }

    @Test
    @DisplayName("deve lancar excecao ao emprestar livro sem exemplar disponivel (RN01)")
    void deveLancarExcecaoQuandoEmprestarSemExemplar() {
        livro.emprestarExemplar();
        livro.emprestarExemplar();
        assertThrows(LivroIndisponivelException.class, () -> livro.emprestarExemplar());
        assertEquals(0, livro.getExemplaresDisponiveis());
    }
}
