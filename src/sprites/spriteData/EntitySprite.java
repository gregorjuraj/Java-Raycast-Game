package sprites.spriteData;

import engine.tools.Tools;

import sprites.SprityDefinition;
import sprites.SprityEnum;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trieda EntitySprite rozširuje triedu Sprite a spravuje textúry pre entitu s rôznymi animáciami.
 * Uchováva sekvencie textúr pre rôzne smery a stavy dannej entity.
 */
public abstract class EntitySprite extends Sprite {
    private HashMap<String, ArrayList<int[]>> entityTextures;

    /**
     * Konštruktor triedy EntitySprite.
     * Inicializuje entitu s danou pozíciou a typom spritu. Načíta textúry pre rôzne smery a stavy,
     * ak je sprite definovaný ako entita.
     *
     * @param x X-ová súradnica pozície entity
     * @param y Y-ová súradnica pozície entity
     * @param sprityEnum typ spritu definovaný enumeráciou SprityEnum
     */
    public EntitySprite(double x, double y, SprityEnum sprityEnum) {
        super(x, y);
        this.entityTextures = new HashMap<>();

        if (sprityEnum.getTyp() == SprityDefinition.ENTITY) {
            for (int i = 0; i < 8; i++) {
                ArrayList<int[]> sekvencia = new ArrayList<>();
                for (int j = 0; j < 8; j++) {
                    int blankTextureCheck = 0;
                    int[] frame = Tools.loadImageTexture("resources/sprites/entities/" + sprityEnum.getNazovSuboru() + ".png", (i * 65), (j * 65), sprityEnum.isResized());
                    // Kontrola, ci obrazok nieje prazdny (cela textura = 0xFF980088)
                    for (int pixel : frame) {
                        if (pixel == 0xFF980088) {
                            blankTextureCheck++;
                        }
                    }
                    if (blankTextureCheck != 4096) {
                        sekvencia.add(frame);
                    }
                }

                // priradenie textury pre konkretny stav
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
            super.setTexture(this.entityTextures.get("front").getFirst());
        }
    }

    public HashMap<String, ArrayList<int[]>> getEntityTextures() {
        return this.entityTextures;
    }
}