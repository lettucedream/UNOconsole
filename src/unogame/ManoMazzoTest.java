package unogame;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManoMazzoTest {

    @Test
    void manoAggiungeERimuoveCarte() {
        Mano mano = new Mano();
        Carta carta = new CartaNormale(Colore.ROSSO, 3);

        mano.aggiungiCarta(carta);

        assertEquals(1, mano.numeroCarte());
        assertSame(carta, mano.rimuoviCarta(0));
        assertEquals(0, mano.numeroCarte());
    }

    @Test
    void manoRifiutaIndiceNonValido() {
        Mano mano = new Mano();
        mano.aggiungiCarta(new CartaNormale(Colore.BLU, 2));

        assertThrows(IllegalArgumentException.class, () -> mano.rimuoviCarta(-1));
        assertThrows(IllegalArgumentException.class, () -> mano.rimuoviCarta(1));
    }

    @Test
    void listaCarteDellaManoNonModificabileDirettamente() {
        Mano mano = new Mano();
        mano.aggiungiCarta(new CartaNormale(Colore.VERDE, 7));

        assertThrows(UnsupportedOperationException.class,
                () -> mano.getCarte().add(new CartaNormale(Colore.ROSSO, 1)));
    }

    @Test
    void mazzoInizialeContieneNumeroCorrettoDiCarte() {
        Mazzo mazzo = new Mazzo();

        assertEquals(108, mazzo.carteRimaste());
    }

    @Test
    void pescaRiduceIlNumeroDiCarteDelMazzo() {
        Mazzo mazzo = new Mazzo();
        int prima = mazzo.carteRimaste();

        Carta pescata = mazzo.pesca();

        assertNotNull(pescata);
        assertEquals(prima - 1, mazzo.carteRimaste());
    }

    @Test
    void ricaricaRipristinaIlColoreNeroDeiJolly() {
        Mazzo mazzo = new Mazzo();
        while (mazzo.carteRimaste() > 0) {
            mazzo.pesca();
        }

        CartaSpeciale jollyGiaGiocato = new CartaSpeciale(Colore.BLU, "JOLLY");
        mazzo.ricarica(List.of(jollyGiaGiocato));

        Carta pescata = mazzo.pesca();
        assertEquals(Colore.NERO, pescata.getColore());
    }
}
