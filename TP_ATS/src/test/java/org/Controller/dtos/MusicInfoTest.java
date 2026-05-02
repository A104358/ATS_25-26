package org.Controller.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MusicInfoTest {

    @Test
    void testConstrutorPrincipalPreencheNome() {
        MusicInfo info = new MusicInfo("Shape of You", "Ed Sheeran",
                "The club is where I go", "https://youtube.com/x", false);
        assertEquals("Shape of You", info.getMusicName());
    }

    @Test
    void testConstrutorPrincipalPreencheArtista() {
        MusicInfo info = new MusicInfo("Shape of You", "Ed Sheeran",
                "The club is where I go", "https://youtube.com/x", false);
        assertEquals("Ed Sheeran", info.getArtistName());
    }

    @Test
    void testConstrutorPrincipalPreencheUrl() {
        MusicInfo info = new MusicInfo("Song", "Artist",
                "lyrics here", "https://youtube.com/x", false);
        assertEquals("https://youtube.com/x", info.getUrl());
    }

    @Test
    void testConstrutorPrincipalErroMensagemVazia() {
        MusicInfo info = new MusicInfo("Song", "Artist", "lyrics", "", false);
        assertEquals("", info.getErrorMessage());
    }

    @Test
    void testConstrutorPrincipalLetraPartidaEmPalavras() {
        // A letra "The club is" deve ser dividida em 3 palavras
        MusicInfo info = new MusicInfo("Song", "Artist", "The club is", "", false);
        assertNotNull(info.getLyrics());
        assertEquals(3, info.getLyrics().length);
    }

    @Test
    void testConstrutorPrincipalMusicaExplicita() {
        MusicInfo info = new MusicInfo("Song", "Artist", "lyrics", "", true);
        assertTrue(info.isExplicit());
    }

    @Test
    void testConstrutorPrincipalMusicaNaoExplicita() {
        MusicInfo info = new MusicInfo("Song", "Artist", "lyrics", "", false);
        assertFalse(info.isExplicit());
    }

    @Test
    void testConstrutorErroPreencheMensagem() {
        MusicInfo info = new MusicInfo("Música não encontrada");
        assertEquals("Música não encontrada", info.getErrorMessage());
    }

    @Test
    void testConstrutorErroNomeVazio() {
        MusicInfo info = new MusicInfo("erro qualquer");
        assertEquals("", info.getMusicName());
    }

    @Test
    void testConstrutorErroArtistaVazio() {
        MusicInfo info = new MusicInfo("erro qualquer");
        assertEquals("", info.getArtistName());
    }

    @Test
    void testConstrutorErroUrlVazia() {
        MusicInfo info = new MusicInfo("erro qualquer");
        assertEquals("", info.getUrl());
    }

    @Test
    void testConstrutorErroLyricsNull() {
        MusicInfo info = new MusicInfo("erro qualquer");
        assertNull(info.getLyrics());
    }

    @Test
    void testConstrutorErroNaoExplicita() {
        MusicInfo info = new MusicInfo("erro qualquer");
        assertFalse(info.isExplicit());
    }

    @Test
    void testSetErrorMessage() {
        MusicInfo info = new MusicInfo("Song", "Artist", "lyrics here", "", false);
        info.setErrorMessage("novo erro");
        assertEquals("novo erro", info.getErrorMessage());
    }
}