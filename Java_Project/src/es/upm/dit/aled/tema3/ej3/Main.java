package es.upm.dit.aled.tema3.ej3;

public class Main {
    public static void main(String[] args) {
        RecursoCompartido recurso = new RecursoCompartido();
        Prioridad[] prioridades = Prioridad.values();

        for (int i = 0; i < 20; i++) {
            // Asignar prioridad aleatoria
            Prioridad p = prioridades[(int)(Math.random() * 3)];
            
            if (i % 5 == 0) {
                Prioridad pE = prioridades[(int)(Math.random() * 3)];
                Thread t2 = new HebraEscritora(recurso, "E" + i, pE);
                t2.start();
            }
            
            Thread t = new HebraLectora(recurso, "L" + i, p);
            t.start();
        }
    }
}