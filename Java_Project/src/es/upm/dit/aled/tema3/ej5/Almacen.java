package es.upm.dit.aled.tema3.ej5;

public class Almacen {
    private int productos = 0;
    private final int CAPACIDAD = 10;

    public synchronized void almacenar(String nombreProd) throws InterruptedException {
        // Esperar si está lleno
        while (productos >= CAPACIDAD) {
            System.out.println("... Productor " + nombreProd + " espera (Almacén LLENO).");
            wait();
        }
        
        productos++;
        System.out.println("+++ Productor " + nombreProd + " almacena. Stock: " + productos);
        notifyAll(); // Avisar a los consumidores (y otros productores)
    }

    public synchronized void extraer(String nombreCons) throws InterruptedException {
        // Esperar si está vacío
        while (productos == 0) {
            System.out.println("... Consumidor " + nombreCons + " espera (Almacén VACÍO).");
            wait();
        }

        productos--;
        System.out.println("--- Consumidor " + nombreCons + " extrae. Stock: " + productos);
        notifyAll(); // Avisar a los productores (y otros consumidores)
    }
}