package unogame;

public class CartaNormale extends Carta {
    
    private int numero;

    public CartaNormale(Colore colore, int numero) {
        super(colore); 
        if (numero < 0 || numero > 9) {
            throw new IllegalArgumentException("Il numero deve essere tra 0 e 9");
        }
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }


    @Override
    public boolean isGiocabile(Carta altraCarta) {
        if (this.colore == altraCarta.getColore()) {
            return true;
        }
        
        if (altraCarta instanceof CartaNormale) {
            CartaNormale altraNormale = (CartaNormale) altraCarta;
            return this.numero == altraNormale.getNumero();
        }

        return false;
    }

    @Override
    public String toString() {
        return colore.getCodiceAnsi() + "[ " + numero + " ]" + Colore.RESET;
    }
}