package unogame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GiocatoriTest {

    @Test
    void giocatorePescaAggiungeCartaAllaMano() {
        GiocatoreUmano giocatore = new GiocatoreUmano("Mario", "1234");

        giocatore.pesca(new CartaNormale(Colore.ROSSO, 4));

        assertEquals(1, giocatore.getMano().numeroCarte());
    }

    @Test
    void giocatoreUmanoVerificaIlPin() {
        GiocatoreUmano giocatore = new GiocatoreUmano("Luigi", "9999");

        assertTrue(giocatore.verificaPin("9999"));
        assertFalse(giocatore.verificaPin("0000"));
    }

    @Test
    void botSceglieLaPrimaCartaGiocabile() {
        GiocatoreBot bot = new GiocatoreBot("Bot");
        bot.pesca(new CartaNormale(Colore.BLU, 3));
        bot.pesca(new CartaNormale(Colore.GIALLO, 7));
        bot.pesca(new CartaNormale(Colore.ROSSO, 5));

        int indice = bot.scegliCartaDaGiocare(new CartaNormale(Colore.ROSSO, 9));

        assertEquals(2, indice);
    }

    @Test
    void botPescaSeNonHaCarteGiocabili() {
        GiocatoreBot bot = new GiocatoreBot("Bot");
        bot.pesca(new CartaNormale(Colore.BLU, 3));
        bot.pesca(new CartaNormale(Colore.GIALLO, 7));

        assertEquals(-1, bot.scegliCartaDaGiocare(new CartaNormale(Colore.ROSSO, 9)));
    }

    @Test
    void botSceglieIlColorePiuPresenteQuandoGiocaJolly() {
        GiocatoreBot bot = new GiocatoreBot("Bot");
        bot.pesca(new CartaNormale(Colore.BLU, 1));
        bot.pesca(new CartaNormale(Colore.BLU, 2));
        bot.pesca(new CartaNormale(Colore.ROSSO, 3));

        assertEquals(Colore.BLU, bot.scegliColoreJolly());
    }
}
