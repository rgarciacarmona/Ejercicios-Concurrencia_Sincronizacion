package es.upm.dit.concurrencia.ej12;

import java.util.LinkedList;
import java.util.Queue;

public class GestorMuseo {

	// Estado del museo
	private int personasDentro = 0;
	private int temperatura = 25; // Temp inicial segura
	private int aforoMaximo = 50;

	// Colas de espera
	private Queue<Thread> colaNormal = new LinkedList<>();
	private Queue<Thread> colaJubilados = new LinkedList<>();

	/**
	 * Entrada Persona Normal
	 */
	public synchronized void entrarSala() {
		Thread yo = Thread.currentThread();
		colaNormal.add(yo); // Me añado al final de la cola normal
		
		try {
			// Esperar si:
			// 1. El aforo está completo (o excedido por cambio de temp).
			// 2. Hay jubilados esperando (tienen prioridad).
			// 3. No soy el primero en mi cola
			while (personasDentro >= aforoMaximo || !colaJubilados.isEmpty() || colaNormal.peek() != yo) {
				wait();
			}

			colaNormal.poll(); // Me extraigo de la cabeza de la cola
			personasDentro++;

			System.out.println(
					">>> Persona entra. Aforo: " + personasDentro + "/" + aforoMaximo + " (Temp: " + temperatura + ")");
		} catch (InterruptedException e) {
			// Si el hilo es interrumpido mientras esperaba, debemos sacarlo de la cola
			// para que no bloquee eternamente a los que están detrás de él.
			colaNormal.remove(yo);
			notifyAll();
		}
	}

	/**
	 * Entrada Persona Jubilada
	 */
	public synchronized void entrarSalaJubilado() {
		Thread yo = Thread.currentThread();
		colaJubilados.add(yo); // Me añado al final de la cola de jubilados
		
		try {
			// Esperar si:
			// 1. No soy el primero en la cola de jubilados.
			// 2. El aforo está completo.
			while (colaJubilados.peek() != yo || personasDentro >= aforoMaximo) {
				wait();
			}

			colaJubilados.poll(); // Me extraigo de la cabeza de la cola
			personasDentro++;
			System.out.println(">>> JUBILADO entra. Aforo: " + personasDentro + "/" + aforoMaximo);

		} catch (InterruptedException e) {
			// Si el hilo es interrumpido mientras esperaba, debemos sacarlo de la cola
			// para que no bloquee eternamente a los que están detrás de él.
			colaJubilados.remove(yo);
			notifyAll();
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
			// Si el aforo aumenta (se arregla el aire), despertamos a la gente para que
			// entre.
			// Si disminuye, no hacemos nada (se bloquearán los siguientes en entrar).
			notifyAll();
		}
	}
}