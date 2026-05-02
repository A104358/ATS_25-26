package org.Model;

import org.Exceptions.*;
import org.Model.Music.Music;
import org.Model.Plan.PlanPremiumBase;
import org.Model.Plan.PlanPremiumTop;
import org.Model.Playlist.Playlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SpotifUMExtendedTest {

    private SpotifUM model;

    @BeforeEach
    void setUp() {
        model = new SpotifUM();
    }

    // =================== AUXILIARES ===================

    private void criarAlbumEMusica(String album, String artista, String musica) throws AlreadyExistsException {
        model.addNewAlbum(album, artista);
        model.addNewMusic(musica, artista, "Pub", "letra da musica", "f", "Pop", album, 100, false, null);
    }

    private void autenticarFree(String nome) throws Exception {
        model.addNewUser(nome, nome + "@test.com", "rua", "pw");
        model.authenticateUser(nome, "pw");
    }

    private void autenticarPremiumBase(String nome) throws Exception {
        autenticarFree(nome);
        model.getCurrentUser().setPlan(new PlanPremiumBase());
    }

    private void autenticarPremiumTop(String nome) throws Exception {
        autenticarFree(nome);
        model.getCurrentUser().setPlan(new PlanPremiumTop());
    }

    // =================== MÚSICAS ===================

    @Test
    void testAddMusicaSemUrlExisteNoSistema() throws Exception {
        model.addNewAlbum("AlbumX", "ArtistX");
        model.addNewMusic("SongA", "ArtistX", "Pub", "letra", "f", "Rock", "AlbumX", 200, false, null);
        assertTrue(model.musicExists("SongA"));
    }

    @Test
    void testAddMusicaComUrlExisteNoSistema() throws Exception {
        model.addNewAlbum("AlbumX", "ArtistX");
        model.addNewMusic("SongURL", "ArtistX", "Pub", "letra", "f", "Rock", "AlbumX", 200, false,
                "https://youtube.com/watch?v=abc");
        assertTrue(model.musicExists("SongURL"));
    }

    @Test
    void testAddMusicaDuplicadaLancaAlreadyExistsException() throws Exception {
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        assertThrows(AlreadyExistsException.class, () ->
                model.addNewMusic("SongA", "ArtistX", "Pub", "l", "f", "Pop", "AlbumX", 100, false, null));
    }

    @Test
    void testPlayMusicDevolveLyrics() throws Exception {
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        String lyrics = model.playMusic("SongA");
        assertEquals("letra da musica", lyrics);
    }

    @Test
    void testPlayMusicIncrementaReproducoes() throws Exception {
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        model.playMusic("SongA");
        model.playMusic("SongA");
        Music m = model.getMusicByName("SongA");
        assertEquals(2, m.getReproductions());
    }

    @Test
    void testPlayMusicNaoExisteLancaNotFoundException() {
        assertThrows(NotFoundException.class, () -> model.playMusic("MusicaInexistente"));
    }

    @Test
    void testGetMusicByNameNaoExisteLancaNotFoundException() {
        assertThrows(NotFoundException.class, () -> model.getMusicByName("Inexistente"));
    }

    @Test
    void testListAllMusicsContemMusicaAdicionada() throws Exception {
        criarAlbumEMusica("AlbumX", "ArtistX", "SongEspecial");
        String lista = model.listAllMusics();
        assertTrue(lista.contains("SongEspecial"));
    }

    @Test
    void testListAllMusicsVazioDevolvStringVazia() {
        assertEquals("", model.listAllMusics());
    }

    // =================== GÉNEROS ===================

    @Test
    void testListAllGenresContemGeneroAdicionado() throws Exception {
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        // A música foi criada com género "Pop" no auxiliar
        String genres = model.listAllGenres();
        assertTrue(genres.contains("Pop"));
    }

    @Test
    void testIncrementGenreReproductionsEGetGenre() throws Exception {
        model.incrementGenreReproductions("Rock");
        model.incrementGenreReproductions("Rock");
        model.incrementGenreReproductions("Jazz");
        String top = model.getGenreWithMostReproductions();
        assertEquals("Rock", top);
    }

    @Test
    void testGetGenreWithMostReproductionsSemDadosLancaException() {
        assertThrows(NoReproductionsInDatabaseException.class,
                () -> model.getGenreWithMostReproductions());
    }

    // =================== ESTATÍSTICAS ===================

    @Test
    void testMostReproducedMusicSemMusicasLancaException() {
        assertThrows(NoMusicsInDatabaseException.class, () -> model.mostReproducedMusic());
    }

    @Test
    void testMostReproducedMusicIdentificaCorrectamente() throws Exception {
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        model.addNewAlbum("AlbumY", "ArtistY");
        model.addNewMusic("SongB", "ArtistY", "Pub", "l", "f", "Pop", "AlbumY", 100, false, null);
        model.playMusic("SongA");
        model.playMusic("SongA");
        model.playMusic("SongB");
        Music top = model.mostReproducedMusic();
        assertEquals("SongA", top.getName());
    }

    @Test
    void testGetTopArtistNameSemArtistasLancaException() {
        assertThrows(NoArtistsInDatabaseException.class, () -> model.getTopArtistName());
    }

    @Test
    void testGetTopArtistNameIdentificaCorrectamente() {
        model.incrementArtistReproductions("Ed Sheeran");
        model.incrementArtistReproductions("Ed Sheeran");
        model.incrementArtistReproductions("Drake");
        assertDoesNotThrow(() -> {
            String result = model.getTopArtistName();
            assertTrue(result.startsWith("Ed Sheeran"));
        });
    }

    @Test
    void testGetUserWithMostPointsSemUtilizadoresLancaException() {
        assertThrows(NoUsersInDatabaseException.class, () -> model.getUserWithMostPoints());
    }

    @Test
    void testGetUserWithMostPlaylistsSemUtilizadoresLancaException() {
        assertThrows(NoUsersInDatabaseException.class, () -> model.getUserWithMostPlaylists());
    }

    @Test
    void testGetUserWithMostReproductionsSemUtilizadoresLancaException() {
        assertThrows(NoUsersInDatabaseException.class, () -> model.getUserWithMostReproductions());
    }

    // =================== PERMISSÕES POR PLANO ===================

    @Test
    void testUtilizadorFreeNaoTemAcessoALibrary() throws Exception {
        autenticarFree("alice");
        assertFalse(model.hasLibrary());
    }

    @Test
    void testUtilizadorFreeNaoPodeSkip() throws Exception {
        autenticarFree("alice");
        assertFalse(model.canCurrentUserSkip());
    }

    @Test
    void testUtilizadorFreeNaoPodeEscolherMusica() throws Exception {
        autenticarFree("alice");
        assertFalse(model.canCurrentUserChooseWhatToPlay());
    }

    @Test
    void testUtilizadorFreeNaoTemAcessoAFavoritos() throws Exception {
        autenticarFree("alice");
        assertFalse(model.currentUserAccessToFavorites());
    }

    @Test
    void testUtilizadorPremiumBaseTemLibrary() throws Exception {
        autenticarPremiumBase("alice");
        assertTrue(model.hasLibrary());
    }

    @Test
    void testUtilizadorPremiumBasePodeSkip() throws Exception {
        autenticarPremiumBase("alice");
        assertTrue(model.canCurrentUserSkip());
    }

    @Test
    void testUtilizadorPremiumBaseNaoTemFavoritos() throws Exception {
        autenticarPremiumBase("alice");
        assertFalse(model.currentUserAccessToFavorites());
    }

    @Test
    void testUtilizadorPremiumTopTemFavoritos() throws Exception {
        autenticarPremiumTop("alice");
        assertTrue(model.currentUserAccessToFavorites());
    }

    // =================== PONTOS ===================

    @Test
    void testAddPointsFreeIncrementa5() throws Exception {
        autenticarFree("alice");
        model.addPointsToCurrentUser();
        assertEquals(5, model.getCurrentUser().getPlan().getPoints());
    }

    @Test
    void testAddPointsPremiumBaseIncrementa10() throws Exception {
        autenticarPremiumBase("alice");
        model.addPointsToCurrentUser();
        assertEquals(10, model.getCurrentUser().getPlan().getPoints());
    }

    // =================== PLAYLISTS ===================

    @Test
    void testAddToCurrentUserPlaylistEListar() throws Exception {
        autenticarPremiumBase("alice");
        model.addToCurrentUserPlaylist("MinhaPlaylist");
        String lista = model.listCurrentUserPlaylists();
        assertTrue(lista.contains("MinhaPlaylist"));
    }

    @Test
    void testSetPlaylistAsPublicSucesso() throws Exception {
        autenticarPremiumBase("alice");
        model.addToCurrentUserPlaylist("MinhaPlaylist");
        Playlist p = model.getCurrentUser().getPlaylists().get(0);
        assertDoesNotThrow(() -> model.setPlaylistAsPublic(p.getId()));
        assertEquals(1, model.getPublicPlaylistSize());
    }

    @Test
    void testSetPlaylistAsPublicJaPublicaLancaAlreadyExistsException() throws Exception {
        autenticarPremiumBase("alice");
        model.addToCurrentUserPlaylist("MinhaPlaylist");
        Playlist p = model.getCurrentUser().getPlaylists().get(0);
        model.setPlaylistAsPublic(p.getId());
        assertThrows(AlreadyExistsException.class, () -> model.setPlaylistAsPublic(p.getId()));
    }

    @Test
    void testAddMusicToCurrentUserPlaylist() throws Exception {
        autenticarPremiumBase("alice");
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        model.addToCurrentUserPlaylist("MinhaPlaylist");
        Playlist p = model.getCurrentUser().getPlaylists().get(0);
        assertDoesNotThrow(() -> model.addMusicToCurrentUserPlaylist(p.getId(), "SongA"));
    }

    @Test
    void testAddMusicToCurrentUserPlaylistMusicaInexistenteLancaNotFoundException() throws Exception {
        autenticarPremiumBase("alice");
        model.addToCurrentUserPlaylist("MinhaPlaylist");
        Playlist p = model.getCurrentUser().getPlaylists().get(0);
        assertThrows(NotFoundException.class,
                () -> model.addMusicToCurrentUserPlaylist(p.getId(), "MusicaQueNaoExiste"));
    }

    @Test
    void testListPublicPlaylistsContemNomeDaPlaylist() throws Exception {
        model.addPlaylist("PlaylistPublica", "admin");
        String lista = model.listPublicPlaylists();
        assertTrue(lista.contains("PlaylistPublica"));
    }

    // =================== REPRODUÇÕES DO UTILIZADOR ===================

    @Test
    void testAddToCurrentUserMusicReproductions() throws Exception {
        autenticarFree("alice");
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        model.addToCurrentUserMusicReproductions("SongA");
        assertEquals(1, model.getCurrentUser().getMusicReproductions().size());
    }

    @Test
    void testGetUserWithMostReproductionsComDados() throws Exception {
        autenticarFree("alice");
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        model.addToCurrentUserMusicReproductions("SongA");
        model.addToCurrentUserMusicReproductions("SongA");
        assertDoesNotThrow(() -> {
            var user = model.getUserWithMostReproductions();
            assertEquals("alice", user.getUsername());
        });
    }

    // =================== BUG DOCUMENTADO — DATAS BOUNDARY ===================

    @Test
    void testGetUserWithMostReproductionsDatasBoundaryExclusivas() throws Exception {
        // DEF-002: getMusicReproductionsCount usa isAfter/isBefore exclusivos.
        // Uma reprodução feita exactamente na startDate ou endDate não é contada.
        autenticarFree("alice");
        criarAlbumEMusica("AlbumX", "ArtistX", "SongA");
        model.addToCurrentUserMusicReproductions("SongA"); // reprodução feita hoje
        LocalDate hoje = LocalDate.now();
        int count = model.getCurrentUser().getMusicReproductionsCount(hoje, hoje);
        // Comportamento real (bugado): 0 — a data de hoje não é nem isAfter(hoje) nem isBefore(hoje)
        assertEquals(0, count, "DEF-002 confirmado: reproduções na boundary date não são contadas");
    }

    // =================== ALTERAR DADOS DO UTILIZADOR ACTUAL ===================

    @Test
    void testIsPasswordCorrect() throws Exception {
        autenticarFree("alice");
        assertTrue(model.isPasswordCorrect("pw"));
        assertFalse(model.isPasswordCorrect("errada"));
    }

    @Test
    void testSetCurrentUserEmail() throws Exception {
        autenticarFree("alice");
        model.setCurrentUserEmail("novo@email.com");
        assertEquals("novo@email.com", model.getCurrentUser().getEmail());
    }

    @Test
    void testSetCurrentUserPassword() throws Exception {
        autenticarFree("alice");
        model.setCurrentUserPassword("novapassword");
        assertTrue(model.isPasswordCorrect("novapassword"));
    }

    @Test
    void testChangeCurrentUserNameSucesso() throws Exception {
        autenticarFree("alice");
        model.changeCurrentUserName("alice_nova");
        assertEquals("alice_nova", model.getCurrentUser().getUsername());
        assertTrue(model.userExists("alice_nova"));
        assertFalse(model.userExists("alice"));
    }

    @Test
    void testChangeCurrentUserNameIgualLancaException() throws Exception {
        autenticarFree("alice");
        assertThrows(IllegalArgumentException.class, () -> model.changeCurrentUserName("alice"));
    }

    @Test
    void testChangeCurrentUserNameJaExisteLancaException() throws Exception {
        model.addNewUser("bob", "b@b.com", "rua", "pw");
        autenticarFree("alice");
        assertThrows(IllegalArgumentException.class, () -> model.changeCurrentUserName("bob"));
    }

    @Test
    void testGetCurrentUserPlanNameFree() throws Exception {
        autenticarFree("alice");
        assertEquals("Free", model.getCurrentUserPlanName());
    }

    @Test
    void testGetCurrentUserPlanNamePremiumBase() throws Exception {
        autenticarPremiumBase("alice");
        assertEquals("PremiumBase", model.getCurrentUserPlanName());
    }
}