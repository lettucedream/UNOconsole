package unogame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchivioRecord {

    private static final Path FILE_RECORD = Paths.get("record.txt");
    private final Map<String, Integer> vittorie;

    public ArchivioRecord() {
        this.vittorie = new HashMap<>();
        carica();
    }

    public void registraVittoria(String nickname) {
        if (nickname == null) {
            return;
        }

        String nomePulito = nickname.trim();
        if (nomePulito.isEmpty()) {
            return;
        }

        vittorie.put(nomePulito, vittorie.getOrDefault(nomePulito, 0) + 1);
        salva();
    }

    public void stampaRecord() {
        System.out.println("\n--- RECORD STORICI ---");

        if (vittorie.isEmpty()) {
            System.out.println("Nessun record salvato.");
            return;
        }

        List<Map.Entry<String, Integer>> classifica = new ArrayList<>(vittorie.entrySet());
        classifica.sort((e1, e2) -> {
            int confrontoVittorie = Integer.compare(e2.getValue(), e1.getValue());
            if (confrontoVittorie != 0) {
                return confrontoVittorie;
            }
            return e1.getKey().compareToIgnoreCase(e2.getKey());
        });

        int posizione = 1;
        for (Map.Entry<String, Integer> entry : classifica) {
            int numeroVittorie = entry.getValue();
            String etichetta = numeroVittorie == 1 ? "vittoria" : "vittorie";
            System.out.println(posizione + ". " + entry.getKey() + " - " + numeroVittorie + " " + etichetta);
            posizione++;
        }
    }

    private void carica() {
        vittorie.clear();

        if (!Files.exists(FILE_RECORD)) {
            return;
        }

        try {
            List<String> righe = Files.readAllLines(FILE_RECORD, StandardCharsets.UTF_8);

            for (String riga : righe) {
                if (riga == null || riga.trim().isEmpty()) {
                    continue;
                }

                String[] parti = riga.split(";", 2);
                if (parti.length != 2) {
                    continue;
                }

                String nome = parti[0].trim();
                String numero = parti[1].trim();

                try {
                    int conteggio = Integer.parseInt(numero);
                    if (!nome.isEmpty() && conteggio > 0) {
                        vittorie.put(nome, conteggio);
                    }
                } catch (NumberFormatException ignored) {
                    // Riga malformata: la ignoriamo.
                }
            }
        } catch (IOException e) {
            System.out.println(">> ERRORE: impossibile leggere il file dei record.");
        }
    }

    private void salva() {
        List<String> righe = new ArrayList<>();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(vittorie.entrySet());
        entries.sort((e1, e2) -> e1.getKey().compareToIgnoreCase(e2.getKey()));

        for (Map.Entry<String, Integer> entry : entries) {
            righe.add(entry.getKey() + ";" + entry.getValue());
        }

        try {
            Files.write(
                FILE_RECORD,
                righe,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            System.out.println(">> ERRORE: impossibile salvare il file dei record.");
        }
    }
}
