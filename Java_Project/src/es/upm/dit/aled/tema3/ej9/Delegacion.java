package es.upm.dit.aled.tema3.ej9;

public class Delegacion {

    // Contadores de personas esperando en cada cola
    private int cola1 = 0;
    private int cola2 = 0;
    
    // Estado de Andrés
    private boolean andresDurmiendo = true; // Empieza durmiendo hasta que llegue alguien
    
    // Flags para coordinar el "rendezvous" (encuentro) entre Andrés y el ciudadano
    // 0: Nadie siendo atendido, 1: Atendiendo V1, 2: Atendiendo V2
    private int atendiendoA = 0; 
    private boolean servicioTerminado = false;

    /**
     * Ciudadano espera en Ventanilla 1
     */
    public synchronized void esperarVentanilla1(String id) throws InterruptedException {
        cola1++;
        System.out.println("Citizen " + id + " entra en Ventanilla 1 (Hay " + cola1 + ").");
        
        // Despertar a Andrés si estaba durmiendo
        notifyAll();

        // Esperar mientras:
        // 1. No me están atendiendo a MÍ (atendiendoA != 1)
        //    Nota: En un monitor simple con wait(), no podemos elegir hebra específica fácilmente.
        //    Usamos la condición: Espero mientras NO sea mi turno de ser procesado.
        //    Simplificación: Andrés elige "procesar ventanilla 1", y una hebra de la V1 entra.
        while (atendiendoA != 1) {
            wait();
        }

        // Sección crítica: Siendo atendido
        System.out.println(">>> Citizen " + id + " está siendo atendido en V1...");
        
        // Esperar a que Andrés termine el papeleo
        while (!servicioTerminado) {
            wait();
        }

        // Salir
        cola1--;
        atendiendoA = 0; // Queda libre
        servicioTerminado = false;
        System.out.println("<<< Citizen " + id + " sale de Hacienda.");
        notifyAll(); // Avisar a Andrés de que ya me he ido
    }

    /**
     * Ciudadano espera en Ventanilla 2
     */
    public synchronized void esperarVentanilla2(String id) throws InterruptedException {
        cola2++;
        System.out.println("Citizen " + id + " entra en Ventanilla 2 (Hay " + cola2 + ").");
        notifyAll();

        while (atendiendoA != 2) {
            wait();
        }

        System.out.println(">>> Citizen " + id + " está siendo atendido en V2...");
        while (!servicioTerminado) {
            wait();
        }

        cola2--;
        atendiendoA = 0;
        servicioTerminado = false;
        System.out.println("<<< Citizen " + id + " sale de Hacienda.");
        notifyAll();
    }

    /**
     * Método del Funcionario (Andrés)
     */
    public synchronized void atenderCiudadano() throws InterruptedException {
        // 1. Si no hay nadie, dormir
        while (cola1 == 0 && cola2 == 0) {
            System.out.println("ZZZ Andrés se echa una siesta...");
            wait();
        }

        // 2. Decidir a quién atender (Regla: Cola más larga, empate gana V1)
        if (cola1 >= cola2) {
            // Elige V1
            atendiendoA = 1;
            System.out.println("--- Andrés llama a Ventanilla 1 (Colas: V1=" + cola1 + ", V2=" + cola2 + ")");
        } else {
            // Elige V2
            atendiendoA = 2;
            System.out.println("--- Andrés llama a Ventanilla 2 (Colas: V1=" + cola1 + ", V2=" + cola2 + ")");
        }

        // Despertar a los ciudadanos para que uno de la ventanilla elegida entre
        notifyAll();

        // 3. Simular trabajo (esperar a que el ciudadano entre y luego "terminar")
        // Esperamos a que el ciudadano coja el turno (realmente el flujo es síncrono en el monitor,
        // pero simulamos el tiempo de proceso).
        Thread.sleep((long)(Math.random() * 500)); // Tiempo de papeleo

        // 4. Terminar servicio
        servicioTerminado = true;
        notifyAll(); // Despierta al ciudadano para que se vaya

        // 5. Esperar a que el ciudadano se vaya realmente antes de llamar al siguiente
        while (atendiendoA != 0) {
            wait();
        }
    }
}