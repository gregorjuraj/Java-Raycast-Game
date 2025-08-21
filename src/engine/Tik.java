package engine;

import entity.Hrac;
import levely.levelData.Level;
import sprites.spriteData.Sprite;

import java.util.ArrayList;

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
        ArrayList<Sprite> spritesToRemove = new ArrayList<>();

        for (Sprite sprite : level.getSprites()) {
            sprite.update(level, hrac);
            if (sprite.isShouldRemove()) {
                spritesToRemove.add(sprite);
            }
        }
        level.getSprites().removeAll(spritesToRemove);
    }
}