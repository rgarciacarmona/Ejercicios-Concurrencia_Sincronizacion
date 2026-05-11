package es.upm.dit.aled.tema3.ej4;

public class Main {
    public static void main(String[] args) {
        Puente puente = new Puente();
        
        // Lanzamos coches mezclados
        for (int i = 0; i < 15; i++) {
            boolean dir = Math.random() < 0.5; // Dirección aleatoria
            String mat = (dir ? "NS_" : "SN_") + i;
            new Coche(puente, mat, dir).start();
            
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
    }
}