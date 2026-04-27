package unogame;

public abstract class Giocatore {
    
    protected String nickname;
    protected Mano mano;

    public Giocatore(String nickname) {
        this.nickname = nickname;
        this.mano = new Mano(); 
    }

    public String getNickname() {
        return nickname;
    }

    public Mano getMano() {
        return mano;
    }

  
    public void pesca(Carta c) {
        mano.aggiungiCarta(c);
    }
    
 
    public abstract int scegliCartaDaGiocare(Carta cartaInGioco);

    @Override
    public String toString() {
        return "Giocatore: " + nickname + " (Carte: " + mano.numeroCarte() + ")";
    }
}