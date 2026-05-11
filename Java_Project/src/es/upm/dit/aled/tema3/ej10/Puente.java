package es.upm.dit.aled.tema3.ej10;

public class Puente {
    // Constantes
    private final int MAX_PESO = 15000;
    private final int MAX_VEHICULOS = 10;

    // Estado actual
    private int pesoActual = 0;
    private int vehiculosActuales = 0;

    // Control de prioridad
    private int ambulanciasEsperando = 0;

    /**
     * Solicitar entrada al puente
     */
    public synchronized void entrarPuente(int peso, boolean esAmbulancia) throws InterruptedException {
        // Si soy ambulancia, me registro para que los coches normales me cedan el paso
        if (esAmbulancia) {
            ambulanciasEsperando++;
            System.out.println("+++ AMBULANCIA llegando (Peso: " + peso + "). Prioridad solicitada.");
        } else {
            System.out.println("... Coche normal llegando (Peso: " + peso + ").");
        }

        try {
            // CONDICIÓN DE ESPERA
            while (!puedoEntrar(peso, esAmbulancia)) {
                wait();
            }

            // Entrar (actualizar estado)
            pesoActual += peso;
            vehiculosActuales++;
            
            String tipo = esAmbulancia ? "AMBULANCIA" : "Coche";
            System.out.println(">>> " + tipo + " entra. Puente: [Vehic: " + vehiculosActuales + 
                               "/10, Peso: " + pesoActual + "/15000]");
            
        } finally {
            // Si era ambulancia y ya he conseguido entrar (o salgo por excepción), dejo de esperar
            if (esAmbulancia) {
                ambulanciasEsperando--;
            }
        }
    }

    /**
     * Lógica de seguridad y prioridad
     */
    private boolean puedoEntrar(int peso, boolean esAmbulancia) {
        // 1. Comprobar límites físicos
        if (vehiculosActuales >= MAX_VEHICULOS) return false;
        if (pesoActual + peso > MAX_PESO) return false;

        // 2. Comprobar prioridades (Solo afecta a coches normales)
        // Si NO soy ambulancia Y hay ambulancias esperando, NO puedo entrar
        if (!esAmbulancia && ambulanciasEsperando > 0) {
            return false; 
        }

        return true;
    }

    /**
     * Salir del puente
     */
    public synchronized void salirPuente(int peso) {
        pesoActual -= peso;
        vehiculosActuales--;
        
        System.out.println("<<< Un vehículo sale. Liberado " + peso + "kg.");
        notifyAll(); // Avisar a todos para que reevalúen
    }
}