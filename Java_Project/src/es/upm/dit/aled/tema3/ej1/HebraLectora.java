package es.upm.dit.aled.tema3.ej1;

/**
 * Ejercicio 1: Hebra lectora
 */
public class HebraLectora extends Thread {
    private final RecursoCompartido recurso;
    private final String nombre;

    public HebraLectora(RecursoCompartido recurso, String nombre) {
        this.recurso = recurso;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            // Intenta leer del recurso
            recurso.leer(nombre);
        } catch (InterruptedException e) {}
    }
}