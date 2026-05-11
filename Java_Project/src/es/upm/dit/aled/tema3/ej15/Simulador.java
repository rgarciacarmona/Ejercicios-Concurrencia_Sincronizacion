package es.upm.dit.aled.tema3.ej15;
import java.util.Random;

public class Simulador {

    public static void main(String[] args) {
        int NUM_CLIENTES = 50;
        Peluqueria peluqueria = new Peluqueria();
        Random rand = new Random();
        
        System.out.println("--- Iniciando Simulación 2.15 (3 Peluqueros, Sofás, Bar) ---");
        
        // Crear e iniciar los 3 peluqueros
        new Thread(new Peluquero(peluqueria, true), "Sir Patrick").start();
        new Thread(new Peluquero(peluqueria, false), "Ayudante-1").start();
        new Thread(new Peluquero(peluqueria, false), "Ayudante-2").start();
        
        // Crear los clientes
        for (int i = 0; i < NUM_CLIENTES; i++) {
            Cliente cliente = new Cliente(i, peluqueria);
            new Thread(cliente).start();
            
            try {
                // Intervalo de llegada de clientes
                Thread.sleep(rand.nextInt(100)); // Ajustar este valor
            } catch (InterruptedException e) {}
        }
    }
}