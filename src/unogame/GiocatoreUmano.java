package unogame;

public class GiocatoreUmano extends Giocatore {

    private String pin;

    public GiocatoreUmano(String nickname, String pin) {
        super(nickname);
        this.pin = pin;
    }

    public boolean verificaPin(String input) {
        return pin.equals(input);
    }

    @Override
    public int scegliCartaDaGiocare(Carta cartaInGioco) {
        return -1;
    }
}