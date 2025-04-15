package levely.levelyMapy;

import engine.tools.Tools;
import levely.levelData.Level;
import sprites.noRotation.Barrel;
import sprites.rotation.Soldier;

public class LevelTest extends Level {
    private Tools tools;

    public LevelTest() {
        super("levelTest.txt");
        this.tools = new Tools();
        super.getSprites().add(new Barrel(this.tools.spriteTileStred(2), this.tools.spriteTileStred(2)));
        super.getSprites().add(new Soldier(this.tools.spriteTileStred(4), this.tools.spriteTileStred(4)));
    }
}
