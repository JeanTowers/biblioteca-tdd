package br.com.biblioteca;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraMultaTest {

    private CalculadoraMulta calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraMulta();
    }

    @Test
    @DisplayName("deve retornar multa zero quando nao ha dias de atraso")
    void deveRetornarZeroQuandoNaoHaAtraso() {
        assertEquals(0.0, calculadora.calcular(0), 0.001);
    }

    @Test
    @DisplayName("deve cobrar dois reais por dia de atraso")
    void deveCobrarDoisReaisPorDiaQuandoHaAtraso() {
        assertEquals(10.0, calculadora.calcular(5), 0.001);
    }
}
