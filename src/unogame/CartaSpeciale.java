package unogame;

public class CartaSpeciale extends Carta {
    
  
    private String tipoEffetto; 

    public CartaSpeciale(Colore colore, String tipoEffetto) {
        super(colore);
        this.tipoEffetto = tipoEffetto;
    }

    public String getTipoEffetto() {
        return tipoEffetto;
    }


    @Override
    public boolean isGiocabile(Carta altraCarta) {
 
        if (this.colore == Colore.NERO) {
            return true;
        }


        if (this.colore == altraCarta.getColore()) {
            return true;
        }


        if (altraCarta instanceof CartaSpeciale) {
            CartaSpeciale altraSpeciale = (CartaSpeciale) altraCarta;
            return this.tipoEffetto.equals(altraSpeciale.getTipoEffetto());
        }

        return false;
    }

    @Override
    public String toString() {
        String simbolo;
        // Abbelliamo i nomi degli effetti
        switch (getTipoEffetto()) {
            case "STOP": simbolo = "STOP"; break;
            case "INVERTI": simbolo = "INVERTI"; break;
            case "PESCA_DUE": simbolo = "PESCA +2"; break;
            case "JOLLY": simbolo = "JOLLY"; break;
            case "JOLLY_PESCA_QUATTRO": simbolo = "JOLLY +4"; break;
            default: simbolo = getTipoEffetto();
        }
        
        return colore.getCodiceAnsi() + "[ " + simbolo + " ]" + Colore.RESET;
    }
}