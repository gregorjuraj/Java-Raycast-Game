package sprites.spriteData;

import engine.tools.Tools;
import sprites.IScore;
import sprites.SprityEnum;

public class StaticSprite extends Sprite implements IScore {

    private final int score;

    public StaticSprite(double x, double y, SprityEnum sprityEnum) {
        super(x, y);
        super.setTexture(Tools.loadImageTexture("resources/sprites/staticTextures/" + sprityEnum.getNazovSuboru() + ".png", 0, 0, sprityEnum.isResized()));
        this.score = sprityEnum.getScore();
    }

    @Override
    public int kolkoScore() {
        return this.score;
    }
}
