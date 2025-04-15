package main;

import engine.Tik;
import engine.Kreslenie;
import entity.Hrac;
import levely.LoadLevels;
import levely.levelData.Level;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;


public class HernyPanel extends JPanel implements Runnable {

    private Thread gameThread;

    private final Ovladanie ovladanie;
    private Hrac hrac;
    private LoadLevels level;
    private Kreslenie kreslenie;
    private Tik AI;

    public static final double FPS = 60; // Frames per second
    public static final double TPS = 20; // Ticks per second for logic



    public HernyPanel() {

        this.setPreferredSize(new Dimension(1280, 720));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setLayout(null);

        this.ovladanie = new Ovladanie();
        this.addKeyListener(this.ovladanie);
        this.level = new LoadLevels();

        this.hrac = new Hrac(this.ovladanie, this);
        this.kreslenie = new Kreslenie(this.hrac);
        this.AI = new Tik();

    }

    public int getSuradnicePanela(String xOrY) {
        Point panelPozicia = this.getLocationOnScreen();
        if (xOrY.equals("x")) {
            return panelPozicia.x;
        } else {
            return panelPozicia.y;
        }
    }

    public void initGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval =  1000000000 / FPS; ///1000000000 nanosec. = 1 sec. cca 16.67ms pre snimok
        double tickInterval = 1000000000 / TPS; // ~50 ms per tick
        double delta = 0; // For rendering
        double tickDelta = 0; // For logic
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        int tickCount = 0;

        while (this.gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            tickDelta += (currentTime - lastTime) / tickInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            // bude robit 2 veci:
            // 1. update
            // 2. prekreslit
            if (tickDelta >= 1) {
                this.updateTPS(this.level.getLevels(1), this.hrac);
                tickDelta--;
                tickCount++;
            }

            if (delta >= 1) {
                long startUpdate = System.nanoTime();
                this.update(this.level.getLevels(1));
                long endUpdate = System.nanoTime();

                long startRepaint = System.nanoTime();
                this.repaint();
                long endRepaint = System.nanoTime();

                //System.out.println("Update trval: " + (endUpdate - startUpdate) / 1000000.0 + " ms");
                //System.out.println("Repaint trval: " + (endRepaint - startRepaint) / 1000000.0 + " ms");

                delta--;
                drawCount++;
            }

            //ukazovatel FPS (debug)
            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }


    public void update(Level level) {
        this.hrac.update(level);
        this.kreslenie.update(level);
    }
    public void updateTPS(Level level, Hrac hrac) {
        this.AI.update(level, hrac);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        this.kreslenie.draw(g2);
        this.hrac.draw(g2);
        //this.levelOne.draw(g2);

        //g2.dispose();
    }
}
