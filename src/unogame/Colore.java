package unogame;

public enum Colore {
    ROSSO("\u001B[31m"), 
    BLU("\u001B[34m"), 
    VERDE("\u001B[32m"), 
    GIALLO("\u001B[33m"), 
    NERO("\u001B[30;1m"); 

    public static final String RESET = "\u001B[0m";

    private String codiceAnsi;

    Colore(String codiceAnsi) {
        this.codiceAnsi = codiceAnsi;
    }

    public String getCodiceAnsi() {
        return codiceAnsi;
    }
}