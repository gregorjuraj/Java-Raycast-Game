package sprites;

public enum SprityEnum {
    BARREL("barrel", SprityDefinition.STATIC,   false),
    STLP("stlp", SprityDefinition.STATIC,   false),
    SEBO("sebo", SprityDefinition.STATIC,   true),
    KOSTLIVEC("kostlivec", SprityDefinition.STATIC,   false),
    STRASIDLO("strasidlo", SprityDefinition.STATIC,   true),

    KNIGHT("knight", SprityDefinition.ANIMATED,   false),
    EXPLOZIA("explozia", SprityDefinition.ANIMATED,   false),

    SOLDIER("soldier", SprityDefinition.ENTITY,   false);



    private final String nazovSuboru;
    private final SprityDefinition typ;
    private final boolean resized;

    SprityEnum(String nazovSuboru, SprityDefinition typ, boolean resized) {
        this.nazovSuboru = nazovSuboru;
        this.typ = typ;
        this.resized = resized;
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
}
