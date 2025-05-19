package sprites;

import engine.tools.Tools;

/**
 * Enum TexturyEnum definuje rôzne textúry.
 * Každá textúra má názov súboru, identifikačné číslo a dáta textúry načítané zo súboru.
 */
public enum TexturyEnum {
    DAY("day", 11),
    FLAG("flag", 12),
    GREYWALL("greywall", 13),
    GREYWALL2("greywall2", 14),
    GREYWALL3("greywall3", 15),
    GREYWALL_BIRD("greywall_bird", 16),
    GREYWALL_DECOR("greywall_decor", 17),
    METALDOOR("metaldoor", 18),
    MOSSY("mossy", 19),
    MOSSY2("mossy2", 20),
    NIGHT("night", 21),
    PAINT("paint", 22),
    WOOD("wood", 23),
    WOOD_BIRD("wood_bird", 24),
    WOOD_DECOR("wood_decor", 25),
    WOOD_PERSON("wood_person", 26),
    BLUEDOOR("bluedoor", 27),
    BLUEDOOR2("bluedoor2", 28),
    BLUESPECIAL("bluespecial", 29),
    BLUEWALL("bluewall", 30),
    BLUEWALL2("bluewall2", 31),
    BLUEWALL_CAGE("bluewall_cage", 32),
    BLUEWALL_CAGE2("bluewall_cage2", 33),
    BLUEWALL_DECOR("bluewall_decor", 34),
    BRICKS("bricks", 35),
    BRICKS_FANCY("bricksfancy", 36),
    BRICKSFLAG("bricksflag", 37),

    TEST("test", 99), //test textura
    DOORSIDE("bluedoorside", 10), //krajna textura pre dvere
    DOOR("bluedoormain", 9); //specialny pripad kde cislo 9 oznacuje dvere (ine spravanie ako stena)

    private final String nazovSuboru;
    private final int cislo; //identifikacne cislo textury, toto cislo potom pouzivam ked vytvaram mapy
    private final int[] textureData;

    /**
     * Konštruktor enumerácie TexturyEnum.
     * Inicializuje názov súboru, číslo textúry a načíta dáta textúry.
     *
     * @param nazovSuboru názov súboru textúry
     * @param cislo identifikačné číslo textúry
     */
    TexturyEnum(String nazovSuboru, int cislo) {
        this.nazovSuboru = nazovSuboru;
        this.cislo = cislo;
        this.textureData = this.loadTextury();
    }

    private int[] loadTextury() {
        return Tools.loadImageTexture("resources/walls/" + this.nazovSuboru + ".png", 0, 0, false);
    }

    public int[] getTexturaData() {
        return this.textureData;
    }

    public int getCislo() {
        return this.cislo;
    }
}