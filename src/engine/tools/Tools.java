package engine.tools;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import main.HernyPanel;

/**
 * Trieda Tools poskytuje pomocné metódy pre herný engine.
 */
public class Tools {
    public static final double PI = Math.PI;
    public static final double STUPNENARADIANY = 0.0174532925199;

    /**
     * Koriguje uhol v radiánoch, aby zostal v rozsahu [0, 2*PI).
     *
     * @param uhol Uhol v radiánoch.
     * @return Hodnota korekcie: 2*PI ak je uhol záporný, -2*PI ak je uhol väčší alebo rovný 2*PI, inak 0.
     */
    public static double korekciaUhla(double uhol) {
        if (uhol < 0) {
            return 2 * PI;
        } else if (uhol >= 2 * PI) {
            return -1 * (2 * PI);
        }
        return 0;
    }

    /**
     * Načítanie obrázka zo súboru a vráti ju ako pole RGB hodnôt (textura).
     *
     * @param filePath Cesta k súboru s obrázkom.
     * @param x X-ová súradnica v obrázku, odkiaľ sa má načítať textúra (64x64 pixelov).
     * @param y Y-ová súradnica v obrázku, odkiaľ sa má načítať textúra (64x64 pixelov).
     * @param resize Ak je true, obrázok sa zmenší na rozmer 64x64 pixelov.
     * @return Pole int-ov obsahujúce RGB hodnoty textúry (64x64 pixelov). Ak načítanie zlyhá, vráti pole vyplnené modrou farbou (0xFF0000FF).
     * @throws IllegalArgumentException Ak sú zadané neplatné súradnice a resize nie je povolené.
     */
    public static int[] loadImageTexture(String filePath, int x, int y, boolean resize) {
        int[] texture = new int[64 * 64];
        try {
            InputStream inputStream = Tools.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new IllegalStateException("Obrazok nenajdeny: " + filePath);
            }
            BufferedImage image = ImageIO.read(inputStream);
            inputStream.close();

            boolean outOfBounds = x < 0 || y < 0 || x + 64 > image.getWidth() || y + 64 > image.getHeight();

            if (resize || outOfBounds) {
                if (outOfBounds && !resize) {
                    throw new IllegalArgumentException("Neplatne coords");
                }
                BufferedImage resized = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
                resized.getGraphics().drawImage(image.getScaledInstance(64, 64, Image.SCALE_DEFAULT), 0, 0, null);
                image = resized;
            }
            image.getRGB(x, y, 64, 64, texture, 0, 64);
        } catch (IOException | IllegalArgumentException e) {
            Arrays.fill(texture, 0xFF0000FF); // Blue
        }
        return texture;
    }

    /**
     * Prevedie čas v sekundách na počet tickov na základe FPS herného panelu.
     *
     * @param cas Čas v sekundách.
     * @return Počet tickov
     */
    public static int sekundyNaTick(int cas) {
        return cas * (int)HernyPanel.FPS;
    }

    /**
     * Vypočíta pozíciu stredu tilu pre sprite na základe jeho pozície.
     *
     * @param pos Pozícia dlaždice (index).
     * @return Stred dlaždice v pixeloch
     */
    public static double spriteTileStred(int pos) {
        return (pos + 1) * 64 - 32;
    }
}