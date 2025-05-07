package sprites.sprites.entities;

import engine.tools.Tools;
import entity.Hrac;
import levely.levelData.Level;
import sprites.SprityEnum;
import sprites.spriteData.EntitySprite;

public class Soldier extends EntitySprite {
    private double viewAngle;
    private State state;
    private double rychlost;
    private int patrolDirection;

    private int moveTimer;

    public Soldier(double x, double y) {
        super(x, y, SprityEnum.SOLDIER);
        this.setDefaultValues();
    }

    private void setDefaultValues() {
        this.viewAngle = Math.toRadians(180);
        this.state = State.PATROL;
        this.rychlost = 4;
        this.patrolDirection = 1;
    }

    public void updateTexture(Hrac hrac) {
        double delta = this.viewAngle - hrac.getHracAngle();
        delta += Tools.korekciaUhla(delta);
        delta = Math.toDegrees(delta);
        if (delta > 315 || delta <= 45) {
            this.setTexture(super.getEntityTextures().get("front").getFirst());
        } else if (delta > 45 && delta <= 135) {
            this.setTexture(super.getEntityTextures().get("left").getFirst());
        } else if (delta > 135 && delta <= 225) {
            this.setTexture(super.getEntityTextures().get("back").getFirst());
        } else {
            this.setTexture(super.getEntityTextures().get("right").getFirst());
        }
    }

    public void updatePhase(Level level, Hrac hrac) {
        this.moveTimer++;
        if (this.moveTimer < 1) {
            return;
        }
        this.moveTimer = 0;

        double dx = hrac.getHracX() - super.getX();
        double dy = hrac.getHracY() - super.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        //System.out.println(distance);
        if (distance < 100) {
            this.state = State.CHASE;
        }

        if (this.state == State.PATROL) {
            double newX = super.getX() + (this.rychlost * this.patrolDirection);
            int mapX = (int)newX;
            int mapY = (int)super.getY();

            if (!level.kolizia(mapX / 64, mapY / 64)) {
                super.setX(newX);
            } else {
                this.patrolDirection *= -1;
                this.viewAngle += Math.toRadians(180);
                this.viewAngle += Tools.korekciaUhla(this.viewAngle);
            }
        } else if (this.state == State.CHASE) {
            double angleToPlayer = Math.atan2(dy, dx);
            double newX = super.getX() + this.rychlost * Math.cos(angleToPlayer);
            double newY = super.getY() + this.rychlost * Math.sin(angleToPlayer);

            int mapX = (int)(newX / 64);
            int mapY = (int)(newY / 64);

            if (!level.kolizia(mapY, mapX)) {
                super.setX(newX);
                super.setY(newY);
                this.viewAngle = hrac.getHracAngle();
            }
        }

    }
}

