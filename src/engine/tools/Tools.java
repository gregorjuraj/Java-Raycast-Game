package engine.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import main.HernyPanel;

public class Tools {
    public static final double PI = Math.PI;
    public static final double STUPNENARADIANY = 0.0174532925199;


    // v radianoch!!!!!!!!!!!!!!!!!!!!
    public static double korekciaUhla(double uhol) {
        if (uhol < 0) {
            return 2 * PI;
        } else if (uhol >= 2 * PI) {
            return -1 * (2 * PI);
        }
        return 0;
    }

    public static int[] loadImageTexture(String filePath) {
        int[] texture = new int[64 * 64];
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            int width = image.getWidth();
            int height = image.getHeight();

            // ak nieje 64x64 tak zmenim velkost
            if (width != 64 || height != 64) {
                BufferedImage resized = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
                resized.getGraphics().drawImage(image.getScaledInstance(64, 64, java.awt.Image.SCALE_DEFAULT), 0, 0, null);
                resized.getRGB(0, 0, 64, 64, texture, 0, 64);
            } else {
                image.getRGB(0, 0, 64, 64, texture, 0, 64);
            }
        } catch (IOException e) {
            System.err.println("Failed to load image: " + e.getMessage());
            System.out.println(filePath);
            // na debug, keby sa nenacita - nastavim cervenu farbu na stenu
            for (int i = 0; i < texture.length; i++) {
                texture[i] = 0xFF0000FF;
            }
        }
        return texture;
    }

    public static int sekundyNaTick(int cas) {
        return cas * (int)HernyPanel.FPS;
    }

    public static double spriteTileStred(int pos) {
        return (pos + 1) * 64 - 32;
    }

}
