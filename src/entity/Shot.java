package entity;

import engine.tools.Tools;
import levely.levelData.Level;
import sprites.spriteData.Sprite;
import sprites.sprites.entities.Soldier;
import sprites.sprites.entities.State;
import sprites.sprites.staticSprites.Bullet;

/**
 * Trieda reprezentujúca výstrel hráča.
 * Vytvára projektil a spracováva zásahy nepriateľov.
 */
public class Shot {
    /**
     * Konštruktor výstrelu.
     * Vytvorí nový projektil na pozícii hráča a kontroluje, či zasiahne nepriateľov v leveli.
     * @param hrac Hráč, ktorý výstrel inicioval
     * @param level Aktuálny herný level
     */
    public Shot(Hrac hrac, Level level) {
        var bullet = new Bullet(hrac.getHracX(), hrac.getHracY(), hrac);
        level.getSprites().add(bullet);

        // Kontrola kolizie
        for (Sprite sprite : level.getSprites()) {
            if (sprite instanceof Soldier) {
                if (((Soldier)sprite).getState() != State.DEAD) {
                    double hx = sprite.getX() - bullet.getX();
                    double hy = sprite.getY() - bullet.getY();
                    double spriteUhol = Math.atan2(-hy, hx);
                    spriteUhol += Tools.korekciaUhla(spriteUhol);

                    double angleDiff = Math.abs(spriteUhol - bullet.getBulletAngle());
                    if (angleDiff > Math.PI) {
                        angleDiff = 2 * Math.PI - angleDiff;
                    }
                    if (angleDiff < 0.05) {
                        ((Soldier)sprite).hit();
                    }
                }
            }
        }
    }
}