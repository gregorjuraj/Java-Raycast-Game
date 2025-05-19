package sprites.spriteData;

/**
 * Trieda Sprite reprezentuje herný sprite s pozíciou, textúrou a údajmi o sprite.
 * Slúži na uchovávanie a manipuláciu s informáciami o sprite v hernom prostredí.
 */
public class Sprite {
    //x, y v 2D mape
    private double x;
    private double y;
    private int[] texture;
    private SpriteData spriteData;

    /**
     * Konštruktor triedy Sprite.
     * Inicializuje sprite s danou pozíciou.
     *
     * @param x X-ová súradnica pozície spritu
     * @param y Y-ová súradnica pozície spritu
     */
    public Sprite(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public SpriteData getSpriteData() {
        return this.spriteData;
    }

    /**
     * Nastavuje údaje o sprite vytvorením nového objektu SpriteData.
     *
     * @param distance vzdialenosť spritu od hráča
     * @param angle uhol spritu voči hráčovi (v radiánoch)
     * @param sprite odkaz na objekt Sprite
     */
    public void setSpriteData(double distance, double angle, Sprite sprite) {
        this.spriteData = new SpriteData(distance, angle, sprite);
    }

    /**
     * Nastavuje textúru spritu.
     *
     * @param texture pole obsahujúce dáta textúry
     */
    public void setTexture(int[] texture) {
        this.texture = texture;
    }

    public int[] getTexture() {
        return this.texture;
    }
}