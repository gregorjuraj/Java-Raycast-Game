package engine;

import textures.TexturyEnum;

import java.util.HashMap;

public class TextureManager {
    private static final HashMap<Integer, TexturyEnum> TEXTURA_BY_CISLO = new HashMap<>();

    static {
        for (TexturyEnum textura : TexturyEnum.values()) {
            TEXTURA_BY_CISLO.put(textura.getCislo(), textura);
        }
    }

    public static TexturyEnum getTexturaZEnumu(int cisloTextury) {
        return TEXTURA_BY_CISLO.get(cisloTextury);
    }
}