package es.upm.dit.aled.tema3.ej4;

public class Coche extends Thread {
    private Puente puente;
    private String matricula;
    private boolean esNorteSur; // true = N->S, false = S->N

    public Coche(Puente p, String m, boolean dir) {
        this.puente = p;
        this.matricula = m;
        this.esNorteSur = dir;
    }

    @Override
    public void run() {
        try {
            if (esNorteSur) {
                puente.entrarNorteSur(matricula);
            } else {
                puente.entrarSurNorte(matricula);
            }

            // Cruzando el puente...
            Thread.sleep((long) (Math.random() * 500));

            puente.salir(matricula);

        } catch (InterruptedException e) {}
    }
}