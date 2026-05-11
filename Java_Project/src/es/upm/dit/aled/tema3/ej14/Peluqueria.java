package es.upm.dit.aled.tema3.ej14;
import java.util.Random;

/**
 * Ejercicio 14: Monitor que gestiona la peluquería
 * 
 * 1 Silla
 * 5 Sofás
 * 15 Sitios en la barra
 */
public class Peluqueria {

    private final int MAX_SOFAS = 5;
    private final int MAX_BAR = 15;
    
    private int sofasOcupados = 0;
    private int barOcupado = 0;
    private boolean sillaOcupada = false;
    private Random random = new Random();
    
    public synchronized boolean entrarPeluqueria() {
        // Si todo está lleno, se marcha
        if (sillaOcupada && sofasOcupados == MAX_SOFAS && barOcupado == MAX_BAR) {
            System.out.println("Cliente " + Thread.currentThread().getName() + " SE VA. (Todo lleno)");
            return false;
        }

        // Si la silla está libre, se sienta
        if (!sillaOcupada) {
            sillaOcupada = true;
            System.out.println("Cliente " + Thread.currentThread().getName() + " se sienta en la SILLA.");
            return true;
        }

        // Si la silla está ocupada, va a un sofá
        if (sofasOcupados < MAX_SOFAS) {
            sofasOcupados++;
            System.out.println("Cliente " + Thread.currentThread().getName() + " espera en SOFÁ. (Sofás: " + sofasOcupados + ")");
            
            // Espera en el sofá hasta que la silla quede libre
            while (sillaOcupada) {
                try { wait(); } catch (InterruptedException e) {}
            }
            sofasOcupados--;
            sillaOcupada = true;
            System.out.println("Cliente " + Thread.currentThread().getName() + " pasa de SOFÁ a SILLA. (Sofás: " + sofasOcupados + ")");
            return true;
        }
        
        // Si los sofás están llenos, va a la barra.
        if (barOcupado < MAX_BAR) {
            barOcupado++;
            System.out.println("Cliente " + Thread.currentThread().getName() + " espera en BAR. (Bar: " + barOcupado + ")");
            
            // Espera en la barra hasta que haya sitio en un sofá
            while (sofasOcupados == MAX_SOFAS) {
                try { wait(); } catch (InterruptedException e) {}
            }
            // Hay sitio en un sofá, se mueve
            barOcupado--;
            sofasOcupados++;
            System.out.println("Cliente " + Thread.currentThread().getName() + " pasa de BAR a SOFÁ. (Bar: " + barOcupado + ", Sofás: " + sofasOcupados + ")");
            
            // Ahora espera en el sofá hasta que la silla esté libre
            while (sillaOcupada) {
                try { wait(); } catch (InterruptedException e) {}
            }
            sofasOcupados--;
            sillaOcupada = true;
            System.out.println("Cliente " + Thread.currentThread().getName() + " pasa de SOFÁ a SILLA. (Sofás: " + sofasOcupados + ")");
            return true;
        }
        
        return false; // No debería llegarse aquí, pero por si acaso
    }

    public synchronized void salirPeluqueria() {
        sillaOcupada = false;
        System.out.println("Cliente " + Thread.currentThread().getName() + " SALE de la peluquería.");
        
        // Notifica a TODOS (en sofás y en barra)
        // Un cliente del sofá tomará la silla.
        // Un cliente de la barra tomará el sofá.
        notifyAll();
    }
    
    /**
     * Simula el tiempo de corte de pelo.
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