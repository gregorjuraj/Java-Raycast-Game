package engine;

import engine.tools.Tools;
import entity.Hrac;
import levely.levelData.Level;
import sprites.spriteData.Sprite;
import sprites.spriteData.SpriteData;

import java.util.ArrayList;

/**
 * Trieda pre správu spritov v hernom svete, určuje viditeľné sprity a ich údaje pre vykreslenie.
 */
public class SpriteManager {
    /**
     * Zoznam viditeľných spritov v aktuálnom zornom poli hráča.
     */
    private ArrayList<SpriteData> visibleSprites;

    /**
     * Konštruktor pre inicializáciu manažéra spritov s prázdnym zoznamom.
     */
    public SpriteManager() {
        this.visibleSprites = new ArrayList<>();
    }

    /**
     * Aktualizuje zoznam viditeľných spritov na základe pozície hráča a úrovne.
     *
     * @param hrac Hráč, z ktorého pozície sa určuje viditeľnosť spritov
     * @param level Level, ktorý obsahuje sprity na spracovanie
     */
    public void update(Hrac hrac, Level level) {
        this.visibleSprites.clear();

        for (Sprite sprite : level.getSprites()) {
            //rozdiel medzi x a y suradnicami spritu a hraca, vektor
            double hx = sprite.getX() - hrac.getHracX();
            double hy = sprite.getY() - hrac.getHracY();

            double spriteUhol = Math.atan2(-hy, hx); // vypocita uhol (v radianoch) medzi vektorom od hraca k spritu a osou X
            spriteUhol += Tools.korekciaUhla(spriteUhol); //normalizacia uhlu

            double angleDiff = Math.abs(spriteUhol - hrac.getHracAngle()); //rozdiel uhlu medzi hracom a spritom
            if (angleDiff > Math.PI) {
                angleDiff = 2 * Math.PI - angleDiff; // Oprava prechodu cez 0/2π
            }

            if (angleDiff < 1) { // Sprite je v zornom poli
                double vzdialenost = Math.sqrt(hx * hx + hy * hy); //euklidovska vzdialenost pomocou pytagorovej vety :)
                sprite.setSpriteData(vzdialenost, spriteUhol, sprite);
                this.visibleSprites.add(sprite.getSpriteData());
            }
        }
    }

    public ArrayList<SpriteData> getVisibleSprites() {
        return this.visibleSprites;
    }
}