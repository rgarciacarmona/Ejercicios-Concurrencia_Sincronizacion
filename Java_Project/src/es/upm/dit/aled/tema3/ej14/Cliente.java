package es.upm.dit.aled.tema3.ej14;

/**
 * Ejercicio 14: Hebra que simula un cliente
 */
public class Cliente extends Thread {

    private Peluqueria peluqueria;

    public Cliente(Peluqueria peluqueria) {
        this.peluqueria = peluqueria;
    }

    @Override
    public void run() {
        // El cliente intenta entrar
        boolean esAtendido = peluqueria.entrarPeluqueria();
        
        // Si ha conseguido sitio (no se ha ido)
        if (esAtendido) {
            // Simula el corte de pelo (mientras "ocupa" la silla)
            peluqueria.simularCortePelo();
            
            // Libera la silla y se va
            peluqueria.salirPeluqueria();
        }
    }
}