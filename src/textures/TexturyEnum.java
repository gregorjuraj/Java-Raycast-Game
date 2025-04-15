package textures;

import engine.tools.Tools;

public enum TexturyEnum {
    COLORSTONE("colorstone.png", 1),
    GREYSTONE("greystone.png", 2),
    MOSSY("mossy.png", 3),
    TEST("test.png", 4),
    TILE1("tile.jpg", 5),
    WOLFWALL("wolfwall.png", 6),
    DREVO("drevo.png", 7),
    DREVOHITLER("drevoHitler.png", 8),

    DOOR("door.png", 9),
    DOORSIDE("dvereBok.png", 10),

    DREVOVTAK("drevoVtak.png", 11);

    private final String nazovSuboru;
    private final int cislo;
    private final Tools tools;
    private final int[] textureData;

    TexturyEnum(String nazovSuboru, int cislo) {
        this.nazovSuboru = nazovSuboru;
        this.cislo = cislo;
        this.tools = new Tools();
        this.textureData = this.loadTextury();
    }

    private int[] loadTextury() {
        return this.tools.loadImageTexture("src/textures/" + this.nazovSuboru);
    }

    public int[] getTexturaData() {
        return this.textureData;
    }

    public int getCislo() {
        return this.cislo;
    }
}