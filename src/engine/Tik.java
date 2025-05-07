package engine;

import entity.Hrac;
import levely.levelData.Level;
import sprites.spriteData.AnimatedSprite;
import sprites.spriteData.EntitySprite;
import sprites.spriteData.Sprite;
import sprites.sprites.entities.Soldier;

public class Tik {

    public Tik() {
    }

    public void update(Level level, Hrac hrac) {
        for (Sprite sprite : level.getSprites()) {
            if (sprite instanceof AnimatedSprite) {
                sprite.setTexture(((AnimatedSprite)sprite).getAnimSequence().get(((AnimatedSprite)sprite).updatePocetSnimkovUpdate()));
            }
            if (sprite instanceof Soldier) {
               ((Soldier)sprite).updateTexture(hrac);
            }
        }
    }
}
