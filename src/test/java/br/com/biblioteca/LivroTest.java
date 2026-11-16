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
}
