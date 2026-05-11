package es.upm.dit.aled.tema3.ej15;
import java.util.Random;

/**
 * Ejercicio 15: Hebra que simula un peluquero
 */
public class Peluquero extends Thread {

    private Peluqueria peluqueria;
    private boolean esSirPatrick;
    private Random random = new Random();

    public Peluquero(Peluqueria peluqueria, boolean esSirPatrick) {
        this.peluqueria = peluqueria;
        this.esSirPatrick = esSirPatrick;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // El peluquero espera (duerme) hasta que llegue un cliente
                Cliente cliente = peluqueria.peluqueroEsperaCliente();
                
                // Simula el corte de pelo (Problema 2.15)
                int tiempoCorte;
                if (esSirPatrick) {
                    tiempoCorte = random.nextInt(401); // 0-400ms
                } else {
                    tiempoCorte = random.nextInt(601); // 0-600ms
                }
                Thread.sleep(tiempoCorte);
                
                // Avisa al cliente que ha terminado
                cliente.terminarCorte();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}