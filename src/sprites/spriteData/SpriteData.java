package sprites.spriteData;

/**
 * Trieda SpriteData uchováva údaje o sprite, vrátane jeho vzdialenosti, uhla a odkazu na objekt Sprite.
 * Používa sa na správu informácií o sprite v hernom prostredí.
 */
public class SpriteData {
    private double distance; // vzdialenost od hraca
    private double angle;
    private Sprite sprite;

    /**
     * Konštruktor triedy SpriteData.
     * Inicializuje údaje o sprite s danou vzdialenosťou, uhlom a objektom Sprite.
     *
     * @param distance vzdialenosť spritu od hráča
     * @param angle uhol spritu voči hráčovi (v radiánoch)
     * @param sprite odkaz na objekt Sprite
     */
    public SpriteData(double distance, double angle, Sprite sprite) {
        this.distance = distance;
        this.angle = angle;
        this.sprite = sprite;
    }

    public double getDistance() {
        return this.distance;
    }

    public double getAngle() {
        return this.angle;
    }

    public Sprite getSprite() {
        return this.sprite;
    }
}