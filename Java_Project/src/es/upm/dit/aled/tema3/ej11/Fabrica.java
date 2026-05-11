package es.upm.dit.aled.tema3.ej11;

public class Fabrica {
    // Stock actual
    private int piezasRojas = 0;
    private int piezasAzules = 0;
    
    // Capacidad máxima de las cestas
    private final int CAPACIDAD_CESTA = 50;

    /**
     * Método de la Línea Roja
     */
    public synchronized void producirRoja() throws InterruptedException {
        // Esperar si la cesta de rojas está llena
        while (piezasRojas >= CAPACIDAD_CESTA) {
            wait();
        }
        piezasRojas++;
        System.out.println("+++ Línea Roja produce. Stock Rojo: " + piezasRojas);
        notifyAll(); // Avisar a los gestores de pedidos
    }

    /**
     * Método de la Línea Azul
     */
    public synchronized void producirAzul() throws InterruptedException {
        // Esperar si la cesta de azules está llena
        while (piezasAzules >= CAPACIDAD_CESTA) {
            wait();
        }
        piezasAzules++;
        System.out.println("+++ Línea Azul produce. Stock Azul: " + piezasAzules);
        notifyAll(); // Avisar a los gestores de pedidos
    }

    /**
     * Método del Gestor de Pedidos
     * Debe ser atómico: o coge todas o espera.
     */
    public synchronized void pedirPiezas(String idPedido, int cantRojas, int cantAzules) throws InterruptedException {
        System.out.println("... Pedido " + idPedido + " solicita [R:" + cantRojas + ", A:" + cantAzules + "]");

        // Espera MIENTRAS no haya suficientes rojas O no haya suficientes azules
        while (piezasRojas < cantRojas || piezasAzules < cantAzules) {
            wait();
        }

        // Sección Crítica: Hay stock suficiente de ambas, las cogemos
        piezasRojas -= cantRojas;
        piezasAzules -= cantAzules;

        System.out.println("--- Pedido " + idPedido + " COMPLETADO. Quedan [R:" + piezasRojas + ", A:" + piezasAzules + "]");
        
        notifyAll(); // Avisar a las líneas de producción (ahora hay hueco)
    }
}