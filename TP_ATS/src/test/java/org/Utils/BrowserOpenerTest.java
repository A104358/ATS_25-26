package org.Utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrowserOpenerTest {

    @Test
    void testAbrirUrlInvalidaLancaExcecao() {
        // URI com espaços é sintaticamente inválido
        BrowserOpener opener = new BrowserOpener("url invalida com espacos");
        assertThrows(Exception.class, opener::abrir);
    }

    @Test
    void testConstrutorGuardaUrl() throws Exception {
        // Testa que o objecto é criado sem erro — o URL é validado só no abrir()
        assertDoesNotThrow(() -> new BrowserOpener("https://www.example.com"));
    }

    @Test
    void testAbrirEmAmbienteHeadless() {
        // Em ambiente sem GUI (CI, terminal), Desktop.isDesktopSupported() == false
        // O método deve lançar UnsupportedOperationException ou abrir com sucesso
        // Este teste documenta o comportamento real e garante que não há crash inesperado
        BrowserOpener opener = new BrowserOpener("https://www.example.com");
        try {
            opener.abrir();
            // Ambiente com GUI — passou sem erro, comportamento correcto
        } catch (UnsupportedOperationException e) {
            // Ambiente headless — comportamento esperado e documentado
            assertEquals("Desktop não suportado.", e.getMessage());
        } catch (Exception e) {
            // Qualquer outra excepção é inesperada
            fail("Excepção inesperada em ambiente headless: " + e.getClass().getSimpleName() + " — " + e.getMessage());
        }
    }
}