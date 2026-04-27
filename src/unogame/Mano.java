package unogame;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Mano {
    
    private List<Carta> carteInMano;

    public Mano() {
        this.carteInMano = new ArrayList<>();
    }

    
    public void aggiungiCarta(Carta c) {
        carteInMano.add(c);
    }

    
    public Carta rimuoviCarta(int indice) {
        if (indice < 0 || indice >= carteInMano.size()) {
            throw new IllegalArgumentException("Indice carta non valido!");
        }
        return carteInMano.remove(indice);
    }

    public List<Carta> getCarte() {
        return Collections.unmodifiableList(carteInMano); //per evitare modifiche esterne alla mano
    }
    
    
    public int numeroCarte() {
        return carteInMano.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Le tue carte:\n");
        for (int i = 0; i < carteInMano.size(); i++) {
            sb.append(i).append(": ").append(carteInMano.get(i)).append("\n");
        }
        return sb.toString();
    }
}