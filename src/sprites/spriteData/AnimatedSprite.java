package sprites.spriteData;

import engine.tools.Tools;
import sprites.SprityEnum;
import sprites.SprityDefinition;

import java.util.HashMap;

public class AnimatedSprite extends Sprite {
    private HashMap<Integer, int[]> animSequence;
    private int pocetSnimkov;
    private int pocetSnimkovUpdate;

    public AnimatedSprite(double x, double y, SprityEnum sprityEnum) {
        super(x, y, sprityEnum);
        this.pocetSnimkov = 0;

        this.animSequence = new HashMap<>();
        if (sprityEnum.getTyp() == SprityDefinition.ANIMATED) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int blankTextureCheck = 0;
                    int[] frame = Tools.loadImageTexture("src/sprites/textures/animatedTextures/" + sprityEnum.getNazovSuboru() + ".png", (j * 65), (i * 65), sprityEnum.isResized());
                    for (int pixel : frame) {
                        if (pixel == 0xFF980088) {
                            blankTextureCheck++;
                        }
                    }
                    if (blankTextureCheck != 4096) {
                        this.pocetSnimkov++;
                        this.animSequence.put(this.pocetSnimkov, frame);
                    }
                }
            }
        }
        this.pocetSnimkovUpdate = 1;
        this.setTexture(this.animSequence.get(1)); // pri inicializacii prva textura v poradi.
    }

    public int updatePocetSnimkovUpdate() {

        if (this.pocetSnimkovUpdate < this.pocetSnimkov) {
            this.pocetSnimkovUpdate++;
        } else {
            this.pocetSnimkovUpdate = 1;
        }
        return this.pocetSnimkovUpdate;
    }

    public HashMap<Integer, int[]> getAnimSequence() {
        return this.animSequence;
    }
}
