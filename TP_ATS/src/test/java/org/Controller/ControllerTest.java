package org.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    private Controller controller;

    @BeforeEach
    void setUp() {
        controller = new Controller();
    }

    // ===================== CONSTRUTORES =====================

    @Test
    void testDefaultConstructor() {
        assertNotNull(controller);
        assertNotNull(controller.getSpotifUM());
    }

    @Test
    void testCopyConstructor() {
        Controller copy = new Controller(controller);
        assertNotNull(copy);
        assertNotNull(copy.getSpotifUM());
    }

    // ===================== UTILIZADORES =====================

    @Test
    void testAddNewUserSuccess() {
        String result = controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        assertTrue(result.contains("sucesso"));
    }

    @Test
    void testAddNewUserDuplicate() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        String result = controller.addNewUser("alice", "alice2@email.com", "Rua B", "pass456");
        assertTrue(result.contains("Já existe"));
    }

    @Test
    void testLoginSuccess() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        boolean result = controller.loginWithMessage("alice", "pass123");
        assertTrue(result);
    }

    @Test
    void testLoginWrongPassword() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        boolean result = controller.loginWithMessage("alice", "wrongpass");
        assertFalse(result);
    }

    @Test
    void testLoginNonExistentUser() {
        boolean result = controller.loginWithMessage("naoexiste", "pass123");
        assertFalse(result);
    }

    // ===================== PLANOS =====================

    @Test
    void testSetFreePlan() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        String result = controller.setFreePlan();
        assertNotNull(result);
    }

    @Test
    void testSetPremiumBasePlan() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        String result = controller.setPremiumBasePlan();
        assertNotNull(result);
    }

    @Test
    void testSetPremiumTopPlan() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        String result = controller.setPremiumTopPlan();
        assertNotNull(result);
    }

    @Test
    void testGetCurrentUserPlan() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        String plan = controller.getCurrentUserPlan();
        assertNotNull(plan);
    }

    // ===================== MÚSICAS =====================

    @Test
    void testAddMusicSuccess() {
        controller.createAlbum("Album1", "Artist1");
        String result = controller.addMusic(
                "Song1", "Artist1", "Publisher1",
                "lyrics", "figures", "pop", "Album1",
                200, false, ""
        );
        assertTrue(result.contains("sucesso"));
    }

    @Test
    void testAddMusicDuplicate() {
        controller.createAlbum("Album1", "Artist1");
        controller.addMusic("Song1", "Artist1", "Publisher1", "lyrics", "figures", "pop", "Album1", 200, false, "");
        String result = controller.addMusic("Song1", "Artist1", "Publisher1", "lyrics", "figures", "pop", "Album1", 200, false, "");
        assertTrue(result.contains("❌") || result.contains("erro") || result.contains("Erro") || result.contains("já existe") || result.contains("Já existe"));
    }

    @Test
    void testMusicExists() {
        controller.createAlbum("Album1", "Artist1");
        controller.addMusic("Song1", "Artist1", "Publisher1", "lyrics", "figures", "pop", "Album1", 200, false, "");
        assertTrue(controller.musicExists("Song1"));
    }

    @Test
    void testMusicNotExists() {
        assertFalse(controller.musicExists("NaoExiste"));
    }

    @Test
    void testListAllMusicsEmpty() {
        String result = controller.listAllMusics();
        assertNotNull(result);
    }

    @Test
    void testListAllMusicsWithMusic() {
        controller.createAlbum("Album1", "Artist1");
        controller.addMusic("Song1", "Artist1", "Publisher1", "lyrics", "figures", "pop", "Album1", 200, false, "");
        String result = controller.listAllMusics();
        assertTrue(result.contains("Song1"));
    }

    // ===================== ÁLBUNS =====================

    @Test
    void testCreateAlbumSuccess() {
        String result = controller.createAlbum("Album1", "Artist1");
        assertNotNull(result);
    }

    @Test
    void testAlbumExists() {
        controller.createAlbum("Album1", "Artist1");
        assertTrue(controller.albumExists("Album1"));
    }

    @Test
    void testAlbumNotExists() {
        assertFalse(controller.albumExists("NaoExiste"));
    }

    @Test
    void testListAllAlbums() {
        controller.createAlbum("Album1", "Artist1");
        String result = controller.listAllAlbums();
        assertTrue(result.contains("Album1"));
    }

    // ===================== PERFIL DO UTILIZADOR =====================

    @Test
    void testChangeCurrentUserEmail() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        String result = controller.changeCurrentUserEmail("novo@email.com");
        assertNotNull(result);
    }

    @Test
    void testChangeCurrentUserPassword() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        String result = controller.changeCurrentUserPassword("novapass");
        assertNotNull(result);
    }

    @Test
    void testIsPasswordCorrect() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        assertTrue(controller.isPasswordCorrect("pass123"));
        assertFalse(controller.isPasswordCorrect("errada"));
    }

    @Test
    void testGetCurrentUser() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        String result = controller.getCurrentUser();
        assertNotNull(result);
    }

    // ===================== ESTATÍSTICAS =====================

    @Test
    void testGetMostReproducedMusic() {
        String result = controller.getMostReproducedMusic();
        assertNotNull(result);
    }

    @Test
    void testGetMostReproducedArtist() {
        String result = controller.getMostReproducedArtist();
        assertNotNull(result);
    }

    @Test
    void testGetUserWithMostPoints() {
        String result = controller.getUserWithMostPoints();
        assertNotNull(result);
    }

    @Test
    void testGetMostReproducedGenre() {
        String result = controller.getMostReproducedGenre();
        assertNotNull(result);
    }

    @Test
    void testGetUserWithMostPlaylists() {
        String result = controller.getUserWithMostPlaylists();
        assertNotNull(result);
    }

    // ===================== PLAYLISTS =====================

    @Test
    void testListPublicPlaylists() {
        String result = controller.listPublicPlaylists();
        assertNotNull(result);
    }

    @Test
    void testListUserPlaylistsWithoutLogin() {
        String result = controller.listUserPlaylists();
        assertNotNull(result);
    }

    @Test
    void testCanCurrentUserSkip() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        // utilizador free não pode skip
        assertFalse(controller.canCurrentUserSkip());
    }

    @Test
    void testCanCurrentUserChooseWhatToPlay() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        assertFalse(controller.canCurrentUserChooseWhatToPlay());
    }

    @Test
    void testCurrentUserAccessToFavorites() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        assertFalse(controller.currentUserAccessToFavorites());
    }

    @Test
    void testPremiumUserCanSkip() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        controller.setPremiumBasePlan();
        assertTrue(controller.canCurrentUserSkip());
    }

    @Test
    void testPremiumTopUserAccessToFavorites() {
        controller.addNewUser("alice", "alice@email.com", "Rua A", "pass123");
        controller.loginWithMessage("alice", "pass123");
        controller.setPremiumTopPlan();
        assertTrue(controller.currentUserAccessToFavorites());
    }

    // ===================== GÉNEROS =====================

    @Test
    void testListAllGenres() {
        String result = controller.listAllGenres();
        assertNotNull(result);
    }
}