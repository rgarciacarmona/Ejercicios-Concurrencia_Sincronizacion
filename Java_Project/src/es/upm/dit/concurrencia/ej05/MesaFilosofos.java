package es.upm.dit.concurrencia.ej05;

public class MesaFilosofos {
    private boolean[] tenedores;

    public MesaFilosofos(int numFilosofos) {
        this.tenedores = new boolean[numFilosofos]; // false = libre, true = ocupado
    }

    /**
     * Intenta coger dos tenedores.
     * Para evitar el interbloqueo, se debe asegurar un orden en la toma de recursos.
     */
    public synchronized void cogerTenedores(int id) throws InterruptedException {
        int t1 = id;                         // Tenedor a la derecha
        int t2 = (id + 1) % tenedores.length; // Tenedor a la izquierda

        // Ordenamos los índices: primero el menor, luego el mayor
        int primero = Math.min(t1, t2);
        int segundo = Math.max(t1, t2);

        // Intentar coger el primer tenedor
        while (tenedores[primero]) {
            wait();
        }
        tenedores[primero] = true;

        // Intentar coger el segundo tenedor
        while (tenedores[segundo]) {
            wait();
        }
        tenedores[segundo] = true;
        
        System.out.println("Filósofo " + id + " tiene ambos tenedores y empieza a comer.");
    }

    public synchronized void dejarTenedores(int id) {
        int t1 = id;
        int t2 = (id + 1) % tenedores.length;

        tenedores[t1] = false;
        tenedores[t2] = false;

        System.out.println("Filósofo " + id + " ha terminado de comer y deja los tenedores.");
        notifyAll(); // Despierta a los vecinos que esperan tenedores
    }
}