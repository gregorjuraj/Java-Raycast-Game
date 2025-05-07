package sprites.spriteData;

import engine.tools.Tools;
import sprites.SprityDefinition;
import sprites.SprityEnum;

import java.util.ArrayList;
import java.util.HashMap;

public class EntitySprite extends Sprite {
    private HashMap<String, ArrayList<int[]>> entityTextures;

    public EntitySprite(double x, double y, SprityEnum sprityEnum) {
        super(x, y, sprityEnum);

        this.entityTextures = new HashMap<>();

        if (sprityEnum.getTyp() == SprityDefinition.ENTITY) {
            for (int i = 0; i < 8; i++) {
                ArrayList<int[]> sekvencia = new ArrayList<>();
                for (int j = 0; j < 8; j++) {
                    int blankTextureCheck = 0;
                    int[] frame = Tools.loadImageTexture("src/sprites/textures/entities/" + sprityEnum.getNazovSuboru() + ".png", (i * 65), (j * 65), sprityEnum.isResized());
                    for (int pixel : frame) {
                        if (pixel == 0xFF980088) {
                            blankTextureCheck++;
                        }
                    }
                    if (blankTextureCheck != 4096) {
                        sekvencia.add(frame);
                    }
                }

                switch (i) {
                    case 0:
                        this.entityTextures.put("front", sekvencia);
                        break;
                    case 1:
                        this.entityTextures.put("left", sekvencia);
                        break;
                    case 2:
                        this.entityTextures.put("back", sekvencia);
                        break;
                    case 3:
                        this.entityTextures.put("right", sekvencia);
                        break;
                    case 4:
                        this.entityTextures.put("death", sekvencia);
                        break;
                    case 5:
                        this.entityTextures.put("shoot", sekvencia);
                        break;
                    case 6:
                        this.entityTextures.put("misc", sekvencia);
                        break;
                }
            }
            this.setTexture(this.entityTextures.get("front").getFirst());
        }

    }

    public HashMap<String, ArrayList<int[]>> getEntityTextures() {
        return this.entityTextures;
    }
}
