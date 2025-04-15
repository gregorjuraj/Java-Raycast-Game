package engine;

import entity.Hrac;
import levely.levelData.Level;
import sprites.rotation.Soldier;
import sprites.spriteData.Sprite;

public class Tik {
    public void update(Level level, Hrac hrac) {
        for (Sprite sprite : level.getSprites()) {

            // pohyb enemies
            if (sprite instanceof Soldier) {
                ((Soldier)sprite).updatePhase(level, hrac);
            }


        }
    }
}
