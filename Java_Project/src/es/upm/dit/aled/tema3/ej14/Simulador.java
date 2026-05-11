package es.upm.dit.aled.tema3.ej14;
import java.util.Random;

/**
 * Ejercicio 14: Simulador principal.
 */
public class Simulador {
    
    public static void main(String[] args) {
        int NUM_CLIENTES = 50;
        Peluqueria peluqueria = new Peluqueria();
        Random rand = new Random();

        System.out.println("--- Iniciando Simulación 2.14  (1 Silla, 5 Sofás, 15 Sitios en la barra) ---");

        for (int i = 0; i < NUM_CLIENTES; i++) {
            Cliente cliente = new Cliente(peluqueria);
            Thread hebraCliente = new Thread(cliente, "Cliente-" + i);
            hebraCliente.start();
            
            try {
                // Hacemos que los clientes lleguen en intervalos aleatorios
                Thread.sleep(rand.nextInt(100)); // Un nuevo cliente cada 0-100 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("--- Todos los clientes han sido generados ---");
    }
}