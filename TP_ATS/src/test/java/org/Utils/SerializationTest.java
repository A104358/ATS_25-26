package org.Utils;

import org.Model.SpotifUM;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SerializationTest {

    @Test
    void testExportAndImportRoundTrip() throws Exception {
        SpotifUM original = new SpotifUM();
        original.addNewUser("alice", "a@b.com", "rua", "pw");

        File temp = File.createTempFile("spotifum_test", ".ser");
        temp.deleteOnExit();

        Serialization.exportar(original, temp.getAbsolutePath());
        SpotifUM loaded = Serialization.importar(temp.getAbsolutePath());

        assertEquals(original, loaded);
    }

    @Test
    void testExportNullThrows() {
        String tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "test.ser").toString();

        assertThrows(IllegalArgumentException.class,
                () -> Serialization.exportar(null, tmpPath));
    }

    @Test
    void testImportNonExistentFileThrows() throws IOException {
        Path temp = Files.createTempFile("spotifum_missing", ".ser");
        Files.deleteIfExists(temp);

        assertThrows(RuntimeException.class,
                () -> Serialization.importar(temp.toString()));
    }

    @Test
    void testExportInvalidPathThrows() throws IOException {
        SpotifUM s = new SpotifUM();
        Path tempDir = Files.createTempDirectory("spotifum_invalid");
        Path invalidPath = tempDir.resolve("missing-dir").resolve("test.ser");

        assertThrows(RuntimeException.class,
                () -> Serialization.exportar(s, invalidPath.toString()));
    }
}
