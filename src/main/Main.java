package main;

import javax.swing.JFrame;

/**
 * Hlavná trieda hry.
 * Inicializuje herné okno a spúšťa herný panel.
 */
public class Main {

    /**
     * Hlavná metóda aplikácie.
     * Nastavuje grafické vlastnosti, vytvára herné okno, pridáva herný panel a spúšťa hru.
     */
    public static void main(String[] args) {
        // povolenie opengl pre lepsi vykon :)
        System.setProperty("sun.java2d.opengl", "true");

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("3D test");


        HernyPanel hernyPanel = new HernyPanel();
        window.add(hernyPanel);
        window.pack(); // prisposobenie okna

        // centrovat okno a zobrazenie
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // spustenie
        hernyPanel.initGameThread();
    }
}