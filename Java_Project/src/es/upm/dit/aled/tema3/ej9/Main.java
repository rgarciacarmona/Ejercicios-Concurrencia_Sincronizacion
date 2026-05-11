package es.upm.dit.aled.tema3.ej9;

public class Main {
    public static void main(String[] args) {
        Delegacion delegacion = new Delegacion();

        // Hebra Andrés
        new Thread(() -> {
            try {
                while (true) {
                    delegacion.atenderCiudadano();
                }
            } catch (InterruptedException e) {}
        }, "Andrés").start();

        // Generador de Ciudadanos
        for (int i = 0; i < 20; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    // Elige ventanilla al azar
                    if (Math.random() < 0.5) {
                        delegacion.esperarVentanilla1("C" + id);
                    } else {
                        delegacion.esperarVentanilla2("C" + id);
                    }
                } catch (InterruptedException e) {}
            }).start();

            try { Thread.sleep(200); } catch (InterruptedException e) {}
        }
    }
}