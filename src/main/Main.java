package main;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("3D test");

        HernyPanel hernyPanel = new HernyPanel();
        window.add(hernyPanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        hernyPanel.initGameThread();
    }
}