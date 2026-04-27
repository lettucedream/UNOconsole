package unogame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Impostazioni impostazioni = new Impostazioni();
        ArchivioRecord archivioRecord = new ArchivioRecord();
        boolean inEsecuzione = true;
        stampaLogo();

        while (inEsecuzione) {
            System.out.println("\t1. Nuova Partita");
            System.out.println("\t2. Impostazioni");
            System.out.println("\t3. Record");
            System.out.println("\t4. Esci");
            System.out.println("");
            System.out.print("   Seleziona un'opzione: ");

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    pulisciSchermo();
                    giocaPartita(scanner, impostazioni, archivioRecord);
                    pulisciSchermo();
                    stampaLogo();
                    break;
                case "2":
                    pulisciSchermo();
                    stampaLogo();
                    gestisciImpostazioni(scanner, impostazioni);
                    pulisciSchermo();
                    stampaLogo();
                    break;
                case "3":
                    pulisciSchermo();
                    stampaLogo();
                    archivioRecord.stampaRecord();
                    System.out.println("\nPremi INVIO per tornare al Menu Principale...");
                    scanner.nextLine();
                    pulisciSchermo();
                    stampaLogo();
                    break;
                case "4":
                    System.out.println("   Grazie per aver giocato! Arrivederci.");
                    inEsecuzione = false;
                    break;
                default:
                    pulisciSchermo();
                    stampaLogo();
                    System.out.println("   Scelta non valida, riprova.\n");
            }
        }
        scanner.close();
    }

    private static void giocaPartita(Scanner scanner, Impostazioni impostazioni, ArchivioRecord archivioRecord) {
        System.out.println("--- BENVENUTO IN UNO CONSOLE ---");

        List<String> nomi = new ArrayList<>();
        List<String> pin = new ArrayList<>();

        for (int i = 1; i <= impostazioni.getNumeroGiocatoriUmani(); i++) {
            System.out.print("Nome Giocatore " + i + ": ");
            String nome = scanner.nextLine();

            String pinGiocatore = leggiPin(scanner, nome);

            nomi.add(nome);
            pin.add(pinGiocatore);
        }

        boolean rigioca;

        do {
            Partita partita = new Partita(impostazioni);

            List<GiocatoreUmano> giocatoriUmani = new ArrayList<>();

            for (int i = 0; i < nomi.size(); i++) {
                giocatoriUmani.add(new GiocatoreUmano(nomi.get(i), pin.get(i)));
            }

            partita.avviaPartita(giocatoriUmani);

            System.out.println("\n--- INIZIO PARTITA! ---");

            while (!partita.isPartitaFinita()) {
                Giocatore corrente = partita.getGiocatoreCorrente();
                Carta cartaInTavola = partita.getCartaInGioco();

                if (partita.giocatoreCorrenteIsBot()) {
                    System.out.println("\n------------------------------------------------");
                    System.out.println("CARTA IN TAVOLA: " + cartaInTavola);
                    System.out.println("TOCCA A: " + corrente.getNickname());
                    System.out.println("Turno automatico del bot...");
                    partita.giocaTurnoBot();

                    if (!partita.isPartitaFinita()) {
                        System.out.println("\nPremi INVIO per continuare...");
                        scanner.nextLine();
                    }
                } else {
                    partita.preparaTurno(scanner);

                    corrente = partita.getGiocatoreCorrente();
                    cartaInTavola = partita.getCartaInGioco();

                    System.out.println("\n------------------------------------------------");
                    System.out.println("CARTA IN TAVOLA: " + cartaInTavola);
                    System.out.println("TOCCA A: " + corrente.getNickname());
                    System.out.println(corrente.getMano());

                    System.out.println("Scegli l'INDICE della carta da giocare (0, 1, 2...)");
                    System.out.print("Oppure digita -1 per PESCARE: ");

                    partita.giocaTurno(scanner);
                }
            }

            String vincitore = partita.getGiocatoreCorrente().getNickname();
            archivioRecord.registraVittoria(vincitore);

            System.out.println("\n================================================");
            System.out.println("             PARTITA TERMINATA!                 ");
            System.out.println("   Il vincitore è: " + vincitore);
            System.out.println("   Record aggiornato con successo.");
            System.out.println("================================================\n");

            rigioca = chiediSeRigiocare(scanner);

            if (rigioca) {
                pulisciSchermo();
                System.out.println("--- NUOVA PARTITA CON LE STESSE IMPOSTAZIONI ---");
            }

        } while (rigioca);
    }

    private static boolean chiediSeRigiocare(Scanner scanner) {
        while (true) {
            System.out.print("Vuoi rigiocare con le stesse impostazioni? (S/N): ");
            String risposta = scanner.nextLine().trim();

            if (risposta.equalsIgnoreCase("S") ||
                risposta.equalsIgnoreCase("SI") ||
                risposta.equalsIgnoreCase("SÌ")) {
                return true;
            }

            if (risposta.equalsIgnoreCase("N") ||
                risposta.equalsIgnoreCase("NO")) {
                return false;
            }

            System.out.println(">> ERRORE: inserisci S oppure N.");
        }
    }

    private static void stampaLogo() {
        String R = Colore.ROSSO.getCodiceAnsi();
        String B = Colore.BLU.getCodiceAnsi();
        String G = Colore.VERDE.getCodiceAnsi();
        String Y = Colore.GIALLO.getCodiceAnsi();
        String N = Colore.NERO.getCodiceAnsi();
        String W = "\u001B[37;1m";
        String RST = Colore.RESET;

        System.out.println(N + "══════════════════════════════════════" + RST);
        System.out.println("");
        System.out.println(
            R + "   ██    ██ " + RST +
            Y + "███    ██ " + RST +
            G + " ██████ " + RST
        );

        System.out.println(
            R + "   ██    ██ " + RST +
            Y + "████   ██ " + RST +
            G + "██    ██" + RST
        );

        System.out.println(
            R + "   ██    ██ " + RST +
            Y + "██ ██  ██ " + RST +
            G + "██    ██" + RST
        );

        System.out.println(
            R + "   ██    ██ " + RST +
            Y + "██  ██ ██ " + RST +
            G + "██    ██" + RST
        );

        System.out.println(
            R + "    ██████  " + RST +
            Y + "██   ████ " + RST +
            G + " ██████ " + RST
        );

        System.out.println("                           " + W + "C O N S O L E" + RST);
        System.out.println(N + "══════════════════════════════════════" + RST);
    }

    private static void gestisciImpostazioni(Scanner scanner, Impostazioni impostazioni) {
        boolean inConfigurazione = true;

        while (inConfigurazione) {
            System.out.println(impostazioni.listaImpostazioni());
            System.out.print("Seleziona l'impostazione da modificare: ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                System.out.println("Configurazione terminata. Ritorno al menu principale...");
                inConfigurazione = false;
                continue;
            }

            int indice;
            try {
                indice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(">> ERRORE: inserisci un indice valido.");
                continue;
            }

            if (indice < 1 || indice > 5) {
                System.out.println(">> ERRORE: impostazione non valida.");
                continue;
            }

            System.out.print("Inserisci il nuovo valore: ");
            String valore = scanner.nextLine().trim();

            String errore = impostazioni.modificaImpostazione(indice, valore);

            if (errore == null) {
                System.out.println(">> Impostazione aggiornata con successo.");
            } else {
                System.out.println(">> ERRORE: " + errore);
            }
        }
    }

    public static void pulisciSchermo() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static String leggiPin(Scanner scanner, String nomeGiocatore) {
        while (true) {
            System.out.print("Inserisci il PIN di " + nomeGiocatore + " (4 cifre): ");
            String pin = scanner.nextLine().trim();

            if (pin.matches("\\d{4}")) {
                return pin;
            }

            System.out.println(">> ERRORE: il PIN deve contenere esattamente 4 cifre.");
        }
    }
}
