package engine;

import sprites.spriteData.Sprite;
import sprites.spriteData.SpriteData;
import sprites.TexturyEnum;
import entity.Hrac;
import levely.levelData.Level;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

/**
 * Trieda pre vykresľovanie hernej scény, vrátane stien, podlahy, stropu a spritov.
 */
public class Kreslenie {
    private final Hrac hrac;
    private final RaycastManager raycastManager;
    private final SpriteManager spriteManager;

    /**
     * Šírka obrazovky v pixeloch.
     */
    private final int sirka = 320; //720
    /**
     * Výška obrazovky v pixeloch.
     */
    private final int vyska = 200; //480
    /**
     * Faktor škálovania pre výpočet výšky stien.
     */
    private final int skalovanie = 277; //600
    /**
     * Zorné pole (FOV) v radiánoch (60 stupňov).
     */
    private final double fov = 1.04719755; // 60 stupnov
    private int offset;

    /**
     * Konštruktor pre inicializáciu kreslenia s hráčom.
     *
     * @param hrac Hráč, z pohľadu ktorého sa scéna vykresľuje
     */
    public Kreslenie(Hrac hrac) {
        this.hrac = hrac;
        this.raycastManager = new RaycastManager();
        this.spriteManager = new SpriteManager();
        this.offset = 0;
    }

    /**
     * Aktualizuje raycasting a sprity na základe aktuálnej úrovne a pozície hráča.
     *
     * @param level Level, v ktorom sa vykresľovanie uskutočňuje
     */
    public void update(Level level) {
        this.raycastManager.update(this.hrac, level, this);
        this.spriteManager.update(this.hrac, level);
    }

