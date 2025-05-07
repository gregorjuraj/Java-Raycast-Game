package levely.levelyMapy;

import levely.levelData.Level;
import sprites.sprites.staticSprites.Barrel;
import sprites.sprites.staticSprites.Kostlivec;
import sprites.sprites.staticSprites.Sebo;

public class LevelOne extends Level {

    public LevelOne() {
        super("levelOne.txt");
        super.getSprites().add(new Barrel(450, 250));
        super.getSprites().add(new Sebo(500, 250));
        super.getSprites().add(new Kostlivec(222, 430));
    }
}