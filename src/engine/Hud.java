package engine;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Trieda pre správu užívateľského rozhrania (HUD) v hernom engine, zodpovedná za vykresľovanie prvkov na obrazovku.
 */
public class Hud {
    private Kreslenie kreslenie;
    private BufferedImage crosshair;
    private BufferedImage hud;

    /**
     * Konštruktor pre inicializáciu HUD s objektom pre kreslenie.
     *
     * @param kreslenie Objekt Kreslenie na vykresľovanie grafiky
     */
    public Hud(Kreslenie kreslenie) {
        this.kreslenie = kreslenie;
        try {
            InputStream crosshairStream = this.getClass().getClassLoader().getResourceAsStream("hud/crosshair.png");
            if (crosshairStream != null) {
                this.crosshair = ImageIO.read(crosshairStream);
                crosshairStream.close();
            }

            InputStream hudStream = this.getClass().getClassLoader().getResourceAsStream("hud/hudoverall.png");
            if (hudStream != null) {
                this.hud = ImageIO.read(hudStream);
                hudStream.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Chyba pri nacitavani HUD obrazkov", e);
        }
    }

    /**
     * Vykreslí prvky HUD na obrazovku pomocou poskytnutého Graphics2D objektu.
     *
     * @param g2 Objekt Graphics2D na vykreslenie grafiky
     */
    public void draw(Graphics2D g2) {
        BufferedImage screen = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dScreen = screen.createGraphics();

        g2dScreen.drawImage(this.crosshair, 640 - 5, 320 - 5, null);
        g2dScreen.drawImage(this.hud, 0, 720 - 113, null);

        g2.drawImage(screen, 0, 0, 1280, 720, null);
        g2dScreen.dispose();
    }
}