package unogame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImpostazioniTest {

    @Test
    void valoriInizialiCorretti() {
        Impostazioni impostazioni = new Impostazioni();

        assertEquals(2, impostazioni.getNumeroGiocatoriUmani());
        assertEquals(0, impostazioni.getNumeroGiocatoriBot());
        assertEquals(7, impostazioni.getCarteIniziali());
        assertEquals(2, impostazioni.getCartePescateDopoMancatoUno());
        assertFalse(impostazioni.isSfidaPiuQuattro());
    }

    @Test
    void modificaValoriValidi() {
        Impostazioni impostazioni = new Impostazioni();

        assertNull(impostazioni.modificaImpostazione(1, "3"));
        assertNull(impostazioni.modificaImpostazione(2, "2"));
        assertNull(impostazioni.modificaImpostazione(3, "5"));
        assertNull(impostazioni.modificaImpostazione(4, "4"));
        assertNull(impostazioni.modificaImpostazione(5, "SI"));

        assertEquals(3, impostazioni.getNumeroGiocatoriUmani());
        assertEquals(2, impostazioni.getNumeroGiocatoriBot());
        assertEquals(5, impostazioni.getCarteIniziali());
        assertEquals(4, impostazioni.getCartePescateDopoMancatoUno());
        assertTrue(impostazioni.isSfidaPiuQuattro());
    }

    @Test
    void rifiutaValoriNonValidi() {
        Impostazioni impostazioni = new Impostazioni();

        assertNotNull(impostazioni.modificaImpostazione(1, "1"));
        assertNotNull(impostazioni.modificaImpostazione(2, "4"));
        assertNotNull(impostazioni.modificaImpostazione(3, "0"));
        assertNotNull(impostazioni.modificaImpostazione(4, "11"));
        assertNotNull(impostazioni.modificaImpostazione(5, "forse"));
        assertNotNull(impostazioni.modificaImpostazione(99, "1"));
    }
}
