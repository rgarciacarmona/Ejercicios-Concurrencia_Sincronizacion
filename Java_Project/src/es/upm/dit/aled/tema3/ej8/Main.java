package es.upm.dit.aled.tema3.ej8;

public class Main {
    public static void main(String[] args) {
        MesaSolidaria mesa = new MesaSolidaria();

        // Estanquero (pone 1 recurso)
        new Thread(() -> {
            try {
                while (true) {
                    int recurso = (int)(Math.random() * 3);
                    mesa.ponerIngrediente(recurso);
                }
            } catch (InterruptedException e) {}
        }, "Estanquero").start();

        // 3 Fumadores
        for (int i = 0; i < 3; i++) {
            final int miRecurso = i;
            new Thread(() -> {
                try {
                    while (true) {
                        mesa.intentarFumar(miRecurso);
                        Thread.sleep((long)(Math.random() * 500));
                    }
                } catch (InterruptedException e) {}
            }, "Fumador-" + i).start();
        }
    }
}