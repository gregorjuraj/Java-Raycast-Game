package sprites;

/**
 * Enum SprityEnum definuje rôzne typy spritov použitých v hernom prostredí.
 * Každý sprite má názov súboru, typ (statický, animovaný, entita) a informáciu o zmene veľkosti.
 */
public enum SprityEnum {
    /** staticke sprity */
    BARREL("barrel", SprityDefinition.STATIC, false, 0),
    BULLET("bullet", SprityDefinition.STATIC, false, 0),
    DEADTREE("deadtree", SprityDefinition.STATIC, false, 0),
    FLAG("flag", SprityDefinition.STATIC, false, 0),
    KNIGHT("knight", SprityDefinition.STATIC, false, 0),
    KOSTLIVEC("kostlivec", SprityDefinition.STATIC, false, 0),
    LIGHT("light", SprityDefinition.STATIC, false, 0),
    LIGHT2("light2", SprityDefinition.STATIC, false, 0),
    PICKUP("pickup", SprityDefinition.STATIC, false, 20),
    PICKUP2("pickup2", SprityDefinition.STATIC, false, 40),
    PICKUP3("pickup3", SprityDefinition.STATIC, false, 60),
    PICKUP4("pickup4", SprityDefinition.STATIC, false, 100),
    PILLAR("pillar", SprityDefinition.STATIC, false, 0),
    SKELETON("skeleton", SprityDefinition.STATIC, false, 0),
    SKELETONPILE("skeletonpile", SprityDefinition.STATIC, false, 0),
    TABLE("table", SprityDefinition.STATIC, false, 0),
    TABLE2("table2", SprityDefinition.STATIC, false, 0),
    TREE("tree", SprityDefinition.STATIC, false, 0),
    WELL("well", SprityDefinition.STATIC, false, 0),

    /** animovane sprity */
    KNIGHTT("knight", SprityDefinition.ANIMATED, false, 0),
    EXPLOZIA("explozia", SprityDefinition.ANIMATED, false, 0),

    /** entity */
    SOLDIER("soldier", SprityDefinition.ENTITY, false, 0);

    private final String nazovSuboru;
    private final SprityDefinition typ;
    private final boolean resized;
    private final int score;

    /**
     * Konštruktor enumu SprityEnum.
     * Inicializuje názov súboru, typ spritu a informáciu o zmene veľkosti.
     *
     * @param nazovSuboru názov súboru textúry
     * @param typ typ spritu podľa enumerácie SprityDefinition
     * @param resized true, ak je textúra upravená na inú veľkosť, inak false
     */
    SprityEnum(String nazovSuboru, SprityDefinition typ, boolean resized, int score) {
        this.nazovSuboru = nazovSuboru;
        this.typ = typ;
        this.resized = resized;
        this.score = score;
    }

    public String getNazovSuboru() {
        return this.nazovSuboru;
    }

    public SprityDefinition getTyp() {
        return this.typ;
    }

    public boolean isResized() {
        return this.resized;
    }

    public int getScore() {
        return this.score;
    }
}