package es.upm.dit.concurrencia.ej09;

import java.util.LinkedList;
import java.util.Queue;

public class Delegacion {

	// Listas de espera para cada ventanilla
	private Queue<Thread> esperaV1 = new LinkedList<>();
	private Queue<Thread> esperaV2 = new LinkedList<>();

	// Flags para coordinar el "rendezvous" (encuentro) entre Andrés y el ciudadano
	// 0: Nadie siendo atendido, 1: Atendiendo V1, 2: Atendiendo V2
	private int atendiendoA = 0;
	private boolean servicioTerminado = false;

	/**
	 * Ciudadano espera en Ventanilla 1
	 */
	public synchronized void esperarVentanilla1(String id) {
		Thread yo = Thread.currentThread();
		esperaV1.add(yo); // Me añado a la cola de la ventanilla 1
		System.out.println("Ciudadano " + id + " entra en cola V1 (Posición: " + esperaV1.size() + ")");

		// Despertar a Andrés si estaba durmiendo
		notifyAll();

		try {
			// Esperar mientras:
			// 1. No soy el primero de la cola V1 (Orden de llegada)
			// 2. Andrés no ha seleccionado la V1 para atender
			while (esperaV1.peek() != yo || atendiendoA != 1) {
				wait();
			}

			// Sección crítica: Siendo atendido
			System.out.println(">>> Ciudadano " + id + " está siendo atendido en V1...");

			// Esperar a que Andrés termine el papeleo
			while (!servicioTerminado) {
				wait();
			}

			// Salir
			esperaV1.poll();
			atendiendoA = 0; // Queda libre
			servicioTerminado = false;
			System.out.println("<<< Ciudadano " + id + " sale de Hacienda.");
			notifyAll(); // Avisar a Andrés de que ya me he ido
		} catch (InterruptedException e) {
			esperaV1.remove(yo);
			notifyAll();
		}
	}

	/**
	 * Ciudadano espera en Ventanilla 2
	 */
	public synchronized void esperarVentanilla2(String id) throws InterruptedException {
		Thread yo = Thread.currentThread();
		esperaV2.add(yo);
		System.out.println("Ciudadano " + id + " entra en cola V2 (Posición: " + esperaV2.size() + ")");

		notifyAll();

		try {
			while (esperaV2.peek() != yo || atendiendoA != 2) {
				wait();
			}

			System.out.println(">>> Ciudadano " + id + " está siendo atendido en V2...");
			while (!servicioTerminado) {
				wait();
			}

			esperaV2.poll();
			atendiendoA = 0;
			servicioTerminado = false;
			System.out.println("<<< Ciudadano " + id + " sale de Hacienda.");
			notifyAll();

		} catch (InterruptedException e) {
			esperaV2.remove(yo);
			notifyAll();
			throw e;
		}
	}

	/**
	 * Método del Funcionario (Andrés)
	 */
	public synchronized void atenderCiudadano() throws InterruptedException {
		// 1. Si no hay nadie, dormir
		while (esperaV1.isEmpty() && esperaV2.isEmpty()) {
			System.out.println("ZZZ Andrés se echa una siesta...");
			wait();
		}

		// 2. Decidir a quién atender (Regla: Cola más larga, empate gana V1)
		if (esperaV1.size() >= esperaV2.size()) {
			// Elige V1
			atendiendoA = 1;
			System.out.println("--- Andrés llama a Ventanilla 1 (Colas: V1=" + esperaV1.size() + ", V2=" + esperaV2.size() + ")");
		} else {
			// Elige V2
			atendiendoA = 2;
			System.out.println("--- Andrés llama a Ventanilla 2 (Colas: V1=" + esperaV1.size() + ", V2=" + esperaV2.size() + ")");
		}

		// Despertar a los ciudadanos para que uno de la ventanilla elegida entre
		notifyAll();

		// 3. Simular trabajo (esperar a que el ciudadano entre y luego "terminar")
		Thread.sleep((long) (Math.random() * 500)); // Tiempo de papeleo

		// 4. Terminar servicio
		servicioTerminado = true;
		notifyAll(); // Despierta al ciudadano para que se vaya

		// 5. Esperar a que el ciudadano se vaya realmente antes de llamar al siguiente
		while (atendiendoA != 0) {
			wait();
		}
	}
}