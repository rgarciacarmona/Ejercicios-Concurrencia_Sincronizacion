package es.upm.dit.concurrencia.ej06;

public class Marmita {
    private int raciones;
    private final int CAPACIDAD_MAX;
    private boolean cocineroAvisado = false;
    private String canibalQueAviso = null;

    public Marmita(int capacidad) {
        this.CAPACIDAD_MAX = capacidad;
        this.raciones = capacidad; // Empieza llena
    }

    public synchronized void comer(String nombreCanibal) throws InterruptedException {
    	// Un caníbal espera si la marmita está vacía O si no es el caníbal que avisó (estando el puesto reservado)
        while (raciones == 0 || (canibalQueAviso != null && !canibalQueAviso.equals(nombreCanibal))) {
            // Si la marmita está vacía y nadie ha avisado aún, este caníbal avisa
            if (raciones == 0 && !cocineroAvisado) {
                System.out.println("!!! " + nombreCanibal + " ve la marmita vacía y AVISA al cocinero.");
                cocineroAvisado = true;
                canibalQueAviso = nombreCanibal;
                notifyAll(); // Despertamos al cocinero
            }
            System.out.println("... " + nombreCanibal + " espera comida.");
            wait();
        }

        // Si el caníbal que va a comer es el que avisó, se limpia la prioridad
        if (nombreCanibal.equals(canibalQueAviso)) {
            canibalQueAviso = null;
        }
        
        // Comer
        raciones--;
        System.out.println("--- " + nombreCanibal + " come. Raciones restantes: " + raciones);
        
        // ¡Importante! Notificamos a los demás porque el caníbal prioritario ya liberó el turno
        // o por si quedan raciones para los que estaban esperando.
        notifyAll();
    }

    public synchronized void rellenar() throws InterruptedException {
        // El cocinero espera mientras haya comida o no le hayan avisado
        while (raciones > 0 || !cocineroAvisado) {
            wait();
        }

        System.out.println(">>> COCINERO cocinando estofado...");
        // Simulación tiempo de cocina (fuera del bloqueo si quisiéramos concurrencia real, 
        // pero dentro para simplificar consistencia en este modelo simple)
        Thread.sleep(1000); 
        
        raciones = CAPACIDAD_MAX;
        cocineroAvisado = false;
        System.out.println(">>> COCINERO ha rellenado la marmita (" + CAPACIDAD_MAX + " raciones).");
        
        notifyAll(); // Avisamos a los caníbales hambrientos
    }
}