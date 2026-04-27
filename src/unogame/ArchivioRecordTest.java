package unogame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ArchivioRecordTest {

    private final Path fileRecord = Paths.get("record.txt");
    private boolean fileEsisteva;
    private byte[] contenutoOriginale;

    @BeforeEach
    void preparaFileRecord() throws IOException {
        fileEsisteva = Files.exists(fileRecord);
        contenutoOriginale = fileEsisteva ? Files.readAllBytes(fileRecord) : null;
        Files.deleteIfExists(fileRecord);
    }

    @AfterEach
    void ripristinaFileRecord() throws IOException {
        Files.deleteIfExists(fileRecord);
        if (fileEsisteva) {
            Files.write(fileRecord, contenutoOriginale);
        }
    }

    @Test
    void registraVittoriaAggiornaIlFile() throws IOException {
        ArchivioRecord archivio = new ArchivioRecord();

        archivio.registraVittoria("Mario");
        archivio.registraVittoria("Mario");

        String contenuto = Files.readString(fileRecord, StandardCharsets.UTF_8);
        assertTrue(contenuto.contains("Mario;2"));
    }

    @Test
    void nonRegistraNicknameVuotiONulli() {
        ArchivioRecord archivio = new ArchivioRecord();

        archivio.registraVittoria(null);
        archivio.registraVittoria("   ");

        assertFalse(Files.exists(fileRecord));
    }

    @Test
    void stampaRecordMostraLaClassifica() {
        ArchivioRecord archivio = new ArchivioRecord();
        archivio.registraVittoria("Luca");
        archivio.registraVittoria("Anna");
        archivio.registraVittoria("Anna");

        PrintStream originale = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            archivio.stampaRecord();
        } finally {
            System.setOut(originale);
        }

        String output = buffer.toString();
        assertTrue(output.contains("Anna - 2 vittorie"));
        assertTrue(output.contains("Luca - 1 vittoria"));
    }
}
