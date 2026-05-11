package es.upm.dit.aled.tema3.ej7;

public class Mesa {
    // Estado de los ingredientes en la mesa
    private boolean tabaco = false;
    private boolean papel = false;
    private boolean cerilla = false;
    private boolean mesaOcupada = false;

    /**
     * El estanquero pone dos ingredientes.
     * tipo1 y tipo2 pueden ser: 0=TABACO, 1=PAPEL, 2=CERILLA
     */
    public synchronized void ponerIngredientes(int tipo1, int tipo2) throws InterruptedException {
        while (mesaOcupada) {
            wait();
        }

        // Colocar ingredientes
        if (tipo1 == 0 || tipo2 == 0) tabaco = true;
        if (tipo1 == 1 || tipo2 == 1) papel = true;
        if (tipo1 == 2 || tipo2 == 2) cerilla = true;

        mesaOcupada = true;
        System.out.println(">>> Estanquero pone: " + verIngredientes());
        notifyAll(); // Avisar a los fumadores
    }

    /**
     * El fumador intenta coger los ingredientes que le faltan.
     * ingredienteQueTengo: 0=TABACO, 1=PAPEL, 2=CERILLA
     */
    public synchronized void cogerIngredientes(int ingredienteQueTengo) throws InterruptedException {
        // Condición: Deben estar en la mesa los dos ingredientes que NO tengo
        // Ejemplo: Si tengo TABACO (0), necesito PAPEL (1) y CERILLA (2)
        while (!puedoFumar(ingredienteQueTengo)) {
            wait();
        }

        // Coger ingredientes (limpiar mesa)
        tabaco = false;
        papel = false;
        cerilla = false;
        mesaOcupada = false;
        
        System.out.println("--- Fumador con " + nombreIngrediente(ingredienteQueTengo) + " recoge ingredientes y fuma.");
        notifyAll(); // Avisar al estanquero
    }

    // Auxiliar para verificar si están los ingredientes complementarios
    private boolean puedoFumar(int tengo) {
        if (!mesaOcupada) return false;
        if (tengo == 0) return papel && cerilla;   // Tengo Tabaco -> Quiero P+C
        if (tengo == 1) return tabaco && cerilla;  // Tengo Papel -> Quiero T+C
        if (tengo == 2) return tabaco && papel;    // Tengo Cerilla -> Quiero T+P
        return false;
    }
    
    private String nombreIngrediente(int i) {
        return i==0 ? "TABACO" : (i==1 ? "PAPEL" : "CERILLA");
    }

    private String verIngredientes() {
        String s = "[ ";
        if (tabaco) s += "TABACO ";
        if (papel) s += "PAPEL ";
        if (cerilla) s += "CERILLA ";
        return s + "]";
    }
}