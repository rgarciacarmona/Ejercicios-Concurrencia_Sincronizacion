// HebraEscritora
package es.upm.dit.aled.tema3.ej3;

public class HebraEscritora extends Thread {
    private RecursoCompartido recurso;
    private String nombre;
    private Prioridad prioridad;

    public HebraEscritora(RecursoCompartido r, String n, Prioridad p) {
        this.recurso = r;
        this.nombre = n;
        this.prioridad = p;
    }

    @Override
    public void run() {
        try {
            recurso.escribir(nombre, prioridad);
        } catch (InterruptedException e) {}
    }
}