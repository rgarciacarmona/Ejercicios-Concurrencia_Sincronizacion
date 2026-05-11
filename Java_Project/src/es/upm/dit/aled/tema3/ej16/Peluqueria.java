package es.upm.dit.aled.tema3.ej16;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Ejercicio 16: Monitor con 3 peluqueros y caja
 * Los peluqueros duermen (wait) si la cola de sofás está vacía
 * Los clientes que llegan (a sofás) les despiertan (notify)
 * Los peluqueros asistentes cobran
 */
public class Peluqueria {

    private final int MAX_SOFAS = 5;
    private final int MAX_BAR = 15;

    // Colas de espera
    private Queue<Cliente> clientesEnSofa = new LinkedList<>();
    private Queue<Cliente> clientesEnBar = new LinkedList<>();
    // Cola para cobrar (clientes esperando en la silla a que la caja se libere)
    private Queue<Cliente> clientesParaCobrar = new LinkedList<>();
    private boolean cajaOcupada = false;

    // ENUM para saber qué tarea toca
    public enum TipoTrabajo { CORTE, COBRO }
    
    // Clase auxiliar para devolver el trabajo y el cliente asociado
    public class Tarea {
        public TipoTrabajo tipo;
        public Cliente cliente;
        public Tarea(TipoTrabajo t, Cliente c) { this.tipo = t; this.cliente = c; }
    }

    // --- MÉTODOS DE ENTRADA (Igual que antes) ---
    public synchronized boolean clienteLlega(Cliente cliente) {
        if (clientesEnSofa.size() == MAX_SOFAS && clientesEnBar.size() == MAX_BAR) {
            return false;
        }
        if (clientesEnSofa.size() < MAX_SOFAS) {
            clientesEnSofa.offer(cliente);
            System.out.println("Cliente " + cliente.getId() + " al SOFÁ.");
            notifyAll(); // Avisamos a cualquiera (Sir Patrick o Ayudantes)
        } else {
            clientesEnBar.offer(cliente);
            System.out.println("Cliente " + cliente.getId() + " al BAR.");
        }
        return true;
    }

    // --- Lógica de distribución de trabajo ---
    public synchronized Tarea obtenerTrabajo(boolean esSirPatrick) throws InterruptedException {
        while (true) {
            // 1. Si soy Ayudante, miro PRIMERO si hay alguien para cobrar
            if (!esSirPatrick && !clientesParaCobrar.isEmpty()) {
                Cliente c = clientesParaCobrar.poll();
                return new Tarea(TipoTrabajo.COBRO, c);
            }

            // 2. Si no hay cobros (o soy Sir Patrick), miro si hay cortes
            if (!clientesEnSofa.isEmpty()) {
                Cliente c = clientesEnSofa.poll();
                
                // Mover del Bar al Sofá
                if (!clientesEnBar.isEmpty()) {
                    Cliente sube = clientesEnBar.poll();
                    clientesEnSofa.offer(sube);
                    System.out.println("--- Cliente " + sube.getId() + " pasa de Bar a Sofá.");
                }
                return new Tarea(TipoTrabajo.CORTE, c);
            }

            // 3. Si no hay nada que hacer, a dormir
            System.out.println(Thread.currentThread().getName() + " duerme...");
            wait();
        }
    }

    // Flujo del Cliente para pagar ---
    
    // El cliente llama a esto cuando termina el corte
    public synchronized void irACaja(Cliente c) throws InterruptedException {
        // "No se levantarán de su silla hasta que vean que la caja está vacía"
        while (cajaOcupada) {
            wait();
        }
        
        // La caja está libre, voy
        cajaOcupada = true;
        clientesParaCobrar.offer(c);
        System.out.println("Cliente " + c.getId() + " va a la CAJA.");
        
        notifyAll(); // Despierta a los Ayudantes para que vengan a cobrar
    }

    // El cliente se va después de pagar
    public synchronized void clienteSaleTienda(Cliente c) {
        cajaOcupada = false; // Libera la caja
        System.out.println("Cliente " + c.getId() + " sale de la tienda (Pagado).");
        notifyAll(); // Avisa a otros clientes esperando en silla para pagar
    }
}