package es.upm.dit.aled.tema3.ej2;

public class HebraEscritora extends Thread {
    private final RecursoCompartido recurso1;
    private final RecursoCompartido recurso2;
    private final String nombre;

    public HebraEscritora(RecursoCompartido r1, RecursoCompartido r2, String nombre) {
        this.recurso1 = r1;
        this.recurso2 = r2;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            // Decidir qué recurso escribir al azar
            if (Math.random() < 0.5) {
                System.out.println(nombre + " decide escribir en Recurso 1");
                recurso1.escribir(nombre);
            } else {
                System.out.println(nombre + " decide escribir en Recurso 2");
                recurso2.escribir(nombre);
            }
        } catch (InterruptedException e) {}
    }
}