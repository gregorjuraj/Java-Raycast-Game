package sprites;

import engine.tools.Tools;

public enum SprityEnum {
    BARREL("barrel.png", false, false),
    STLP("stlp.png", false, false ),
    SEBO("sebo.jpg", false, false),
    KOSTLIVEC("kostlivec.png", false, false),

    SOLDIER("soldier", false, true);

    private String nazovSuboru;
    private int[] textureData;
    private int[] textureDataBack;
    private int[] textureDataLeft;
    private int[] textureDataRight;

    SprityEnum(String nazovSuboru, Boolean animated, Boolean entity ) {
        this.nazovSuboru = nazovSuboru;

        if (!entity) {
            this.textureData = this.loadTextury(entity, animated, 0);
        } else {
            this.textureData = this.loadTextury(entity, animated, 1);
            this.textureDataBack = this.loadTextury(entity, animated, 2);
            this.textureDataRight = this.loadTextury(entity, animated, 3);
            this.textureDataLeft = this.loadTextury(entity, animated, 4);
        }

    }

    private int[] loadTextury(boolean entity, boolean animated, int phase) {
        if (!entity) {
            if (!animated) {
                return Tools.loadImageTexture("src/sprites/noRotation/textures/" + this.nazovSuboru);
            } else {
                return Tools.loadImageTexture("src/sprites/noRotation/textures/" + this.nazovSuboru);
            }
        } else {
            switch (phase) {
                case 1:
                    return Tools.loadImageTexture("src/sprites/rotation/textures/" + this.nazovSuboru + "Front.png");
                case 2:
                    return Tools.loadImageTexture("src/sprites/rotation/textures/" + this.nazovSuboru + "Back.png");
                case 3:
                    return Tools.loadImageTexture("src/sprites/rotation/textures/" + this.nazovSuboru + "Right.png");
                case 4:
                    return Tools.loadImageTexture("src/sprites/rotation/textures/" + this.nazovSuboru + "Left.png");
                default:
                    System.out.println("test");
                    return null;
            }
        }
    }

    public int[] getTextura() {
        return this.textureData;
    }

    public int[] getTextureBack() {
        return this.textureDataBack;
    }

    public int[] getTextureLeft() {
        return this.textureDataLeft;
    }

    public int[] getTextureRight() {
        return this.textureDataRight;
    }
}
