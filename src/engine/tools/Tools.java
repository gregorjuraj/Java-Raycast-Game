package engine.tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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

    public static int[] loadImageTexture(String filePath, int x, int y, boolean resize) {
        int[] texture = new int[64 * 64];
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
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
            System.err.println("Nepodarilo sa nacitat obrazok: " + filePath + ": " + e.getMessage());
            Arrays.fill(texture, 0xFF0000FF); // Blue
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
