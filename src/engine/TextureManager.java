package engine;

import sprites.TexturyEnum;

import java.util.HashMap;

/**
 * Trieda pre správu textúr v hernom engine, poskytuje prístup k textúram na základe ich číselného identifikátora.
 */
public class TextureManager {
    /**
     * Statická mapa na uchovávanie asociácie medzi číslom textúry a jej enum hodnotou.
     */
    private static final HashMap<Integer, TexturyEnum> TEXTURA_BY_CISLO = new HashMap<>();

    static {
        for (TexturyEnum textura : TexturyEnum.values()) {
            TEXTURA_BY_CISLO.put(textura.getCislo(), textura);
        }
    }

    /**
     * Vráti textúru zodpovedajúcu zadanému číslu textúry.
     *
     * @param cisloTextury Číselný identifikátor textúry
     * @return Objekt TexturyEnum zodpovedajúci číslu textúry, alebo null, ak taká textúra neexistuje
     */
    public static TexturyEnum getTexturaZEnumu(int cisloTextury) {
        return TEXTURA_BY_CISLO.get(cisloTextury);
    }
}