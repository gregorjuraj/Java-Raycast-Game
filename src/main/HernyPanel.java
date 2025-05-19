package main;

import engine.Hud;
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

/**
 * Trieda reprezentujúca hlavný herný panel.
 * Spravuje herný loop, vykresľovanie, aktualizácie a ovládanie hry.
 */
public class HernyPanel extends JPanel implements Runnable {
    private Thread gameThread; // Vlákno pre game-loop

    private final Ovladanie ovladanie;
    private Hrac hrac;
    private LoadLevels level;

    /**
     * Konštruktor herného panelu.
     * Inicializuje panel, ovládanie, hráča, vykresľovanie, HUD a AI.
     */
    private Kreslenie kreslenie; // Objekt pre vykresľovanie herných prvkov
    private Hud hud; // Objekt pre vykresľovanie používateľského rozhrania
    private Tik ai; // Objekt pre aktualizáciu AI

    public static final double FPS = 60; // Pocet snimok za sekundu
    public static final double TPS = 10; // Pocet tikov za sekundu

    /**
     * Konštruktor herného panelu.
     * Inicializuje panel, ovládanie, hráča, vykresľovanie, HUD a AI.
     */
    public HernyPanel() {
        this.setPreferredSize(new Dimension(1280, 720));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //pre plynule vykreslovanie
        this.setFocusable(true);
        this.setLayout(null); //manualne rozlozenie pre panel

        this.ovladanie = new Ovladanie();
        this.addKeyListener(this.ovladanie);
        this.level = new LoadLevels();

        this.hrac = new Hrac(this.ovladanie, this);
        this.kreslenie = new Kreslenie(this.hrac);
        this.hud = new Hud(this.kreslenie);
        this.ai = new Tik();
    }

    /**
     * Vracia X alebo Y súradnicu pozície panela na obrazovke.
     * @param xOrY Určuje, či sa má vrátiť X ("x") alebo Y ("y") súradnica
     * @return Súradnica panela
     */
    public int getSuradnicePanela(String xOrY) {
        Point panelPozicia = this.getLocationOnScreen();
        if (xOrY.equals("x")) {
            return panelPozicia.x;
        } else {
            return panelPozicia.y;
        }
    }

    /**
     * Spustí herné vlákno.
     */
    public void initGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    /**
     * Hlavný herný loop.
     * Riadi aktualizácie logiky, vykresľovanie a časovanie hry.
     */
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // Interval pre vykresľovanie (cca 16.67 ms)
        double tickInterval = 1000000000 / TPS; // Interval pre logické aktualizácie (cca 100 ms)
        double delta = 0;
        double tickDelta = 0;
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

            // aktualizacia logiky entit
            if (tickDelta >= 1) {
                this.updateTPS(this.level.getLevels(0), this.hrac);
                tickDelta--;
                tickCount++;
            }

            // vykreslovanie
            if (delta >= 1) {
                long startUpdate = System.nanoTime();
                this.update(this.level.getLevels(0));
                long endUpdate = System.nanoTime();

                long startRepaint = System.nanoTime();
                this.repaint();
                long endRepaint = System.nanoTime();

                delta--;
                drawCount++;
            }

            // FPS
            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                tickCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Aktualizuje stav hry (hráč a vykresľovanie).
     * @param level Aktuálny herný level
     */
    public void update(Level level) {
        this.hrac.update(level);
        this.kreslenie.update(level);
    }

    /**
     * Aktualizuje AI logiku hry.
     * @param level Aktuálny herný level
     * @param hrac Inštancia hráča
     */
    public void updateTPS(Level level, Hrac hrac) {
        this.ai.update(level, hrac);
    }

    /**
     * Vykreslí herný obsah na panel.
     * @param g Grafický kontext
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        this.kreslenie.draw(g2);
        this.hud.draw(g2);

        //g2.dispose();
    }
}