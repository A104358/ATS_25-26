package org.Utils;

import org.junit.jupiter.api.Test;
import javax.sound.sampled.Clip;
import static org.junit.jupiter.api.Assertions.*;

class MusicPlayerTest {

    @Test
    void testPlayMusicFicheiroInexistenteDevolvNull() {
        MusicPlayer player = new MusicPlayer();
        Clip result = player.playMusic("musica_que_definitivamente_nao_existe_xyz");
        assertNull(result, "Deve devolver null quando o ficheiro WAV não existe nos recursos");
    }

    @Test
    void testPlayMusicNomeComEspacosDevolvNull() {
        // O método normaliza o nome (substitui espaços por _) mas o ficheiro não existe
        MusicPlayer player = new MusicPlayer();
        Clip result = player.playMusic("Nome Com Espacos Que Nao Existe");
        assertNull(result);
    }

    @Test
    void testPlayMusicNomeVazioDevolvNull() {
        MusicPlayer player = new MusicPlayer();
        Clip result = player.playMusic("");
        assertNull(result);
    }
}