package es.upm.dit.aled.tema3.ej10;

public class Main {
    public static void main(String[] args) {
        Puente puente = new Puente();

        for (int i = 0; i < 20; i++) {
            // Generar atributos aleatorios
            // Peso entre 1000 y 4000 kg
            int peso = 1000 + (int)(Math.random() * 3000);
            // 10% de probabilidad de ser ambulancia
            boolean esAmbulancia = Math.random() < 0.15; 
            
            new Vehiculo(puente, peso, esAmbulancia).start();
            
            try { Thread.sleep(200); } catch (InterruptedException e) {}
        }
    }
}