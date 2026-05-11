package es.upm.dit.aled.tema3.ej10;

public class Vehiculo extends Thread {
    private Puente puente;
    private int peso;
    private boolean esAmbulancia;

    public Vehiculo(Puente p, int peso, boolean ambulancia) {
        this.puente = p;
        this.peso = peso;
        this.esAmbulancia = ambulancia;
    }

    @Override
    public void run() {
        try {
            puente.entrarPuente(peso, esAmbulancia);
            
            // Cruzando el puente
            Thread.sleep((long) (Math.random() * 1000));
            
            puente.salirPuente(peso);
        } catch (InterruptedException e) {}
    }
}