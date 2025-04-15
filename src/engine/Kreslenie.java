package engine;

import sprites.rotation.Soldier;
import sprites.spriteData.Sprite;
import sprites.spriteData.SpriteData;
import textures.TexturyEnum;
import entity.Hrac;
import levely.levelData.Level;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

public class Kreslenie {
    private final Hrac hrac;
    private final RaycastManager raycastManager;
    private final SpriteManager spriteManager;

    // pre tieto hodnoty je to ako retro hra, pre tie druhe uz viac "HD"
    private final int sirka = 320; //720
    private final int vyska = 200; //480
    private final int skalovanie = 277; //600
    private final double fov = 1.04719755; // 60 stupnov

    private int offset;

    public Kreslenie(Hrac hrac) {
        this.hrac = hrac;
        this.raycastManager = new RaycastManager();
        this.spriteManager = new SpriteManager();
        this.offset = 0;
    }

    public void update(Level levelOne) {
        this.raycastManager.update(this.hrac, levelOne, this);
        this.spriteManager.update(this.hrac, levelOne);
    }

    public void draw(Graphics2D g2) {
        BufferedImage screen = new BufferedImage(this.sirka, this.vyska, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((DataBufferInt)screen.getRaster().getDataBuffer()).getData();

        g2.setColor(Color.white);
        g2.fillRect(this.hrac.getHracX(), this.hrac.getHracY(), 10, 10);

        this.drawCeilingWallFloor(pixels);
        this.drawSprites(pixels, this.spriteManager.getVisibleSprites());

        g2.drawImage(screen, 0, 0, 1280, 720, null);

        boolean test = true;
        if (test) {
            for (int i = 0; i < 320; i++) {
                if (i == 160) {
                    g2.setColor(Color.red);
                    g2.drawLine(this.hrac.getHracX() + 5, this.hrac.getHracY() + 5, this.raycastManager.getRaycast()[i].getFinalDlzkaX(), this.raycastManager.getRaycast()[i].getFinalDlzkaY());
                    //System.out.println(this.raycastManager.getRaycast()[i].getFinalUhol());
                }
            }
        }

    }


    private void drawCeilingWallFloor(int[] pixels) {
        double rectWidth = this.sirka / 320.0;
        int horizon = (this.vyska - this.hrac.getPitch()) / 2;
        Calc[] raycast = this.raycastManager.getRaycast();

        for (int i = 0; i < 320; i++) {
            int xStart = (int)(i * rectWidth);
            int xEnd = (int)((i + 1) * rectWidth);

            // Calculate wall height
            double dlzka = raycast[i].getFinalDlzka() * Math.cos(this.hrac.getHracAngle() - raycast[i].getFinalUhol());
            int height = (int)(64.0 / dlzka * this.skalovanie);
            int halfHeight = height / 2;
            int yTop = horizon - halfHeight;
            int yBottom = horizon + halfHeight;

            int colorV = 0xFFFF00FF; // Magenta
            double maxDlzka = 800;

            for (int py = 0; py < this.vyska; py++) {
                for (int px = xStart; px < xEnd; px++) {
                    int index = py * this.sirka + px;
                    if (index < 0 || index >= pixels.length) {
                        continue;
                    }

                    if (py < yTop) { // Strop
                        double straightDistance = (64.0 * this.skalovanie) / (2.0 * (horizon - py));
                        double realDistance = straightDistance / Math.cos(this.hrac.getHracAngle() - raycast[i].getFinalUhol());
                        double shadingFactor = Math.max(0, 1.0 - (realDistance / maxDlzka));

                        double ceilX = this.hrac.getHracX() + realDistance * Math.cos(raycast[i].getFinalUhol());
                        double ceilY = this.hrac.getHracY() - realDistance * Math.sin(raycast[i].getFinalUhol());

                        int texX = (int)(ceilX % 64);
                        int texY = (int)(ceilY % 64);
                        if (texX < 0) {
                            texX += 64;
                        }
                        if (texY < 0) {
                            texY += 64;
                        }

                        int color = TexturyEnum.GREYSTONE.getTexturaData()[texY * 64 + texX];
                        pixels[index] = this.darkenColor(color, shadingFactor);

                    } else if (py < yBottom) { // Stena
                        double shadingFactor = Math.max(0, 1.0 - (dlzka / maxDlzka));

                        if (raycast[i].isHorizontalHit()) {
                            int texX = 0;
                            if (raycast[i].isDvereHit()) {
                                texX = (63 - (raycast[i].getFinalDlzkaX() % 64) + raycast[i].getDrawStep()) % 64;
                            } else {
                                texX = 63 - (raycast[i].getFinalDlzkaX() % 64);
                            }
                            double texY = (py - yTop) * (64.0f / (yBottom - yTop));
                            if (texY >= 0 && texY < 64) {
                                if (raycast[i].getTextura() != null) {
                                    int color = raycast[i].getTextura().getTexturaData()[(int)texY * 64 + texX];
                                    pixels[index] = this.darkenColor(color, shadingFactor);
                                } else {
                                    pixels[index] = colorV;
                                }
                            } else {
                                pixels[index] = colorV;
                            }
                        } else {
                            int texX = 0;
                            if (raycast[i].isDvereHit()) {
                                texX = (63 - (raycast[i].getFinalDlzkaY() % 64) + raycast[i].getDrawStep()) % 64;
                            } else {
                                texX = 63 - (raycast[i].getFinalDlzkaY() % 64);
                            }
                            double texY = (py - yTop) * (64.0f / (yBottom - yTop));
                            if (texY >= 0 && texY < 64) {
                                if (raycast[i].getTextura() != null) {
                                    int color = raycast[i].getTextura().getTexturaData()[(int)texY * 64 + texX];
                                    pixels[index] = this.darkenColor(color, shadingFactor);
                                } else {
                                    pixels[index] = colorV;
                                }
                            } else {
                                pixels[index] = colorV;
                            }
                        }

                    } else { // Podlaha
                        double straightDistance = (64.0 * this.skalovanie) / (2.0 * (py - horizon));
                        double realDistance = straightDistance / Math.cos(this.hrac.getHracAngle() - raycast[i].getFinalUhol());
                        double shadingFactor = Math.max(0, 1.0 - (realDistance / maxDlzka));

                        double floorX = this.hrac.getHracX() + realDistance * Math.cos(raycast[i].getFinalUhol());
                        double floorY = this.hrac.getHracY() - realDistance * Math.sin(raycast[i].getFinalUhol());

                        int texX = (int)(floorX % 64);
                        int texY = (int)(64 - (floorY % 64));
                        if (texX < 0) {
                            texX += 64;
                        }
                        if (texY < 0) {
                            texY += 64;
                        }
                        if (texX >= 64) {
                            texX -= 64;
                        }
                        if (texY >= 64) {
                            texY -= 64;
                        }

                        int color = TexturyEnum.GREYSTONE.getTexturaData()[texY * 64 + texX];
                        pixels[index] = this.darkenColor(color, shadingFactor);
                    }
                }
            }
        }
    }

    private void drawSprites(int[] pixels, ArrayList<SpriteData> visibleSprites) {
        if (visibleSprites == null || visibleSprites.isEmpty()) {
            return;
        }

        visibleSprites.sort((a, b) -> Double.compare(b.getDistance(), a.getDistance()));

        for (SpriteData spriteData : visibleSprites) {
            Sprite sprite = spriteData.getSprite();
            double spriteDlzka = spriteData.getDistance();
            double spriteAngle = spriteData.getAngle();

            double q = spriteAngle - this.hrac.getHracAngle();
            int spriteHeight = (int)(64.0 / spriteDlzka * this.skalovanie);
            int spriteWidth = spriteHeight;
            int screenX = (int)(this.sirka / 2 * (1 + Math.tan(q) / Math.tan(this.fov / 2)));
            int screenY = (this.vyska - this.hrac.getPitch()) / 2;


            double maxDlzka = 550;
            double shadingFactor = Math.max(0, 1.0 - (spriteDlzka / maxDlzka));

            for (int x = screenX - spriteWidth / 2; x < screenX + spriteWidth / 2; x++) {
                if (x < 0 || x >= this.sirka) {
                    continue;
                }

                // Prepočítaj rayIndex pre aktuálny pixel x
                int rayIndex = (int)((double)x / this.sirka * 320); // Priamo mapujeme x na rayIndex (0–319)
                double wallDistance = this.raycastManager.getWallDistances()[rayIndex];
                if (spriteDlzka >= wallDistance) {
                    continue; // Preskoč pixel, ak je stena bližšie
                }

                int texX = ((x - (screenX - spriteWidth / 2)) * 64 / spriteWidth);
                for (int y = screenY - spriteHeight / 2; y < screenY + spriteHeight / 2; y++) {
                    if (y < 0 || y >= this.vyska) {
                        continue;
                    }

                    int texY = (y - (screenY - spriteHeight / 2)) * 64 / spriteHeight;
                    if (texX >= 0 && texX < 64 && texY >= 0 && texY < 64) {
                        int index = y * this.sirka + x;
                        int color = sprite.getType().getTextura()[texY * 64 + texX];

                        //specialne pre soldier (rotatable sprity)
                        if (sprite instanceof Soldier) {
                            color = ((Soldier)sprite).updateTexture(this.hrac)[texY * 64 + texX];
                        }

                        if ((color != 0xFF000000) && (color != 0xFF980088)) {
                            pixels[index] = this.darkenColor(color, shadingFactor);
                        }
                    }
                }
            }
        }
    }


    private int darkenColor(int color, double factor) {
        int alpha = (color >> 24) & 0xFF; // Alfa (nemenim)
        int red = (int)(((color >> 16) & 0xFF) * factor); // R
        int green = (int)(((color >> 8) & 0xFF) * factor); // G
        int blue = (int)((color & 0xFF) * factor); // B


        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        // Zloženie farby späť do formátu ARGB
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
