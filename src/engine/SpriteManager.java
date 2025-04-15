package engine;

import engine.tools.Tools;
import entity.Hrac;
import levely.levelData.Level;
import sprites.spriteData.Sprite;
import sprites.spriteData.SpriteData;

import java.util.ArrayList;

public class SpriteManager {
    private ArrayList<SpriteData> visibleSprites;

    public SpriteManager() {
        this.visibleSprites = new ArrayList<>();
    }

    public void update(Hrac hrac, Level levelOne) {
        this.visibleSprites.clear();

        for (Sprite sprite : levelOne.getSprites()) {
            double hx = sprite.getX() - hrac.getHracX();
            double hy = sprite.getY() - hrac.getHracY();
            double spriteUhol = Math.atan2(-hy, hx);
            spriteUhol += Tools.korekciaUhla(spriteUhol);

            double angleDiff = Math.abs(spriteUhol - hrac.getHracAngle());
            if (angleDiff > Math.PI) {
                angleDiff = 2 * Math.PI - angleDiff; // Oprava prechodu cez 0/2π
            }


            //System.out.println(angleDiff < 1);
            if (angleDiff < 1) { // Sprite je v zornom poli
                // System.out.println("vidim");
                double vzdialenost = Math.sqrt(hx * hx + hy * hy);
                sprite.setSpriteData(vzdialenost, spriteUhol, sprite);
                this.visibleSprites.add(sprite.getSpriteData());
            }
        }
    }

    public ArrayList<SpriteData> getVisibleSprites() {
        return this.visibleSprites;
    }

}
