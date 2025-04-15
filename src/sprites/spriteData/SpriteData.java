package sprites.spriteData;

public class SpriteData {
    private double distance;
    private double angle;
    private Sprite sprite;

    SpriteData(double distance, double angle, Sprite sprite) {
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

