package es.upm.dit.aled.tema3.ej15;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Ejercicio 15: Monitor con 3 peluqueros
 * Los peluqueros duermen (wait) si la cola de sofás está vacía
 * Los clientes que llegan (a sofás) les despiertan (notify)
 */
public class Peluqueria {

    private final int MAX_SOFAS = 5;
    private final int MAX_BAR = 15;

    // Usamos colas para mantener el orden de llegada
    private Queue<Cliente> clientesEnSofa = new LinkedList<>();
    private Queue<Cliente> clientesEnBar = new LinkedList<>();

    /**
     * Lo invoca la hebra Cliente
     * El cliente intenta sentarse en el sofá o la barra
     * Retorna true si consigue sitio, false si se va
     */
    public synchronized boolean clienteLlega(Cliente cliente) {
        // Si todo lleno, se va
        if (clientesEnSofa.size() == MAX_SOFAS && clientesEnBar.size() == MAX_BAR) {
            System.out.println("Cliente " + cliente.getId() + " SE VA. (Todo lleno)");
            return false;
        }
        
        // Intenta ir al sofá
        if (clientesEnSofa.size() < MAX_SOFAS) {
            clientesEnSofa.offer(cliente);
            System.out.println("Cliente " + cliente.getId() + " espera en SOFÁ. (Sofás: " + clientesEnSofa.size() + ")");
            
            // Despierta a un peluquero que esté durmiendo
            notify(); // No es necesario despertar a todos (pero no pasaría nada si se hace)
        } 
        // Si sofás llenos, va a la barra
        else {
            clientesEnBar.offer(cliente);
            System.out.println("Cliente " + cliente.getId() + " espera en BAR. (Bar: " + clientesEnBar.size() + ")");
        }
        return true;
    }

    /**
     * Lo invoca la hebra Peluquero
     * El peluquero espera (duerme) hasta que haya alguien en un sofá
     * Cuando atiende a uno, mueve a otro de la barra al sofá
     * Retorna el Cliente al que va a atender
     */
    public synchronized Cliente peluqueroEsperaCliente() throws InterruptedException {
        // El peluquero duerme si no hay nadie en los sofás
        while (clientesEnSofa.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " duerme (sofás vacíos)... ZzzZz");
            wait();
        }
        
        // Se despierta. ¡Hay un cliente!
        Cliente cliente = clientesEnSofa.poll(); // Coge al cliente del sofá
        System.out.println(Thread.currentThread().getName() + " atiende a Cliente " + cliente.getId() + ". (Sofás: " + clientesEnSofa.size() + ")");
        
        // Mueve a un cliente de la barra al sofá
        if (!clientesEnBar.isEmpty()) {
            Cliente clienteBar = clientesEnBar.poll();
            clientesEnSofa.offer(clienteBar);
            System.out.println("Cliente " + clienteBar.getId() + " pasa de BAR a SOFÁ. (Bar: " + clientesEnBar.size() + ", Sofás: " + clientesEnSofa.size() + ")");
        }
        
        return cliente;
    }
    
    /**
     * Lo invoca el cliente cuando su corte ha terminado para "irse"
     */
    public synchronized void clienteSeVa(Cliente cliente) {
         System.out.println("Cliente " + cliente.getId() + " TERMINA y se va.");
    }
}