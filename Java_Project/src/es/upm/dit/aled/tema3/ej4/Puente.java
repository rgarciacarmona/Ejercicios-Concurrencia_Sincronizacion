package es.upm.dit.aled.tema3.ej4;

public class Puente {

    // Constantes
    private static final int MAX_COCHES = 3;

    // Estado del puente
    private int cochesEnPuente = 0;
    private String direccionActual = "NADIE"; // "NORTE_SUR", "SUR_NORTE" o "NADIE"

    /**
     * Un coche intenta entrar desde el Norte hacia el Sur
     */
    public synchronized void entrarNorteSur(String matricula) throws InterruptedException {
        // Espera si:
        // 1. El puente está lleno.
        // 2. O hay coches yendo en dirección contraria (Sur -> Norte).
        while (cochesEnPuente == MAX_COCHES || 
              (cochesEnPuente > 0 && direccionActual.equals("SUR_NORTE"))) {
            System.out.println("... Coche " + matricula + " (N->S) esperando.");
            wait();
        }

        // Entrar
        cochesEnPuente++;
        direccionActual = "NORTE_SUR"; // Establecemos/Confirmamos dirección
        System.out.println(">>> Coche " + matricula + " entra (N->S). Puente: " + cochesEnPuente);
    }

    /**
     * Un coche intenta entrar desde el Sur hacia el Norte
     */
    public synchronized void entrarSurNorte(String matricula) throws InterruptedException {
        // Espera si:
        // 1. El puente está lleno.
        // 2. O hay coches yendo en dirección contraria (Norte -> Sur).
        while (cochesEnPuente == MAX_COCHES || 
              (cochesEnPuente > 0 && direccionActual.equals("NORTE_SUR"))) {
            System.out.println("... Coche " + matricula + " (S->N) esperando.");
            wait();
        }

        // Entrar
        cochesEnPuente++;
        direccionActual = "SUR_NORTE"; 
        System.out.println(">>> Coche " + matricula + " entra (S->N). Puente: " + cochesEnPuente);
    }

    /**
     * Salir del puente (independiente de la dirección)
     */
    public synchronized void salir(String matricula) {
        cochesEnPuente--;
        System.out.println("<<< Coche " + matricula + " sale. Puente: " + cochesEnPuente);

        // Si el puente se vacía, reseteamos la dirección para que cualquiera pueda entrar
        if (cochesEnPuente == 0) {
            direccionActual = "NADIE";
            System.out.println("--- Puente vacío, cambio de dirección posible ---");
        }
        
        notifyAll(); // Notificamos a todos para que reevalúen sus condiciones
    }
}