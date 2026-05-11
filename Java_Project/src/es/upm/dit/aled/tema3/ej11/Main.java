package es.upm.dit.aled.tema3.ej11;

public class Main {
    public static void main(String[] args) {
        Fabrica fabrica = new Fabrica();

        // Hebra Línea Roja
        new Thread(() -> {
            try {
                while (true) {
                    fabrica.producirRoja();
                    Thread.sleep(100); // Tiempo de fabricación
                }
            } catch (InterruptedException e) {}
        }, "LineaRoja").start();

        // Hebra Línea Azul
        new Thread(() -> {
            try {
                while (true) {
                    fabrica.producirAzul();
                    Thread.sleep(150); // Quizás tarda distinto
                }
            } catch (InterruptedException e) {}
        }, "LineaAzul").start();

        // Gestores de Pedidos (Varios simulando pedidos aleatorios)
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                try {
                    while (true) {
                        // Generar pedido aleatorio (ej. entre 1 y 10 piezas de cada)
                        int rojas = 1 + (int)(Math.random() * 10);
                        int azules = 1 + (int)(Math.random() * 10);
                        
                        fabrica.pedirPiezas(Thread.currentThread().getName(), rojas, azules);
                        
                        Thread.sleep(1000); // Tiempo procesando el pedido
                    }
                } catch (InterruptedException e) {}
            }, "Gestor-" + i).start();
        }
    }
}