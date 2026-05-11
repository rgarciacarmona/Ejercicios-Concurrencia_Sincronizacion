package es.upm.dit.aled.tema3.ej16;
import java.util.Random;

/**
 * Ejercicio 16: Hebra que simula un peluquero
 */
public class Peluquero extends Thread {

    private Peluqueria peluqueria;
    private boolean esSirPatrick;
    private Random random = new Random();

    public Peluquero(Peluqueria peluqueria, boolean esSirPatrick) {
        this.peluqueria = peluqueria;
        this.esSirPatrick = esSirPatrick;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Pedir tarea (Corte o Cobro)
                Peluqueria.Tarea tarea = peluqueria.obtenerTrabajo(esSirPatrick);

                if (tarea.tipo == Peluqueria.TipoTrabajo.CORTE) {
                    // --- Lógica de CORTAR ---
                    System.out.println(this.getName() + " CORTA a " + tarea.cliente.getId());
                    
                    int tiempo = esSirPatrick ? random.nextInt(401) : random.nextInt(601);
                    Thread.sleep(tiempo);
                    
                    tarea.cliente.avisarFinCorte();

                } else {
                    // --- Lógica de COBRAR (Solo Ayudantes) ---
                    System.out.println(this.getName() + " $$$ COBRA a " + tarea.cliente.getId());
                    
                    Thread.sleep(random.nextInt(151)); // 0 a 150 ms
                    
                    tarea.cliente.avisarFinPago();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}