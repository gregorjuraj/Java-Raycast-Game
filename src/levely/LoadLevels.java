package levely;

import engine.tools.Tools;
import levely.levelData.Level;
import sprites.SprityEnum;
import sprites.spriteData.AnimatedSprite;
import sprites.spriteData.StaticSprite;
import sprites.sprites.entities.Soldier;

/**
 * Trieda na načítanie a správu viacerých herných levelov.
 * Uchováva pole levelov a umožňuje prístup k nim podľa indexu.
 */
public class LoadLevels {
    private final Level[] levels;

    /**
     * Konštruktor pre načítanie levelov.
     * Inicializuje pole levelov a načíta preddefinované levely zo súborov.
     */
    public LoadLevels() {
        this.levels = new Level[10];

        this.levels[0] = new Level("levelTest.txt");
        this.levels[0].getSprites().add(new Soldier(700, 400));
        this.levels[0].getSprites().add(new StaticSprite(158, 220, SprityEnum.FLAG));
        this.levels[0].getSprites().add(new StaticSprite(100, 220, SprityEnum.PICKUP4));
        this.levels[0].getSprites().add(new StaticSprite(Tools.spriteTileStred(5), Tools.spriteTileStred(1), SprityEnum.SKELETON));
        this.levels[0].getSprites().add(new StaticSprite(Tools.spriteTileStred(7), Tools.spriteTileStred(1), SprityEnum.SKELETONPILE));
        this.levels[0].getSprites().add(new StaticSprite(Tools.spriteTileStred(6), Tools.spriteTileStred(3), SprityEnum.PILLAR));
        this.levels[0].getSprites().add(new StaticSprite(277, 233, SprityEnum.PICKUP2));
        this.levels[0].getSprites().add(new StaticSprite(Tools.spriteTileStred(15), Tools.spriteTileStred(2), SprityEnum.TABLE2));
        this.levels[0].getSprites().add(new StaticSprite(Tools.spriteTileStred(13), Tools.spriteTileStred(1), SprityEnum.BARREL));
        this.levels[0].getSprites().add(new AnimatedSprite(Tools.spriteTileStred(13), Tools.spriteTileStred(8), SprityEnum.EXPLOZIA));

        this.levels[1] = new Level("levelOne.txt");
    }

    public Level getLevels(int intLevel) {
        return this.levels[intLevel];
    }
}