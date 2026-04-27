package unogame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Partita {

    private Mazzo mazzoPesca;
    private List<Carta> mazzoScarti; 
    private List<Giocatore> giocatori;
    
    private int indiceGiocatoreCorrente; 
    private boolean sensoOrario; 
    private boolean partitaFinita;
    private Impostazioni impostazioni;

    public Partita(Impostazioni impostazioni) {
        this.impostazioni = impostazioni;
        this.mazzoPesca = new Mazzo();
        this.mazzoScarti = new ArrayList<>();
        this.giocatori = new ArrayList<>();
        this.indiceGiocatoreCorrente = 0;
        this.sensoOrario = true; 
        this.partitaFinita = false;
    }

  

    public void avviaPartita(List<GiocatoreUmano> giocatoriUmani) {
        giocatori.clear();

        giocatori.addAll(giocatoriUmani);

        for (int i = 1; i <= impostazioni.getNumeroGiocatoriBot(); i++) {
            giocatori.add(new GiocatoreBot("Bot " + i));
        }

        if (giocatori.size() < 2 || giocatori.size() > 5) {
            throw new IllegalStateException("Il numero totale di giocatori deve essere tra 2 e 5.");
        }

        mazzoPesca.mescola();

        for (int i = 0; i < impostazioni.getCarteIniziali(); i++) {
            for (Giocatore g : giocatori) {
                g.pesca(mazzoPesca.pesca());
            }
        }

        Carta primaCarta = mazzoPesca.pesca();

        while (primaCarta instanceof CartaSpeciale) {
            System.out.println("Prima carta estratta: " + primaCarta + " -> Non valida, rimescolo.");
            mazzoPesca.aggiungiSotto(primaCarta);
            mazzoPesca.mescola();
            primaCarta = mazzoPesca.pesca();
        }

        mazzoScarti.add(primaCarta);
        System.out.println("Partita iniziata! Carta in tavola: " + primaCarta);
    }

    public Carta getCartaInGioco() {
        if (mazzoScarti.isEmpty()) return null;
        return mazzoScarti.get(mazzoScarti.size() - 1);
    }

    public Giocatore getGiocatoreCorrente() {
        return giocatori.get(indiceGiocatoreCorrente);
    }

    public boolean giocatoreCorrenteIsBot() {
        return getGiocatoreCorrente() instanceof GiocatoreBot;
    }

    private void controllaDichiarazioneUnoBot(Giocatore giocatore) {
        if (giocatore.getMano().numeroCarte() == 1) {
            System.out.println(giocatore.getNickname() + " dice UNO!");
        }
    }

    private void autenticaGiocatoreCorrente(Scanner scanner) {
        Giocatore corrente = getGiocatoreCorrente();

        if (!(corrente instanceof GiocatoreUmano)) {
            return;
        }

        GiocatoreUmano umano = (GiocatoreUmano) corrente;

        while (true) {
            System.out.print("Inserisci il PIN di " + umano.getNickname() + ": ");
            String input = scanner.nextLine().trim();

            if (umano.verificaPin(input)) {
                System.out.println("Autenticazione riuscita.");
                return;
            }

            System.out.println("PIN errato. Ritenta.");
        }
    }

    public void preparaTurno(Scanner scanner) {
        Main.pulisciSchermo();
        autenticaGiocatoreCorrente(scanner);
        Main.pulisciSchermo();
    }
        
  
    public void giocaTurno(Scanner scanner) {
        Giocatore corrente = getGiocatoreCorrente();

        while (true) {
            try {
                String input = scanner.nextLine();
                int scelta = Integer.parseInt(input);

                if (scelta == -1) {
                    giocatorePesca(scanner);
                    return;
                }

                if (scelta < 0 || scelta >= corrente.getMano().numeroCarte()) {
                    System.out.println(">> ERRORE: Indice non valido!");
                    System.out.println("Scegli un indice valido oppure digita -1 per pescare: ");
                    continue;
                }

                Carta cartaScelta = corrente.getMano().getCarte().get(scelta);
                Carta cartaInTavola = getCartaInGioco();
                Colore coloreScelto = null;

                if (cartaScelta.getColore() == Colore.NERO) {
                    coloreScelto = scegliColoreJolly(scanner);
                }

                if (!cartaScelta.isGiocabile(cartaInTavola)) {
                    System.out.println(">> ERRORE: Mossa non valida! Quella carta non si può giocare.");
                    System.out.println("Riprova scegliendo un'altra carta oppure digita -1 per pescare: ");
                    continue;
                }

                corrente.getMano().rimuoviCarta(scelta);

                if (cartaScelta.getColore() == Colore.NERO && coloreScelto != null) {
                    cartaScelta.setColore(coloreScelto);
                    System.out.println("Colore cambiato in: " + coloreScelto);
                }

                mazzoScarti.add(cartaScelta);
                System.out.println(corrente.getNickname() + " gioca: " + cartaScelta);

                if (corrente.getMano().numeroCarte() == 0) {
                    partitaFinita = true;
                    System.out.println("VITTORIA! " + corrente.getNickname() + " ha vinto la partita!");
                    return;
                }

                gestisciFineTurnoUmano(scanner, corrente);

                applicaEffetti(cartaScelta);
                passaTurno();
                return;

            } catch (NumberFormatException e) {
                System.out.println(">> ERRORE: Devi inserire un numero!");
                System.out.println("Riprova scegliendo un indice valido oppure digita -1 per pescare: ");
            } catch (Exception e) {
                System.out.println(">> ERRORE IMPREVISTO: " + e.getMessage());
                System.out.println("Riprova: ");
            }
        }
    }

    public void giocaTurnoBot() {
        GiocatoreBot bot = (GiocatoreBot) getGiocatoreCorrente();
        Carta cartaInTavola = getCartaInGioco();

        int scelta = bot.scegliCartaDaGiocare(cartaInTavola);

        // Caso 1: il bot non ha carte giocabili e pesca
        if (scelta == -1) {
            Carta pescata = pescaDalMazzo();
            bot.pesca(pescata);
            System.out.println(bot.getNickname() + " pesca una carta.");

            if (bot.vuoleGiocareCartaPescata(pescata, getCartaInGioco())) {
                int indiceNuovaCarta = bot.getMano().numeroCarte() - 1;
                bot.getMano().rimuoviCarta(indiceNuovaCarta);

                if (pescata.getColore() == Colore.NERO) {
                    Colore coloreScelto = bot.scegliColoreJolly();
                    pescata.setColore(coloreScelto);
                    System.out.println(bot.getNickname() + " cambia il colore in: " + coloreScelto);
                }

                mazzoScarti.add(pescata);
                System.out.println(bot.getNickname() + " gioca la carta appena pescata: " + pescata);

                controllaDichiarazioneUnoBot(bot);

                if (bot.getMano().numeroCarte() == 0) {
                    partitaFinita = true;
                    System.out.println("VITTORIA! " + bot.getNickname() + " ha vinto la partita!");
                    return;
                }

                applicaEffetti(pescata);
            }

            passaTurno();
            return;
        }

        // Caso 2: il bot ha una carta giocabile e la gioca
        Carta cartaScelta = bot.getMano().getCarte().get(scelta);

        if (!cartaScelta.isGiocabile(cartaInTavola)) {
            System.out.println(">> ERRORE BOT: carta non giocabile.");
            passaTurno();
            return;
        }

        bot.getMano().rimuoviCarta(scelta);

        if (cartaScelta.getColore() == Colore.NERO) {
            Colore coloreScelto = bot.scegliColoreJolly();
            cartaScelta.setColore(coloreScelto);
            System.out.println(bot.getNickname() + " cambia il colore in: " + coloreScelto);
        }

        mazzoScarti.add(cartaScelta);
        System.out.println(bot.getNickname() + " gioca: " + cartaScelta);

        controllaDichiarazioneUnoBot(bot);

        if (bot.getMano().numeroCarte() == 0) {
            partitaFinita = true;
            System.out.println("VITTORIA! " + bot.getNickname() + " ha vinto la partita!");
            return;
        }

        applicaEffetti(cartaScelta);
        passaTurno();
    }

    // metodo sicuro per pescare una carta, con gestione del caso mazzo esaurito
    private Carta pescaDalMazzo() {
        if (mazzoPesca.carteRimaste() == 0) {
            if (mazzoScarti.size() <= 1) {
                throw new IllegalStateException("Non ci sono abbastanza carte per ricaricare il mazzo.");
            }

            System.out.println("\n[!] Mazzo di pesca esaurito! Rimescolo gli scarti...");

            Carta cartaInTavola = mazzoScarti.remove(mazzoScarti.size() - 1);

            mazzoPesca.ricarica(new ArrayList<>(mazzoScarti));

            mazzoScarti.clear();
            mazzoScarti.add(cartaInTavola);
        }

        return mazzoPesca.pesca();
    }


    // gestisce l'azione del giocatore
    public void giocatorePesca(Scanner scanner) {
        Giocatore corrente = getGiocatoreCorrente();

        Carta pescata = pescaDalMazzo();
        corrente.pesca(pescata);
        System.out.println(corrente.getNickname() + " ha pescato: " + pescata);

        if (pescata.isGiocabile(getCartaInGioco())) {
            System.out.print("La carta è giocabile! Vuoi giocarla subito? (S/N): ");
            String risposta = scanner.nextLine();

            if (risposta.equalsIgnoreCase("S")) {
                int indiceNuovaCarta = corrente.getMano().numeroCarte() - 1;
                Colore coloreScelto = null;

                if (pescata.getColore() == Colore.NERO) {
                    coloreScelto = scegliColoreJolly(scanner);
                }

                corrente.getMano().rimuoviCarta(indiceNuovaCarta);

                if (pescata.getColore() == Colore.NERO && coloreScelto != null) {
                    pescata.setColore(coloreScelto);
                }

                mazzoScarti.add(pescata);
                System.out.println(corrente.getNickname() + " gioca la carta appena pescata: " + pescata);

                if (corrente.getMano().numeroCarte() == 0) {
                    partitaFinita = true;
                    System.out.println("VITTORIA! " + corrente.getNickname() + " ha vinto la partita!");
                    return;
                }

                gestisciFineTurnoUmano(scanner, corrente);
                applicaEffetti(pescata);
                passaTurno();
                return;
            }
        }
        gestisciFineTurnoUmano(scanner, corrente);
        passaTurno();
    }


    private void applicaEffetti(Carta c) {
        if (c instanceof CartaSpeciale) {
            CartaSpeciale s = (CartaSpeciale) c;

            switch (s.getTipoEffetto()) {
                case "STOP":
                    System.out.println("Effetto STOP: il prossimo giocatore salta il turno!");
                    passaTurno();
                    break;

                case "INVERTI":
                    System.out.println("Effetto INVERTI: si cambia giro!");
                    sensoOrario = !sensoOrario;
                    if (giocatori.size() == 2) {
                        passaTurno();
                    }
                    break;

                case "PESCA_DUE":
                    System.out.println("Effetto +2: il prossimo pesca 2 carte e salta il turno!");
                    Giocatore prossimo = getGiocatoreSuccessivo();
                    prossimo.pesca(pescaDalMazzo());
                    prossimo.pesca(pescaDalMazzo());
                    passaTurno();
                    break;

                case "JOLLY_PESCA_QUATTRO":
                    System.out.println("Effetto +4: il prossimo pesca 4 carte e salta il turno!");
                    Giocatore sfortunato = getGiocatoreSuccessivo();
                    for (int i = 0; i < 4; i++) {
                        sfortunato.pesca(pescaDalMazzo());
                    }
                    passaTurno();
                    break;

                case "JOLLY":
                    System.out.println("Effetto JOLLY: Colore cambiato!");
                    break;
            }
        }
    }


    private void passaTurno() {
        if (partitaFinita) return;

        if (sensoOrario) {
            indiceGiocatoreCorrente++;
            if (indiceGiocatoreCorrente >= giocatori.size()) indiceGiocatoreCorrente = 0;
        } else {
            indiceGiocatoreCorrente--;
            if (indiceGiocatoreCorrente < 0) indiceGiocatoreCorrente = giocatori.size() - 1;
        }
    }
    

    private Giocatore getGiocatoreSuccessivo() {
        int indiceTemp = indiceGiocatoreCorrente;
        if (sensoOrario) {
            indiceTemp++;
            if (indiceTemp >= giocatori.size()) indiceTemp = 0;
        } else {
            indiceTemp--;
            if (indiceTemp < 0) indiceTemp = giocatori.size() - 1;
        }
        return giocatori.get(indiceTemp);
    }
    
    public boolean isPartitaFinita() {
        return partitaFinita;
    }
    

    private Colore scegliColoreJolly(Scanner scanner) {
        Colore coloreScelto = null;
        boolean sceltaValida = false;
        
        while (!sceltaValida) {
            System.out.println("Scegli il nuovo colore (1: ROSSO, 2: BLU, 3: VERDE, 4: GIALLO):");
            String inputColore = scanner.nextLine();
            
            switch(inputColore) {
                case "1": coloreScelto = Colore.ROSSO; sceltaValida = true; break;
                case "2": coloreScelto = Colore.BLU; sceltaValida = true; break;
                case "3": coloreScelto = Colore.VERDE; sceltaValida = true; break;
                case "4": coloreScelto = Colore.GIALLO; sceltaValida = true; break;
                default: System.out.println(">> ERRORE: Colore non valido. Ritenta!");
            }
        }
        return coloreScelto;
    }

    private void gestisciFineTurnoUmano(Scanner scanner, Giocatore giocatore) {
        System.out.print("Premi INVIO per terminare il turno...");
        String input = scanner.nextLine().trim();

        if (giocatore.getMano().numeroCarte() == 1) {
            if (input.equalsIgnoreCase("UNO!")) {
                System.out.println("UNO! dichiarato correttamente.");
            } else {
                int penalita = impostazioni.getCartePescateDopoMancatoUno();

                System.out.println("Hai dimenticato di dichiarare UNO!: peschi " + penalita + " carte!");

                for (int i = 0; i < penalita; i++) {
                    giocatore.pesca(pescaDalMazzo());
                }

                System.out.print("Premi INVIO per terminare il turno...");
                scanner.nextLine();
            }
        }
    }
    
}