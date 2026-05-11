package es.upm.dit.aled.tema3.ej2;

/**
 * Ejercicio 1: Monitor
 *
 * Gestiona el acceso concurrente a un recurso (un Integer).
 * El recurso se considera no inicializado si es 'null'.
 */
public class RecursoCompartido {

	// Recurso compartido
	private Integer entero = null; 
    
    private int numLectores = 0;
    private boolean escribiendo = false;

    /**
     * Lectura.
     */
    public void leer(String nombreLector) throws InterruptedException {
        // 1. Adquirir permiso de lectura
        this.accesoLectura(nombreLector);
        // 2. Acción de lectura (Fuera de sincronización) 
        Integer valorLeido = this.entero; // Se lee la referencia al objeto
        System.out.println("!!! Lector " + nombreLector + " está leyendo. Valor = " + valorLeido);
        // Espera hasta 500 ms
        Thread.sleep((long) (Math.random() * 500));
        // 3. Liberar permiso de lectura
        this.terminaLectura(nombreLector);        
    }
    
    private synchronized void accesoLectura(String nombreLector) throws InterruptedException {
    	// Si el entero es null debe esperar
        while (escribiendo || this.entero == null) { 
            System.out.println("... Lector " + nombreLector + " espera para leer.");
            wait();
        }
        numLectores++;
        System.out.println(">>> Lector " + nombreLector + " ha empezado a leer.");
    }
    
    private synchronized void terminaLectura(String nombreLector) throws InterruptedException {
    	numLectores--;
        if (numLectores == 0) {
            notifyAll();
        }
        System.out.println("<<< Lector " + nombreLector + " ha terminado de leer.");
    }

    /**
     * Escritura.
     */
    public void escribir(String nombreEscritor) throws InterruptedException {
        // 1. Adquirir permiso de escritura
        this.accesoEscritura(nombreEscritor);
        // 2. Acción de escritura
        Integer valorAntiguo = this.entero;
        // El entero se genera aleatoriamente
        this.entero = (int) (Math.random() * 10000); 
        System.out.println("!!! Escritor " + nombreEscritor + " escribe. Valor antiguo = " + String.valueOf(valorAntiguo) + ", nuevo = " + this.entero);
        // Espera hasta 500 ms
        Thread.sleep((long) (Math.random() * 500));
        // 3. Liberar permiso de escritura
        this.terminaEscritura(nombreEscritor);
    }
    
    private synchronized void accesoEscritura(String nombreEscritor) throws InterruptedException {
    	while (numLectores > 0 || escribiendo) {
    		System.out.println("... Escritor " + nombreEscritor + " espera para escribir.");
            wait();
        }
        escribiendo = true;
        System.out.println(">>> Escritor " + nombreEscritor + " ha empezado a escribir.");
    }
    
    private synchronized void terminaEscritura(String nombreEscritor) throws InterruptedException {
    	escribiendo = false;
    	notifyAll();
    	System.out.println("<<< Escritor " + nombreEscritor + " ha terminado de escribir.");
    }
}