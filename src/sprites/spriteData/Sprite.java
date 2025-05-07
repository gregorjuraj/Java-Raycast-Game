package sprites.spriteData;

import sprites.SprityEnum;

public class Sprite {
    private double x;
    private double y;
    private int[] texture;
    private SpriteData spriteData;

    public Sprite(double x, double y, SprityEnum sprityEnum) {
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
    public void setSpriteData(double distance, double angle, Sprite sprite) {
        this.spriteData = new SpriteData(distance, angle, sprite);
    }

    public void setTexture(int[] texture) {
        this.texture = texture;
    }

    public int[] getTexture() {
        return this.texture;
    }
}
