package es.upm.dit.aled.tema3.ej16;

/**
 * Ejercicio 16: Hebra que simula un cliente
 */
public class Cliente extends Thread {

    private int id;
    private Peluqueria peluqueria;
    
    // Flags para evitar race conditions (notificaciones perdidas)
    private boolean corteTerminado = false;
    private boolean pagoTerminado = false;

    public Cliente(int id, Peluqueria peluqueria) {
        this.id = id;
        this.peluqueria = peluqueria;
    }

    @Override
    public void run() {
        // 1. Intentar entrar
        boolean entra = peluqueria.clienteLlega(this);
        if (!entra) return;

        // 2. Esperar Corte
        synchronized (this) {
            while (!corteTerminado) {
                try { wait(); } catch (InterruptedException e) {}
            }
        }

        // 3. Ir a Pagar (Bloquea si la caja está ocupada)
        try {
            peluqueria.irACaja(this);
        } catch (InterruptedException e) { e.printStackTrace(); }

        // 4. Esperar a ser cobrado
        synchronized (this) {
            while (!pagoTerminado) {
                try { wait(); } catch (InterruptedException e) {}
            }
        }

        // 5. Salir
        peluqueria.clienteSaleTienda(this);
    }

    // Métodos para ser despertado por el peluquero/ayudante
    public synchronized void avisarFinCorte() {
        this.corteTerminado = true;
        notify();
    }

    public synchronized void avisarFinPago() {
        this.pagoTerminado = true;
        notify();
    }
}