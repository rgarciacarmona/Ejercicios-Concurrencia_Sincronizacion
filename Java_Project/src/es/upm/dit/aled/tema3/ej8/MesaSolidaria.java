package es.upm.dit.aled.tema3.ej8;

public class MesaSolidaria {
    private Integer ingredienteMesa = null; // null, 0, 1, 2
    
    // Estado de petición de ayuda
    private boolean pidiendoAyuda = false;
    private Integer ingredienteSolicitado = null; // Qué necesita el que pide ayuda
    private boolean ayudaConcedida = false; // Flag para sincronizar el intercambio

    /**
     * El estanquero pone 1 ingrediente
     */
    public synchronized void ponerIngrediente(int ingrediente) throws InterruptedException {
        while (ingredienteMesa != null || pidiendoAyuda) {
            wait();
        }
        ingredienteMesa = ingrediente;
        System.out.println(">>> Estanquero pone: " + nombre(ingrediente));
        notifyAll();
    }

    /**
     * El fumador intenta fumar.
     * miRecurso: El que tengo infinito.
     */
    public synchronized void intentarFumar(int miRecurso) throws InterruptedException {
        
        // LÓGICA DE AYUDA (SOLIDARIDAD)
        // Antes de intentar fumar, verifico si alguien está pidiendo MI recurso
        while (pidiendoAyuda && ingredienteSolicitado == miRecurso && !ayudaConcedida) {
            System.out.println("!!! Fumador (" + nombre(miRecurso) + ") PRESTA ayuda solidaria.");
            ayudaConcedida = true;
            notifyAll();
            // Sigo con mi vida (volveré a intentar fumar o esperar)
        }
        
        // LÓGICA DE FUMAR
        // Puedo coger el de la mesa si:
        // 1. Hay algo en la mesa.
        // 2. No es lo mismo que ya tengo.
        // 3. Nadie está pidiendo ayuda todavía (mesa libre de conflictos).
        while (ingredienteMesa == null || ingredienteMesa == miRecurso || pidiendoAyuda) {
            
            // IMPORTANTE: Si estoy esperando y alguien pide mi ayuda, debo despertar y ayudar
            if (pidiendoAyuda && ingredienteSolicitado == miRecurso && !ayudaConcedida) {
               System.out.println("!!! Fumador (" + nombre(miRecurso) + ") se despierta para PRESTAR ayuda.");
               ayudaConcedida = true;
               notifyAll();
            }
            wait();
        }

        // COGER DE LA MESA
        int ingredienteCogido = ingredienteMesa;
        ingredienteMesa = null; // La mesa queda vacía de ingredientes del estanquero
        
        // Calcular qué me falta
        // Total recursos son 0, 1, 2. La suma es 3.
        // Me falta = 3 - miRecurso - ingredienteCogido
        int meFalta = 3 - miRecurso - ingredienteCogido;
        
        System.out.println("... Fumador (" + nombre(miRecurso) + ") coge " + nombre(ingredienteCogido) + 
                           " y PIDE " + nombre(meFalta));

        // PEDIR AYUDA
        pidiendoAyuda = true;
        ingredienteSolicitado = meFalta;
        ayudaConcedida = false;
        notifyAll(); // Despierta a los compañeros para que vean la petición

        // Esperar a que el compañero solidario me lo dé
        while (!ayudaConcedida) {
            wait();
        }

        // FUMAR
        System.out.println("=== Fumador (" + nombre(miRecurso) + ") recibe ayuda y FUMA.");
        
        // RESETEAR SISTEMA
        pidiendoAyuda = false;
        ingredienteSolicitado = null;
        ayudaConcedida = false;
        
        notifyAll(); // Avisar al estanquero de que he terminado
    }

    private String nombre(int i) {
        return i==0 ? "TABACO" : (i==1 ? "PAPEL" : "CERILLA");
    }
}