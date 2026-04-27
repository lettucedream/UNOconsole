package unogame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazzo {
    
    
    private List<Carta> carte;

    public Mazzo() {
        this.carte = new ArrayList<>();
        inizializzaMazzo(); 
    }

    
    private void inizializzaMazzo() {
        for (Colore c : Colore.values()) {
            if (c == Colore.NERO) continue; 

            
            carte.add(new CartaNormale(c, 0));
            

            for (int i = 1; i <= 9; i++) {
                carte.add(new CartaNormale(c, i));
                carte.add(new CartaNormale(c, i));
            }


            String[] effettiSpeciali = {"STOP", "INVERTI", "PESCA_DUE"};
            for (String effetto : effettiSpeciali) {
                carte.add(new CartaSpeciale(c, effetto));
                carte.add(new CartaSpeciale(c, effetto));
            }
        }


        for (int i = 0; i < 4; i++) {
            carte.add(new CartaSpeciale(Colore.NERO, "JOLLY"));
            carte.add(new CartaSpeciale(Colore.NERO, "JOLLY_PESCA_QUATTRO"));
        }
    }


    public void mescola() {
        Collections.shuffle(this.carte);
    }


    public Carta pesca() {
        if (carte.isEmpty()) {
            // rimescolare gli scarti!!!!!!
            throw new IllegalStateException("Il mazzo è finito!"); 
        }
        return carte.remove(0);
    }
    
    public void aggiungiSotto(Carta c) {
        carte.add(c);
    }
    
    
    public int carteRimaste() {
        return carte.size();
    }
    
    public void ricarica(List<Carta> scarti) {
    	
    	for (Carta c : scarti) {
            if (c instanceof CartaSpeciale) {
                CartaSpeciale cs = (CartaSpeciale) c;
                String effetto = cs.getTipoEffetto();
                
                if (effetto.equals("JOLLY") || effetto.equals("JOLLY_PESCA_QUATTRO")) {
                    cs.setColore(Colore.NERO);
                }
            }
        }
    	
        this.carte.addAll(scarti);
        mescola();
    }
    
}