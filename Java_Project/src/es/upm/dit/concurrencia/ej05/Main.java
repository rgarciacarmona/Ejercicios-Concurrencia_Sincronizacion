package es.upm.dit.concurrencia.ej05;

public class Main {
    public static void main(String[] args) {
        int numFilosofos = 5; // Configuración estándar del problema
        MesaFilosofos mesa = new MesaFilosofos(numFilosofos);
        
        Filosofo[] filosofos = new Filosofo[numFilosofos];

        System.out.println("--- Comienza la cena de los filósofos ---");
        
        for (int i = 0; i < numFilosofos; i++) {
            filosofos[i] = new Filosofo(i, mesa);
            filosofos[i].start();
        }
    }
}