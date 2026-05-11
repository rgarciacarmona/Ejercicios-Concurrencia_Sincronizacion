package es.upm.dit.aled.tema3.ej1;

/**
 * Ejercicio 1: Main
 */
public class Main {
	// Configuración del número de hebras
	private static final int NUM_LECTORAS = 300;
	private static final int RATIO_LECTORAS_ESCRITORAS = 10;

	public static void main(String[] args) {
		// Recurso compartido (monitor)
		RecursoCompartido recurso = new RecursoCompartido();

		System.out.println("Iniciando: " + NUM_LECTORAS + " lectoras y " +
							NUM_LECTORAS / RATIO_LECTORAS_ESCRITORAS + " escritoras.");
		try {
			// Crear y arrancar hebras
			for (int i = 0; i < NUM_LECTORAS; i++) {
				Thread t = new HebraLectora(recurso, "L" + i);
				t.start();
				// 1 escritora por cada N lectoras
				if (i % RATIO_LECTORAS_ESCRITORAS == 0) {
					Thread t2 = new HebraEscritora(recurso, "E" + i);
					t2.start();
				}
				// Esperar hasta 500 ms entre creación de hebras.
				Thread.sleep((long) (Math.random() * 500));
			}
		} catch (InterruptedException e) {}
	}
}