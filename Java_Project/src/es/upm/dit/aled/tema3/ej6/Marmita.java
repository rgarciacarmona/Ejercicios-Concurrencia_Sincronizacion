package es.upm.dit.aled.tema3.ej6;

public class Marmita {
    private int raciones;
    private final int CAPACIDAD_MAX;
    private boolean cocineroAvisado = false;

    public Marmita(int capacidad) {
        this.CAPACIDAD_MAX = capacidad;
        this.raciones = capacidad; // Empieza llena
    }

    public synchronized void comer(String nombreCanibal) throws InterruptedException {
        // Si la marmita está vacía, avisar al cocinero y esperar
        while (raciones == 0) {
            if (!cocineroAvisado) {
                System.out.println("!!! " + nombreCanibal + " ve la marmita vacía y AVISA al cocinero.");
                cocineroAvisado = true;
                notifyAll(); // Despertamos al cocinero
            }
            System.out.println("... " + nombreCanibal + " espera comida.");
            wait();
        }

        // Comer
        raciones--;
        System.out.println("--- " + nombreCanibal + " come. Raciones restantes: " + raciones);
        
        // Si soy el último en comer y dejo la marmita a 0, el siguiente avisará
        // No es estrictamente necesario notificar aquí, salvo para otros caníbales
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