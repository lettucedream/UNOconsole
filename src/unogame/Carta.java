package unogame;


public abstract class Carta {
	
    protected Colore colore;

    public Carta(Colore colore) {
        this.colore = colore;
    }

    public Colore getColore() {
        return colore;
    }

    public void setColore(Colore colore) {
        this.colore = colore;
    }

  
    public abstract boolean isGiocabile(Carta altraCarta);

    @Override
    public abstract String toString();
}