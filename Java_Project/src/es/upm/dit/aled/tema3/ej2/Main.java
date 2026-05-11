package es.upm.dit.aled.tema3.ej2;



public class Main {
    private static final int NUM_LECTORAS = 300;
    private static final int RATIO_LECTORAS_ESCRITORAS = 10;

    public static void main(String[] args) {
        // Instanciamos dos recursos independientes
        RecursoCompartido recurso1 = new RecursoCompartido();
        RecursoCompartido recurso2 = new RecursoCompartido();

        System.out.println("EIniciando hebras para dos recursos...");

        try {
            for (int i = 0; i < NUM_LECTORAS; i++) {
                // Pasamos ambos recursos a la lectora
                Thread t = new HebraLectora(recurso1, recurso2, "L" + i);
                t.start();

                if (i % RATIO_LECTORAS_ESCRITORAS == 0) {
                    // Pasamos ambos recursos a la escritora
                    Thread t2 = new HebraEscritora(recurso1, recurso2, "E" + i);
                    t2.start();
                }
                // Esperar hasta 200 ms entre creación de hebras.
                Thread.sleep((long) (Math.random() * 200));
            }
        } catch (InterruptedException e) {}
    }
}