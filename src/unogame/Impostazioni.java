package unogame;

public class Impostazioni {

    private static final int MIN_GIOCATORI_TOTALI = 2;
    private static final int MAX_GIOCATORI_TOTALI = 5;

    private static final int MIN_GIOCATORI_UMANI = 1;
    private static final int MAX_GIOCATORI_UMANI = 5;

    private static final int MIN_GIOCATORI_BOT = 0;
    private static final int MAX_GIOCATORI_BOT = 4;

    private static final int MIN_CARTE_INIZIALI = 1;
    private static final int MAX_CARTE_INIZIALI = 20;

    private static final int MIN_CARTE_MANCATO_UNO = 1;
    private static final int MAX_CARTE_MANCATO_UNO = 10;

    private int numeroGiocatoriUmani = 2;
    private int numeroGiocatoriBot = 0;
    private int carteIniziali = 7;
    private int cartePescateDopoMancatoUno = 2;
    private boolean sfidaPiuQuattro = false;

    public int getNumeroGiocatoriUmani() {
        return numeroGiocatoriUmani;
    }

    public int getNumeroGiocatoriBot() {
        return numeroGiocatoriBot;
    }

    public int getCarteIniziali() {
        return carteIniziali;
    }

    public int getCartePescateDopoMancatoUno() {
        return cartePescateDopoMancatoUno;
    }

    public boolean isSfidaPiuQuattro() {
        return sfidaPiuQuattro;
    }

