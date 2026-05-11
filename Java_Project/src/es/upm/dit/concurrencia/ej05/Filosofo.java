package es.upm.dit.concurrencia.ej05;

import java.util.Random;

public class Filosofo extends Thread {
    private int id;
    private MesaFilosofos mesa;
    private Random random = new Random();

    public Filosofo(int id, MesaFilosofos mesa) {
        this.id = id;
        this.mesa = mesa;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 1. Pensar: El filósofo dedica un tiempo a meditar
                System.out.println("Filósofo " + id + " está pensando...");
                Thread.sleep(random.nextInt(1000));

                // 2. Hambre: Intenta coger los tenedores
                System.out.println("Filósofo " + id + " tiene hambre y busca tenedores.");
                mesa.cogerTenedores(id);

                // 3. Comer: Una vez tiene ambos, come durante un tiempo
                Thread.sleep(random.nextInt(1000));

                // 4. Terminar: Deja los tenedores en la mesa
                mesa.dejarTenedores(id);
            }
        } catch (InterruptedException e) {
            System.err.println("Filósofo " + id + " fue interrumpido.");
            Thread.currentThread().interrupt();
        }
    }
}