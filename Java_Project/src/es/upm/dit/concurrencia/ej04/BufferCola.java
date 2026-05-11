package es.upm.dit.concurrencia.ej04;

import java.util.LinkedList;
import java.util.Queue;

public class BufferCola {

    // Estructura de datos para el buffer (Array de 10 enteros) 
    private final int TAMANIO = 10;
    private int[] buffer = new int[TAMANIO];
    
    // Punteros para gestionar el buffer como una cola circular 
    private int entrada = 0;
    private int salida = 0;
    private int cantidad = 0;

    // Listas de espera
    private Queue<Thread> esperaProductores = new LinkedList<>();
    private Queue<Thread> esperaConsumidores = new LinkedList<>();

    /**
     * Método para que las hebras productoras introduzcan un dato
     */
    public synchronized void producir(int dato) {
        Thread yo = Thread.currentThread();
        esperaProductores.add(yo); // Se añade a la cola de productores

        try {
            // Esperar si:
            // 1. No soy el primero de la cola de productores (orden de llegada)
            // 2. El buffer está lleno
            while (esperaProductores.peek() != yo || cantidad == TAMANIO) {
                wait();
            }

            // Sección Crítica: insertar dato
            esperaProductores.poll(); // Sale de la cola de espera
            buffer[entrada] = dato;
            entrada = (entrada + 1) % TAMANIO;
            cantidad++;
            
            System.out.println(">>> Productor inserta: " + dato + " (Datos en buffer: " + cantidad + ")");
            
            // Notificar a todos para que el siguiente productor o consumidor evalúe su condición
            notifyAll();

        } catch (InterruptedException e) {
            esperaProductores.remove(yo);
            notifyAll();
        }
    }

    /**
     * Método para que las hebras consumidoras extraigan un dato
     */
    public synchronized Integer consumir() {
        Thread yo = Thread.currentThread();
        esperaConsumidores.add(yo); // Se añade a la cola de consumidores

        try {
            // Esperar si:
            // 1. No soy el primero de la cola de consumidores (orden de llegada)
            // 2. El buffer está vacío
            while (esperaConsumidores.peek() != yo || cantidad == 0) {
                wait();
            }

            // Sección Crítica: extraer dato
            esperaConsumidores.poll(); // Sale de la cola de espera
            int dato = buffer[salida];
            salida = (salida + 1) % TAMANIO;
            cantidad--;

            System.out.println("<<< Consumidor extrae: " + dato + " (Datos en buffer: " + cantidad + ")");
            
            notifyAll();
            return dato;

        } catch (InterruptedException e) {
            esperaConsumidores.remove(yo);
            notifyAll();
            return null;
        }
    }
}