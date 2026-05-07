package org.QuickCheck;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import org.Exceptions.AlreadyExistsException;
import org.Exceptions.EmptyPlaylistException;
import org.Model.Music.Music;
import org.Model.Plan.PlanFree;
import org.Model.Playlist.Playlist;
import org.Model.Playlist.PlaylistCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Property-based tests using jqwik (QuickCheck style).
 */
public class QuickCheckPropertyTest {

    @Provide
    Arbitrary<String> asciiText() {
        return Arbitraries.strings()
            .withCharRange('a', 'z')
            .ofMinLength(1)
            .ofMaxLength(12);
    }

    @Provide
    Arbitrary<Music> music() {
        return Combinators.combine(
            asciiText(),
            asciiText(),
            asciiText(),
            asciiText(),
            asciiText(),
            asciiText(),
            asciiText(),
            Arbitraries.integers().between(0, 600)
        ).as((name, interpreter, publisher, lyrics, figures, genre, album, duration) ->
            new Music(
                name,
                interpreter,
                publisher,
                lyrics,
                figures,
                genre,
                album,
                duration,
                duration % 2 == 0
            )
        );
    }

    @Provide
    Arbitrary<List<Music>> uniqueMusicList() {
        return music()
            .list()
            .ofMinSize(1)
            .ofMaxSize(20)
            .uniqueElements(Music::getName);
    }

    @Provide
    Arbitrary<Integer> points() {
        return Arbitraries.integers().between(0, 10000);
    }

    @Property(tries = 100)
    void musicPlayIncrementsReproductions(@ForAll("music") Music music) {
        int before = music.getReproductions();
        String lyrics = music.getLyrics();

        String returned = music.play();

        assertEquals(before + 1, music.getReproductions());
        assertEquals(lyrics, returned);
    }

    @Property(tries = 100)
    void playlistAddRemoveKeepsSize(
        @ForAll("uniqueMusicList") List<Music> musics,
        @ForAll("music") Music extra
    ) throws AlreadyExistsException {
        Playlist playlist = new Playlist("qc", "autor", musics);

        Set<String> names = new HashSet<>();
        for (Music m : musics) {
            names.add(m.getName());
        }

        Music uniqueExtra = ensureUniqueName(extra, names);
        int before = playlist.getMusics().size();

        playlist.addMusic(uniqueExtra);
        assertEquals(before + 1, playlist.getMusics().size());

        boolean removed = playlist.removeMusic(uniqueExtra);
        assertTrue(removed);
        assertEquals(before, playlist.getMusics().size());
    }

    @Property(tries = 100)
    void randomPlaylistUsesOnlyExistingMusics(@ForAll("uniqueMusicList") List<Music> musics) {
        Map<String, Music> map = toMap(musics);

        List<Music> random = PlaylistCreator.createRandomPlaylist(map);

        assertTrue(random.size() >= 1 && random.size() <= musics.size());

        Set<String> seen = new HashSet<>();
        for (Music m : random) {
            assertTrue(map.containsKey(m.getName()));
            assertTrue(seen.add(m.getName()));
        }
    }

    @Property(tries = 100)
    void genrePlaylistRespectsConstraints(@ForAll("uniqueMusicList") List<Music> musics)
        throws AlreadyExistsException, EmptyPlaylistException {
        Map<String, Music> map = toMap(musics);
        String genre = musics.get(0).getGenre();
        int maxDuration = 0;
        for (Music m : musics) {
            if (genre.equals(m.getGenre())) {
                maxDuration += m.getDuration();
            }
        }

        List<Music> playlist = PlaylistCreator.createGenrePlaylist(
            "user",
            "qc-genre",
            genre,
            maxDuration,
            map,
            new HashMap<>()
        );

        int total = 0;
        for (Music m : playlist) {
            assertEquals(genre, m.getGenre());
            total += m.getDuration();
        }
        assertTrue(total <= maxDuration);
    }

    @Property(tries = 100)
    void planFreeAddsFive(@ForAll("points") int points) {
        PlanFree plan = new PlanFree();
        plan.setPoints(points);
        plan.addPoints();
        assertEquals(points + 5, plan.getPoints());
    }

    private Map<String, Music> toMap(List<Music> musics) {
        Map<String, Music> map = new HashMap<>();
        for (Music m : musics) {
            map.put(m.getName(), m);
        }
        return map;
    }

    private Music ensureUniqueName(Music base, Set<String> existingNames) {
        String name = base.getName();
        String candidate = name;
        int counter = 0;
        while (existingNames.contains(candidate)) {
            counter++;
            candidate = name + "_" + counter;
        }
        return new Music(
            candidate,
            base.getInterpreter(),
            base.getPublisher(),
            base.getLyrics(),
            base.getMusicalFigures(),
            base.getGenre(),
            base.getAlbum(),
            base.getDuration(),
            base.isExplicit()
        );
    }
}
