package es.upm.dit.aled.tema3.ej5;

public class Main {
    public static void main(String[] args) {
        Almacen almacen = new Almacen();

        // 2 Productores
        for (int i = 1; i <= 2; i++) {
            new Thread(() -> {
                try {
                    while (true) {
                        almacen.almacenar(Thread.currentThread().getName());
                        Thread.sleep((long)(Math.random() * 300));
                    }
                } catch (InterruptedException e) {}
            }, "Prod-" + i).start();
        }

        // 3 Consumidores
        for (int i = 1; i <= 3; i++) {
            new Thread(() -> {
                try {
                    while (true) {
                        almacen.extraer(Thread.currentThread().getName());
                        Thread.sleep((long)(Math.random() * 500));
                    }
                } catch (InterruptedException e) {}
            }, "Cons-" + i).start();
        }
    }
}