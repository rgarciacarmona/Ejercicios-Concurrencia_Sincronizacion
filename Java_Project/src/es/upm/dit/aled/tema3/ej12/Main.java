package es.upm.dit.aled.tema3.ej12;

public class Main {
    public static void main(String[] args) {
        GestorMuseo museo = new GestorMuseo();

        // Hebra Termómetro (Igual que antes)
        new Thread(() -> {
            try {
                while (true) {
                    // Simular temperatura variable (entre 20 y 45 ºC) para forzar cambios de aforo
                    int temp = 20 + (int)(Math.random() * 26);
                    museo.notificarTemperatura(temp);
                    Thread.sleep(2500); // Cambia la temperatura cada 2.5 segundos
                }
            } catch (InterruptedException e) {}
        }, "Termometro").start();

        // Generador de Personas (Modificado para saturar)
        // Aumentamos a 200 personas para asegurar que superamos el aforo de 35 y 50
        for (int i = 0; i < 200; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    boolean esJubilado = Math.random() < 0.20; // 20% jubilados
                    
                    if (esJubilado) {
                        museo.entrarSalaJubilado();
                    } else {
                        museo.entrarSala();
                    }

                    // Simular visita: Aumentamos el tiempo (2 a 4 segundos)
                    // Esto provoca que la gente se acumule dentro.
                    Thread.sleep(2000 + (long)(Math.random() * 2000));

                    museo.salirSala();

                } catch (InterruptedException e) {}
            }, "Persona-" + id).start();
            
            // Intervalo de llegada MUY CORTO (0 a 30ms)
            // Esto simula una llegada masiva de gente en poco tiempo.
            try { 
                Thread.sleep((long)(Math.random() * 30)); 
            } catch (InterruptedException e) {}
        }
    }
}