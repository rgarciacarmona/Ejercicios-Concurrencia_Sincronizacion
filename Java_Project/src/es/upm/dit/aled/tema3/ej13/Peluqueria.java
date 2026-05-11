package es.upm.dit.aled.tema3.ej13;
import java.util.Random;

/**
 * Ejercicio 13: Monitor que gestiona la peluquería
 * 
 * 1 Silla de peluquería
 * 5 Sofás
 */
public class Peluqueria {

    private final int MAX_SOFAS = 5;
    private int sofasOcupados = 0;
    private boolean sillaOcupada = false;
    private Random random = new Random();

    /**
     * Lo invoca un cliente al llegar.
     * Devuelve true si el cliente es atendido (consigue silla o sofá).
     * Devuelve false si el cliente se va (todo lleno).
     */
    public synchronized boolean entrarPeluqueria() {
        // Si todos los sofás (y la silla) están ocupados se marcha
        if (sillaOcupada && sofasOcupados == MAX_SOFAS) {
            System.out.println("Cliente " + Thread.currentThread().getName() + " SE VA. (Todo lleno)");
            return false;
        }

        // Si la silla está libre, la ocupa
        if (!sillaOcupada) {
            sillaOcupada = true;
            System.out.println("Cliente " + Thread.currentThread().getName() + " se sienta en la SILLA.");
            return true;
        }

        // Si la silla está ocupada, va al sofá
        if (sofasOcupados < MAX_SOFAS) {
            sofasOcupados++;
            System.out.println("Cliente " + Thread.currentThread().getName() + " espera en SOFÁ. (Sofás: " + sofasOcupados + ")");
            
            // Espera en el sofá hasta que la silla quede libre
            while (sillaOcupada) {
                try {
                    wait(); // Espera una notificación (de salirPeluqueria)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // La silla se ha liberado, el cliente se levanta del sofá y ocupa la silla
            sofasOcupados--;
            sillaOcupada = true;
            System.out.println("Cliente " + Thread.currentThread().getName() + " pasa de SOFÁ a SILLA. (Sofás: " + sofasOcupados + ")");
            return true;
        }
        
        return false; // No debería llegarse aquí, pero por si acaso
    }

    /**
     * Lo invoca un cliente después de simular su corte de pelo
     */
    public synchronized void salirPeluqueria() {
        sillaOcupada = false;
        System.out.println("Cliente " + Thread.currentThread().getName() + " SALE de la peluquería.");
        
        // Notifica a TODOS los que esperan en el sofá que la silla está libre
        // Solo uno de ellos (el que gane la carrera) la ocupará
        notifyAll();
    }
    
    /**
     * Simula el tiempo de corte de pelo
     */
    public void simularCortePelo() {
        try {
            // Tiempo necesario para cortar el pelo: de 0 a 400 milisegundos
            Thread.sleep(random.nextInt(401));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}