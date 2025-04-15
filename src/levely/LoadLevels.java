package levely;

import levely.levelData.Level;
import levely.levelyMapy.LevelOne;
import levely.levelyMapy.LevelTest;

public class LoadLevels {
    private final Level[] levels;

    public LoadLevels() {
        this.levels = new Level[10];

        this.levels[0] = new LevelOne();
        this.levels[1] = new LevelTest();
    }

    public Level getLevels(int intLevel) {
        return this.levels[intLevel];
    }
}
