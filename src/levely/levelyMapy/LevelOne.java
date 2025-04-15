package levely.levelyMapy;

import levely.levelData.Level;
import sprites.noRotation.Barrel;
import sprites.noRotation.Kostlivec;
import sprites.noRotation.Sebo;

public class LevelOne extends Level {

    public LevelOne() {
        super("levelOne.txt");
        super.getSprites().add(new Barrel(450, 250));
        super.getSprites().add(new Sebo(500, 250));
        super.getSprites().add(new Kostlivec(222, 430));
    }
}