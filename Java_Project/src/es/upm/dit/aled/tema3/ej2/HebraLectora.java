package es.upm.dit.aled.tema3.ej2;

public class HebraLectora extends Thread {
    private final RecursoCompartido recurso1;
    private final RecursoCompartido recurso2;
    private final String nombre;

    public HebraLectora(RecursoCompartido r1, RecursoCompartido r2, String nombre) {
        this.recurso1 = r1;
        this.recurso2 = r2;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            // Decidir el orden de lectura al azar
            if (Math.random() < 0.5) {
                System.out.println(nombre + " decide leer Recurso 1 y luego Recurso 2");
                recurso1.leer(nombre + "(R1)");
                recurso2.leer(nombre + "(R2)");
            } else {
                System.out.println(nombre + " decide leer Recurso 2 y luego Recurso 1");
                recurso2.leer(nombre + "(R2)");
                recurso1.leer(nombre + "(R1)");
            }
        } catch (InterruptedException e) {}
    }
}