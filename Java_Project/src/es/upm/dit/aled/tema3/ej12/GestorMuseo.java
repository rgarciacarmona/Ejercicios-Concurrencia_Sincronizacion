package es.upm.dit.aled.tema3.ej12;

public class GestorMuseo {

    // Estado del museo
    private int personasDentro = 0;
    private int temperatura = 25; // Temp inicial segura
    private int aforoMaximo = 50;
    
    // Control de prioridad
    private int jubiladosEsperando = 0;

    /**
     * Entrada Persona Normal
     */
    public synchronized void entrarSala() throws InterruptedException {
        // Esperar si:
        // 1. El aforo está completo (o excedido por cambio de temp).
        // 2. Hay jubilados esperando (tienen prioridad).
        while (personasDentro >= aforoMaximo || jubiladosEsperando > 0) {
            wait();
        }

        personasDentro++;
        System.out.println(">>> Persona entra. Aforo: " + personasDentro + "/" + aforoMaximo + " (Temp: " + temperatura + ")");
    }

    /**
     * Entrada Persona Jubilada
     */
    public synchronized void entrarSalaJubilado() throws InterruptedException {
        jubiladosEsperando++;
        System.out.println("... Jubilado esperando. Total cola prioritaria: " + jubiladosEsperando);

        try {
            // El jubilado solo espera si no hay sitio real.
            // No le importa si hay personas normales esperando (se las salta).
            while (personasDentro >= aforoMaximo) {
                wait();
            }

            personasDentro++;
            System.out.println(">>> JUBILADO entra. Aforo: " + personasDentro + "/" + aforoMaximo);
            
        } finally {
            // Tanto si entra como si sale por interrupción, deja de esperar
            jubiladosEsperando--;
        }
    }

    /**
     * Salida (común para todos)
     */
    public synchronized void salirSala() {
        personasDentro--;
        System.out.println("<<< Alguien sale. Aforo: " + personasDentro + "/" + aforoMaximo);
        notifyAll(); // Notificar a todos (jubilados y normales) para que reevalúen entrar
    }

    /**
     * Notificación de Temperatura (Hebra Termómetro)
     */
    public synchronized void notificarTemperatura(int temp) {
        this.temperatura = temp;
        
        int aforoAnterior = aforoMaximo;
        
        // Actualizar aforo según la regla
        if (temperatura > 30) {
            aforoMaximo = 35;
        } else {
            aforoMaximo = 50;
        }

        if (aforoMaximo != aforoAnterior) {
            System.out.println("!!! CAMBIO DE TEMPERATURA (" + temp + "ºC). Nuevo aforo: " + aforoMaximo);
            // Si el aforo aumenta (se arregla el aire), despertamos a la gente para que entre.
            // Si disminuye, no hacemos nada (se bloquearán los siguientes en entrar).
            notifyAll();
        }
    }
}