package unogame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarteTest {

    @Test
    void cartaNormaleGiocabilePerColoreONumero() {
        CartaNormale carta = new CartaNormale(Colore.ROSSO, 5);

        assertTrue(carta.isGiocabile(new CartaNormale(Colore.ROSSO, 8)));
        assertTrue(carta.isGiocabile(new CartaNormale(Colore.BLU, 5)));
        assertFalse(carta.isGiocabile(new CartaNormale(Colore.BLU, 8)));
    }

    @Test
    void cartaNormaleRifiutaNumeriNonValidi() {
        assertThrows(IllegalArgumentException.class, () -> new CartaNormale(Colore.ROSSO, -1));
        assertThrows(IllegalArgumentException.class, () -> new CartaNormale(Colore.ROSSO, 10));
    }

    @Test
    void cartaSpecialeGiocabilePerColoreOEffetto() {
        CartaSpeciale stopRosso = new CartaSpeciale(Colore.ROSSO, "STOP");

        assertTrue(stopRosso.isGiocabile(new CartaNormale(Colore.ROSSO, 3)));
        assertTrue(stopRosso.isGiocabile(new CartaSpeciale(Colore.BLU, "STOP")));
        assertFalse(stopRosso.isGiocabile(new CartaSpeciale(Colore.BLU, "PESCA_DUE")));
    }

    @Test
    void jollySempreGiocabile() {
        CartaSpeciale jolly = new CartaSpeciale(Colore.NERO, "JOLLY");

        assertTrue(jolly.isGiocabile(new CartaNormale(Colore.GIALLO, 4)));
        assertTrue(jolly.isGiocabile(new CartaSpeciale(Colore.ROSSO, "STOP")));
    }
}
