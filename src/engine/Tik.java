package engine;

import entity.Hrac;
import levely.levelData.Level;
import sprites.IScore;
import sprites.spriteData.AnimatedSprite;
import sprites.spriteData.Sprite;
import sprites.sprites.entities.Soldier;
import sprites.sprites.staticSprites.Bullet;

/**
 * Trieda pre správu herného cyklu (tick), ktorá aktualizuje stavy spritov v leveli.
 */
public class Tik {

    /**
     * Aktualizuje stavy všetkých spritov v leveli na základe pozície hráča.
     *
     * @param level Úroveň, ktorá obsahuje sprity na aktualizáciu
     * @param hrac Hráč, ktorého pozícia ovplyvňuje správanie spritov
     */
    public void update(Level level, Hrac hrac) {
        Sprite bulletToRemove = null;
        Sprite spriteToRemove = null;

        for (Sprite sprite : level.getSprites()) {
            if (sprite instanceof AnimatedSprite) {
                sprite.setTexture(((AnimatedSprite)sprite).getAnimSequence().get(((AnimatedSprite)sprite).updatePocetSnimkovUpdate()));
            }
            if (sprite instanceof Soldier) {
                ((Soldier)sprite).updateTexture(hrac);
                ((Soldier)sprite).updatePhase(level, hrac);
            }
            if (sprite instanceof IScore && ((IScore)sprite).kolkoScore() > 0) {
                if ( (hrac.getHracX() / 64 == (int)sprite.getX() / 64) && (hrac.getHracY() / 64 == (int)sprite.getY() / 64)) {
                    hrac.setScoreValue(((IScore)sprite).kolkoScore());
                    spriteToRemove = sprite;
                }
            }
            if (sprite instanceof Bullet) {
                sprite.setX(sprite.getX() + 50 * Math.cos(((Bullet)sprite).getBulletAngle()));
                sprite.setY(sprite.getY() - 50 * Math.sin(((Bullet)sprite).getBulletAngle()));
                if (((Bullet)sprite).getDolet()) {
                    bulletToRemove = sprite;
                }
            }
        }
        if (bulletToRemove != null) {
            level.getSprites().remove(bulletToRemove);
        }
        if (spriteToRemove != null) {
            level.getSprites().remove(spriteToRemove);
        }
    }
}