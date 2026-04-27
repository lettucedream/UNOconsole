package unogame;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PartitaTest {

    @Test
    void avviaPartitaValidaDistribuisceCarteEPreparaLaCartaInTavola() {
        Impostazioni impostazioni = new Impostazioni();
        impostazioni.modificaImpostazione(3, "2");
        Partita partita = new Partita(impostazioni);

        GiocatoreUmano mario = new GiocatoreUmano("Mario", "1234");
        GiocatoreUmano luigi = new GiocatoreUmano("Luigi", "5678");
        partita.avviaPartita(List.of(mario, luigi));

        assertNotNull(partita.getCartaInGioco());
        assertTrue(partita.getCartaInGioco() instanceof CartaNormale);
        assertEquals("Mario", partita.getGiocatoreCorrente().getNickname());
        assertEquals(2, mario.getMano().numeroCarte());
        assertEquals(2, luigi.getMano().numeroCarte());
        assertFalse(partita.isPartitaFinita());
    }


    @Test
    void giocatorePescaAggiungeUnaCartaAllaMano() {
        Impostazioni impostazioni = new Impostazioni();
        impostazioni.modificaImpostazione(3, "2");
        Partita partita = new Partita(impostazioni);
        GiocatoreUmano mario = new GiocatoreUmano("Mario", "1234");
        GiocatoreUmano luigi = new GiocatoreUmano("Luigi", "5678");
        partita.avviaPartita(List.of(mario, luigi));

        int cartePrima = partita.getGiocatoreCorrente().getMano().numeroCarte();
        partita.giocatorePesca(new Scanner("N\n\n"));

        assertEquals(cartePrima + 1, mario.getMano().numeroCarte());
    }
}
