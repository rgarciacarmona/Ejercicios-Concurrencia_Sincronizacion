package es.upm.dit.aled.tema3.ej7;

public class Main {
    public static void main(String[] args) {
        Mesa mesa = new Mesa();

        // Estanquero
        new Thread(() -> {
            try {
                while (true) {
                    // Elegir aleatoriamente qué NO poner (el ingrediente que falta)
                    int falta = (int)(Math.random() * 3);
                    // Poner los otros dos
                    int pone1 = (falta + 1) % 3;
                    int pone2 = (falta + 2) % 3;
                    
                    mesa.ponerIngredientes(pone1, pone2);
                }
            } catch (InterruptedException e) {}
        }, "Estanquero").start();

        // 3 Fumadores (0:Tiene Tabaco, 1:Tiene Papel, 2:Tiene Cerillas)
        for (int i = 0; i < 3; i++) {
            final int miIngrediente = i;
            new Thread(() -> {
                try {
                    while (true) {
                        mesa.cogerIngredientes(miIngrediente);
                        Thread.sleep((long)(Math.random() * 500)); // Tiempo fumando
                    }
                } catch (InterruptedException e) {}
            }, "Fumador-" + i).start();
        }
    }
}