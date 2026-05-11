package es.upm.dit.aled.tema3.ej15;

/**
 * Ejercicio 15: Hebra que simula un cliente
 */
public class Cliente extends Thread {

	private int id;
	private Peluqueria peluqueria;
	private boolean corteTerminado = false;

	public Cliente(int id, Peluqueria peluqueria) {
		this.id = id;
		this.peluqueria = peluqueria;
	}


	@Override
    public void run() {
        boolean esAtendido = peluqueria.clienteLlega(this);

        if (esAtendido) {
            synchronized (this) {
                // Mientras no haya terminado el corte, espero.
                while (!corteTerminado) { 
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            peluqueria.clienteSeVa(this);
        }
    }

    public synchronized void terminarCorte() {
        this.corteTerminado = true;
        notify();
    }
}