package sprites.spriteData;

import engine.tools.Tools;
import entity.Hrac;
import levely.levelData.Level;
import sprites.SprityEnum;

public class StaticSprite extends Sprite {

    private final int score;

    public StaticSprite(double x, double y, SprityEnum sprityEnum) {
        super(x, y);
        super.setTexture(Tools.loadImageTexture("resources/sprites/staticTextures/" + sprityEnum.getNazovSuboru() + ".png", 0, 0, sprityEnum.isResized()));
        this.score = sprityEnum.getScore();
    }

    @Override
    public void update(Level level, Hrac hrac) {
        if (this.score > 0) {
            if ( (hrac.getHracX() / 64 == (int)super.getX() / 64) && (hrac.getHracY() / 64 == (int)super.getY() / 64)) {
                hrac.setScoreValue(this.score);
                super.beRemoved();
            }
        }
    }

}