    /**
     * Vykreslí scénu na obrazovku pomocou poskytnutého Graphics2D objektu.
     *
     * @param g2 Objekt Graphics2D na vykreslenie grafiky
     */
    public void draw(Graphics2D g2) {
        BufferedImage screen = new BufferedImage(this.sirka, this.vyska, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((DataBufferInt)screen.getRaster().getDataBuffer()).getData();

        this.drawCeilingWallFloor(pixels);
        this.drawSprites(pixels, this.spriteManager.getVisibleSprites());

        g2.drawImage(screen, 0, 0, 1280, 720, null);

        boolean debug = false;
        if (debug) {
            for (int i = 0; i < 320; i++) {
                if (i == 160) {
                    g2.setColor(Color.red);
                    g2.drawLine(this.hrac.getHracX() + 5, this.hrac.getHracY() + 5, this.raycastManager.getRaycast()[i].getFinalDlzkaX(), this.raycastManager.getRaycast()[i].getFinalDlzkaY());
                    //System.out.println(this.raycastManager.getRaycast()[i].getFinalUhol());
                }
            }
        }
    }

    /**
     * Vykreslí strop, steny a podlahu na obrazovku.
     *
     * @param pixels Pole pixelov pre vykreslenie
     */
    private void drawCeilingWallFloor(int[] pixels) {
        double rectWidth = this.sirka / 320.0; //sirka jedneho vertikalneho pásu
        int horizon = (this.vyska - this.hrac.getPitch()) / 2; //urcenie horizontalnej ciary ktora urcuje stred obrazovky (predeluje na hornu a dolnu cast)
        Calc[] raycast = this.raycastManager.getRaycast(); //ziskanie udajov z lucov

        for (int i = 0; i < 320; i++) {
            int xStart = (int)(i * rectWidth);
            int xEnd = (int)((i + 1) * rectWidth);

            // vypocet vysky steny
            double dlzka = raycast[i].getFinalDlzka() * Math.cos(this.hrac.getHracAngle() - raycast[i].getFinalUhol()); // fish-eye efekt oprava
            int height = (int)(64.0 / dlzka * this.skalovanie);
            int halfHeight = height / 2;
            int yTop = horizon - halfHeight;
            int yBottom = horizon + halfHeight;

            int colorV = 0xFFFF00FF; // Magenta
            double maxDlzka = 800; //max vzdialenost pokial hrac vidi (viac ako 800 = cierna)

            for (int py = 0; py < this.vyska; py++) {
                for (int px = xStart; px < xEnd; px++) {
                    int index = py * this.sirka + px;
                    if (index < 0 || index >= pixels.length) {
                        continue;
                    }

                    if (py < yTop) { // Strop
                        double straightDistance = (64.0 * this.skalovanie) / (2.0 * (horizon - py)); //vyska stropu v jednotkach herneho sveta * skalovanie / vzdialenost od horizontu po aktualny pixel
                        double realDistance = straightDistance / Math.cos(this.hrac.getHracAngle() - raycast[i].getFinalUhol()); // fish-eye efekt oprava
                        double shadingFactor = Math.max(0, 1.0 - (realDistance / maxDlzka));

                        double ceilX = this.hrac.getHracX() + realDistance * Math.cos(raycast[i].getFinalUhol()); //vypocet pozicie bodu na strope v hernom svete (2D mapa)
                        double ceilY = this.hrac.getHracY() - realDistance * Math.sin(raycast[i].getFinalUhol());

                        int texX = (int)(ceilX % 64); //suradnice textury modulo 64 aby hodnota bola v rozsahu 0-63 (textura je velkosti 64x64)
                        int texY = (int)(ceilY % 64);
                        if (texX < 0) {
                            texX += 64;
                        }
                        if (texY < 0) {
                            texY += 64;
                        }

                        int color = TexturyEnum.GREYWALL.getTexturaData()[texY * 64 + texX];
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
                            double texY = (py - yTop) * (64.0 / (yBottom - yTop));
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
                            double texY = (py - yTop) * (64.0 / (yBottom - yTop));
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

                        int color = TexturyEnum.GREYWALL.getTexturaData()[texY * 64 + texX];
                        pixels[index] = this.darkenColor(color, shadingFactor);
                    }
                }
            }
        }
    }

    /**
     * Vykreslí viditeľné sprity na obrazovku s ohľadom na vzdialenosť a uhly.
     *
     * @param pixels Pole pixelov pre vykreslenie
     * @param visibleSprites Zoznam viditeľných spritov
     */
    private void drawSprites(int[] pixels, ArrayList<SpriteData> visibleSprites) {
        if (visibleSprites == null || visibleSprites.isEmpty()) {
            return;
        }

        visibleSprites.sort((a, b) -> Double.compare(b.getDistance(), a.getDistance()));

        for (SpriteData spriteData : visibleSprites) {
            Sprite sprite = spriteData.getSprite();
            double spriteDlzka = spriteData.getDistance();
            double spriteAngle = spriteData.getAngle();

            double q = spriteAngle - this.hrac.getHracAngle(); // Rozdiel medzi uhlom spritu a uhlom pohladu hraca. urcuje, kde na obrazovke (vlavo/vpravo) bude sprite umiestneny.
            int spriteHeight = (int)(64.0 / spriteDlzka * this.skalovanie);
            int spriteWidth = spriteHeight;
            int screenX = (int)(this.sirka / 2 * (1 + Math.tan(q) / Math.tan(this.fov / 2))); //??? tento vzorec proste funguje... (premietanie relativneho uhlu q na obrazovku)
            int screenY = (this.vyska - this.hrac.getPitch()) / 2;

            double maxDlzka = 550;
            double shadingFactor = Math.max(0, 1.0 - (spriteDlzka / maxDlzka));

            for (int x = screenX - spriteWidth / 2; x < screenX + spriteWidth / 2; x++) { //iteracia cez pixely v sirke spritu
                if (x < 0 || x >= this.sirka) {
                    continue;
                }

                //samotne vykreslovanie pixel po pixeli na obrazovku

                // Prepočítaj rayIndex pre aktuálny pixel x
                int rayIndex = (int)((double)x / this.sirka * 320); // prepocet ktory z lucov priamo zodpoveda pixelu x
                double wallDistance = this.raycastManager.getWallDistances()[rayIndex]; //z daneho lucu ziska dlzku
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
                        int color = sprite.getTexture()[texY * 64 + texX];
                        // vynimka pre tieto dve farby ktore nebudem vykreslovat
                        if ((color != 0xFF980088)) {
                            pixels[index] = this.darkenColor(color, shadingFactor);
                        }
                    }
                }
            }
        }
    }

    /**
     * Ztmaví farbu podľa zadaného faktora tieňovania.
     *
     * @param color Pôvodná farba vo formáte ARGB
     * @param factor Faktor tieňovania (0.0 až 1.0)
     * @return Ztmavená farba vo formáte ARGB
     */
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

    /**
     * Nastaví posun (offset) pre vykresľovanie.
     *
     * @param offset Hodnota posunu
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
}