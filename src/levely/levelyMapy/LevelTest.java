package levely.levelyMapy;

import engine.tools.Tools;
import levely.levelData.Level;
import sprites.SprityEnum;
import sprites.spriteData.AnimatedSprite;
import sprites.sprites.animatedSprites.Knight;
import sprites.sprites.entities.Soldier;
import sprites.sprites.staticSprites.Barrel;
import sprites.sprites.staticSprites.Kostlivec;
import sprites.sprites.staticSprites.Sebo;


public class LevelTest extends Level {

    public LevelTest() {
        super("levelTest.txt");
        super.getSprites().add(new Barrel(200, 240));
        super.getSprites().add(new Knight(Tools.spriteTileStred(5), Tools.spriteTileStred(5)));
        super.getSprites().add(new AnimatedSprite(800, 800, SprityEnum.EXPLOZIA));
        super.getSprites().add(new Soldier(600, 600));
    }
}
