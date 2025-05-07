package sprites.spriteData;

import engine.tools.Tools;
import sprites.SprityEnum;

public class StaticSprite extends Sprite {

    public StaticSprite(double x, double y, SprityEnum sprityEnum) {
        super(x, y, sprityEnum);
        super.setTexture(Tools.loadImageTexture("src/sprites/textures/staticTextures/" + sprityEnum.getNazovSuboru() + ".png", 0, 0, sprityEnum.isResized()));
    }
}
