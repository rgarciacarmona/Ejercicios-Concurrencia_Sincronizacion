package es.upm.dit.aled.tema3.ej6;

public class Main {
    public static void main(String[] args) {
        Marmita marmita = new Marmita(5); // Marmita de 5 raciones

        // Hebra Cocinero
        new Thread(() -> {
            try {
                while (true) {
                    marmita.rellenar();
                }
            } catch (InterruptedException e) {}
        }, "Cocinero").start();

        // Hebras Caníbales
        for (int i = 1; i <= 4; i++) {
            new Thread(() -> {
                try {
                    while (true) {
                        marmita.comer(Thread.currentThread().getName());
                        // Tiempo para digerir antes de volver a tener hambre
                        Thread.sleep((long) (Math.random() * 2000));
                    }
                } catch (InterruptedException e) {}
            }, "Canibal-" + i).start();
        }
    }
}