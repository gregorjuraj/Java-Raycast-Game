package sprites.spriteData;

import engine.tools.Tools;
import entity.Hrac;
import levely.levelData.Level;
import sprites.SprityEnum;
import sprites.SprityDefinition;

import java.util.HashMap;

/**
 * Trieda AnimatedSprite rozširuje triedu Sprite a pridáva funkcionalitu pre animované sprity.
 * Spravuje sekvenciu animácií a aktualizuje snímky pre zobrazenie animovaného spritu.
 */
public class AnimatedSprite extends Sprite {
    private HashMap<Integer, int[]> animSequence;
    private int pocetSnimkov; //celkovy pocet snimkov v animacii
    private int pocetSnimkovUpdate; //aktualne poradove cislo v animacii

    /**
     * Konštruktor triedy AnimatedSprite.
     * Inicializuje animovaný sprite s danou pozíciou a typom spritu. Načíta sekvenciu animácií z textúr,
     * ak je sprite definovaný ako animovaný.
     *
     * @param x X-ová súradnica pozície spritu
     * @param y Y-ová súradnica pozície spritu
     * @param sprityEnum typ spritu definovaný enumeráciou SprityEnum
     */
    public AnimatedSprite(double x, double y, SprityEnum sprityEnum) {
        super(x, y);
        this.pocetSnimkov = 0;
        this.animSequence = new HashMap<>();

        if (sprityEnum.getTyp() == SprityDefinition.ANIMATED) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int blankTextureCheck = 0;
                    int[] frame = Tools.loadImageTexture("resources/sprites/animatedTextures/" + sprityEnum.getNazovSuboru() + ".png", (j * 65), (i * 65), sprityEnum.isResized());
                    // Kontrola, ci obrazok nieje prazdny (cela textura = 0xFF980088)
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
        super.setTexture(this.animSequence.get(1)); // inicializacia
    }

    /**
     * Aktualizuje poradové číslo aktuálnej snímky animácie.
     * Ak je dosiahnutý posledný snímok, vráti sa na prvý.
     *
     * @return aktuálne poradové číslo snímky
     */
    private int updatePocetSnimkovUpdate() {
        if (this.pocetSnimkovUpdate < this.pocetSnimkov) {
            this.pocetSnimkovUpdate++;
        } else {
            this.pocetSnimkovUpdate = 1;
        }
        return this.pocetSnimkovUpdate;
    }

    @Override
    public void update(Level level, Hrac hrac) {
        this.setTexture(this.animSequence.get(this.updatePocetSnimkovUpdate()));
    }
}