package es.upm.dit.aled.tema3.ej3;

public class RecursoCompartido {

    private Integer entero = null;
    private int numLectores = 0;
    private boolean escribiendo = false;

    // Contadores de hebras esperando por prioridad
    // Indices: 0=BAJA, 1=MEDIA, 2=ALTA (coincide con el ordinal del Enum)
    private int[] lectoresEsperando = {0, 0, 0};
    private int[] escritoresEsperando = {0, 0, 0};

    /**
     * Lectura con prioridad
     */
    public void leer(String nombre, Prioridad p) throws InterruptedException {
        accesoLectura(nombre, p);
        
        // Sección crítica
        System.out.println("!!! Lector " + nombre + " (" + p + ") leyendo. Valor: " + entero);
        Thread.sleep((long) (Math.random() * 500));
        
        terminaLectura(nombre, p);
    }

    private synchronized void accesoLectura(String nombre, Prioridad p) throws InterruptedException {
        // Registrar que estamos esperando
        lectoresEsperando[p.ordinal()]++;

        try {
            // CONDICIÓN DE ESPERA:
            // 1. Si alguien está escribiendo
            // 2. Si el entero es null (no inicializado)
            // 3. PRIORIDAD: Si hay ESCRITORES de MAYOR prioridad esperando
            //    (Nota: No nos importa si hay lectores de mayor prioridad, porque podemos compartir)
            while (escribiendo || entero == null || hayEscritoresMayorPrioridad(p)) {
                System.out.println("... Lector " + nombre + " (" + p + ") espera.");
                wait();
            }
        } finally {
            // Ya no estamos esperando (o hemos entrado o ha saltado excepción)
            lectoresEsperando[p.ordinal()]--;
        }

        numLectores++;
        System.out.println(">>> Lector " + nombre + " (" + p + ") entra.");
    }

    private synchronized void terminaLectura(String nombre, Prioridad p) {
        numLectores--;
        if (numLectores == 0) {
            notifyAll(); // Despertamos a todos para que reevalúen condiciones
        }
        System.out.println("<<< Lector " + nombre + " (" + p + ") sale.");
    }

    /**
     * Escritura con prioridad
     */
    public void escribir(String nombre, Prioridad p) throws InterruptedException {
        accesoEscritura(nombre, p);
        
        // Sección crítica
        entero = (int) (Math.random() * 10000);
        System.out.println("!!! Escritor " + nombre + " (" + p + ") escribe nuevo valor: " + entero);
        Thread.sleep((long) (Math.random() * 500));
        
        terminaEscritura(nombre, p);
    }

    private synchronized void accesoEscritura(String nombre, Prioridad p) throws InterruptedException {
        escritoresEsperando[p.ordinal()]++;

        try {
            // CONDICIÓN DE ESPERA:
            // 1. Si hay lectores dentro
            // 2. Si hay otro escritor dentro
            // 3. PRIORIDAD A: Si hay CUALQUIER hebra (L o E) de MAYOR prioridad esperando
            // 4. PRIORIDAD B: Si hay LECTORES de MI MISMA prioridad (Los lectores ganan empates)
            while (numLectores > 0 || escribiendo || 
                   hayAlguienMayorPrioridad(p) || 
                   (lectoresEsperando[p.ordinal()] > 0)) { 
                   
                System.out.println("... Escritor " + nombre + " (" + p + ") espera.");
                wait();
            }
        } finally {
            escritoresEsperando[p.ordinal()]--;
        }

        escribiendo = true;
        System.out.println(">>> Escritor " + nombre + " (" + p + ") entra.");
    }

    private synchronized void terminaEscritura(String nombre, Prioridad p) {
        escribiendo = false;
        notifyAll();
        System.out.println("<<< Escritor " + nombre + " (" + p + ") sale.");
    }

    // --- Métodos auxiliares de prioridad ---

    // Devuelve true si hay algún escritor con prioridad estricta mayor que 'p'
    private boolean hayEscritoresMayorPrioridad(Prioridad p) {
        for (int i = p.ordinal() + 1; i < 3; i++) {
            if (escritoresEsperando[i] > 0) return true;
        }
        return false;
    }

    // Devuelve true si hay cualquier hebra (L o E) con prioridad estricta mayor que 'p'
    private boolean hayAlguienMayorPrioridad(Prioridad p) {
        for (int i = p.ordinal() + 1; i < 3; i++) {
            if (lectoresEsperando[i] > 0 || escritoresEsperando[i] > 0) return true;
        }
        return false;
    }
}