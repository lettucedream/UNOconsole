package unogame;

public class GiocatoreBot extends Giocatore {

    public GiocatoreBot(String nickname) {
        super(nickname);
    }

    @Override
    public int scegliCartaDaGiocare(Carta cartaInGioco) {
        for (int i = 0; i < mano.getCarte().size(); i++) {
            Carta c = mano.getCarte().get(i);
            if (c.isGiocabile(cartaInGioco)) {
                return i;
            }
        }
        return -1;
    }

    public boolean vuoleGiocareCartaPescata(Carta pescata, Carta cartaInGioco) {
        return pescata.isGiocabile(cartaInGioco);
    }

    public Colore scegliColoreJolly() {
        int rosso = 0;
        int blu = 0;
        int verde = 0;
        int giallo = 0;

        for (Carta c : mano.getCarte()) {
            if (c.getColore() == Colore.ROSSO) rosso++;
            else if (c.getColore() == Colore.BLU) blu++;
            else if (c.getColore() == Colore.VERDE) verde++;
            else if (c.getColore() == Colore.GIALLO) giallo++;
        }

        Colore migliore = Colore.ROSSO;
        int max = rosso;

        if (blu > max) {
            max = blu;
            migliore = Colore.BLU;
        }

        if (verde > max) {
            max = verde;
            migliore = Colore.VERDE;
        }

        if (giallo > max) {
            migliore = Colore.GIALLO;
        }

        return migliore;
    }
}