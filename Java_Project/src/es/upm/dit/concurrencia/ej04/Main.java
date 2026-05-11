package es.upm.dit.concurrencia.ej04;

public class Main {
	public static void main(String[] args) {
		// Variables
		int productores = 3;
		int consumidores = 2;
		
        // Creamos el recurso compartido (Monitor)
        BufferCola monitor = new BufferCola();

        // 1. Lanzamos hebras productoras
        // Cada una intentará producir 5 números aleatorios
        for (int i = 1; i <= productores; i++) {
            final int idProductor = i;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 5; j++) {
                        int dato = (int) (Math.random() * 10000); // Entre 0 y 10.000
                        monitor.producir(dato);
                        // Pequeño retardo para que la salida por consola sea legible
                        Thread.sleep((long) (Math.random() * 300));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Productor-" + idProductor).start();
        }

        // 2. Lanzamos hebras consumidoras
        // Cada una intentará consumir 7 números (en total 14, dejando 1 para el final)
        for (int i = 1; i <= consumidores; i++) {
            final int idConsumidor = i;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 7; j++) {
                        int dato = monitor.consumir();
                        // Al extraerlo, lo imprimimos por pantalla
                        System.out.println("LOG: " + Thread.currentThread().getName() + " procesó el dato: " + dato);
                        
                        // El consumidor suele ser algo más lento o rápido dependiendo de la lógica
                        Thread.sleep((long) (Math.random() * 600));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumidor-" + idConsumidor).start();
        }
    }
}