    public String listaImpostazioni() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- IMPOSTAZIONI ---\n");
        sb.append("1. Numero giocatori umani (1-5): [").append(numeroGiocatoriUmani).append("]\n");
        sb.append("2. Numero giocatori bot (0-4): [").append(numeroGiocatoriBot).append("]\n");
        sb.append("3. Numero carte iniziali (1-20): [").append(carteIniziali).append("]\n");
        sb.append("4. Numero carte pescate dopo mancato UNO! (1-10): [").append(cartePescateDopoMancatoUno).append("]\n");
        sb.append("5. Sfida +4: [").append(sfidaPiuQuattro ? "SI" : "NO").append("]\n");
        sb.append("0. Torna al menu principale\n");
        return sb.toString();
    }

    /**
     * Ritorna null se la modifica va a buon fine.
     * Ritorna una stringa con il messaggio di errore se il valore non è valido.
     */
    public String modificaImpostazione(int indice, String valore) {
        switch (indice) {
            case 1:
                return modificaNumeroGiocatoriUmani(valore);
            case 2:
                return modificaNumeroGiocatoriBot(valore);
            case 3:
                return modificaCarteIniziali(valore);
            case 4:
                return modificaCartePescateDopoMancatoUno(valore);
            case 5:
                return modificaSfidaPiuQuattro(valore);
            default:
                return "Indice impostazione non valido. Scegli un valore tra 0 e 5.";
        }
    }

    private String modificaNumeroGiocatoriUmani(String valore) {
        Integer nuovoValore = parseIntero(valore);

        if (nuovoValore == null) {
            return "Numero giocatori umani non valido: inserisci un numero intero tra "
                    + MIN_GIOCATORI_UMANI + " e " + MAX_GIOCATORI_UMANI + ".";
        }

        if (nuovoValore < MIN_GIOCATORI_UMANI || nuovoValore > MAX_GIOCATORI_UMANI) {
            return "Numero giocatori umani fuori limite: minimo "
                    + MIN_GIOCATORI_UMANI + ", massimo " + MAX_GIOCATORI_UMANI + ".";
        }

        int totale = nuovoValore + numeroGiocatoriBot;
        if (totale > MAX_GIOCATORI_TOTALI) {
            int massimoConsentito = MAX_GIOCATORI_TOTALI - numeroGiocatoriBot;
            return "Troppi giocatori umani: con " + numeroGiocatoriBot
                    + " bot puoi impostare al massimo " + massimoConsentito
                    + " giocatori umani (totale massimo " + MAX_GIOCATORI_TOTALI + ").";
        }

        if (totale < MIN_GIOCATORI_TOTALI) {
            return "Giocatori totali insufficienti: servono almeno "
                    + MIN_GIOCATORI_TOTALI + " giocatori.";
        }

        numeroGiocatoriUmani = nuovoValore;
        return null;
    }

    private String modificaNumeroGiocatoriBot(String valore) {
        Integer nuovoValore = parseIntero(valore);

        if (nuovoValore == null) {
            return "Numero giocatori bot non valido: inserisci un numero intero tra "
                    + MIN_GIOCATORI_BOT + " e " + MAX_GIOCATORI_BOT + ".";
        }

        if (nuovoValore < MIN_GIOCATORI_BOT || nuovoValore > MAX_GIOCATORI_BOT) {
            return "Numero giocatori bot fuori limite: minimo "
                    + MIN_GIOCATORI_BOT + ", massimo " + MAX_GIOCATORI_BOT + ".";
        }

        int totale = numeroGiocatoriUmani + nuovoValore;
        if (totale > MAX_GIOCATORI_TOTALI) {
            int massimoConsentito = MAX_GIOCATORI_TOTALI - numeroGiocatoriUmani;
            return "Troppi bot: con " + numeroGiocatoriUmani
                    + " giocatori umani puoi impostare al massimo " + massimoConsentito
                    + " bot (totale massimo " + MAX_GIOCATORI_TOTALI + ").";
        }

        if (totale < MIN_GIOCATORI_TOTALI) {
            return "Giocatori totali insufficienti: servono almeno "
                    + MIN_GIOCATORI_TOTALI + " giocatori.";
        }

        numeroGiocatoriBot = nuovoValore;
        return null;
    }

    private String modificaCarteIniziali(String valore) {
        Integer nuovoValore = parseIntero(valore);

        if (nuovoValore == null) {
            return "Numero carte iniziali non valido: inserisci un numero intero tra "
                    + MIN_CARTE_INIZIALI + " e " + MAX_CARTE_INIZIALI + ".";
        }

        if (nuovoValore < MIN_CARTE_INIZIALI || nuovoValore > MAX_CARTE_INIZIALI) {
            return "Numero carte iniziali fuori limite: minimo "
                    + MIN_CARTE_INIZIALI + ", massimo " + MAX_CARTE_INIZIALI + ".";
        }

        carteIniziali = nuovoValore;
        return null;
    }

    private String modificaCartePescateDopoMancatoUno(String valore) {
        Integer nuovoValore = parseIntero(valore);

        if (nuovoValore == null) {
            return "Numero carte pescate dopo mancato UNO! non valido: inserisci un numero intero tra "
                    + MIN_CARTE_MANCATO_UNO + " e " + MAX_CARTE_MANCATO_UNO + ".";
        }

        if (nuovoValore < MIN_CARTE_MANCATO_UNO || nuovoValore > MAX_CARTE_MANCATO_UNO) {
            return "Numero carte pescate dopo mancato UNO! fuori limite: minimo "
                    + MIN_CARTE_MANCATO_UNO + ", massimo " + MAX_CARTE_MANCATO_UNO + ".";
        }

        cartePescateDopoMancatoUno = nuovoValore;
        return null;
    }

    private String modificaSfidaPiuQuattro(String valore) {
        Boolean flag = parseSiNo(valore);

        if (flag == null) {
            return "Valore non valido per 'Sfida +4': inserisci SI oppure NO.";
        }

        sfidaPiuQuattro = flag;
        return null;
    }

    private Integer parseIntero(String valore) {
        try {
            return Integer.parseInt(valore.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean parseSiNo(String valore) {
        String v = valore.trim().toLowerCase();
        if (v.equals("si") || v.equals("sì") || v.equals("s") || v.equals("true")) {
            return true;
        }
        if (v.equals("no") || v.equals("n") || v.equals("false")) {
            return false;
        }
        return null;
    }
}