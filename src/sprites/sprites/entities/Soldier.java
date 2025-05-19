package sprites.sprites.entities;

import engine.tools.Tools;
import entity.Hrac;
import levely.levelData.Level;
import sprites.SprityEnum;
import sprites.spriteData.EntitySprite;

import java.util.Random;

/**
 * Trieda Soldier rozširuje triedu EntitySprite a reprezentuje nepriateľskú entitu typu vojak.
 * Spravuje pohyb, animácie, útoky a správanie vojak.
 */
public class Soldier extends EntitySprite {
    private double viewAngle; //uhol pohladu vojaka
    private State state; //stav co robi
    private double rychlost;
    private int patrolDirection; //1 - dopredu, -1 - dozadu

    private int hp;
    private int deathAnimFrameCount; //pocet snimok pre animaciu smrti
    private boolean shooting;
    private int shootAnimFrameCount; //pocet snimok pre animaciu strielania

    /**
     * Konštruktor triedy Soldier.
     * Inicializuje vojaka na danej pozícii a nastavuje predvolené hodnoty.
     *
     * @param x X-ová súradnica pozície vojaka
     * @param y Y-ová súradnica pozície vojaka
     */
    public Soldier(double x, double y) {
        super(x, y, SprityEnum.SOLDIER);
        this.setDefaultValues();
    }

    /**
     * Nastavuje predvolené hodnoty pre atribúty vojaka, ako uhol pohľadu, stav, rýchlosť a zdravie.
     */
    private void setDefaultValues() {
        this.viewAngle = Math.toRadians(180);
        this.state = State.PATROL;
        this.rychlost = 4;
        this.patrolDirection = 1;
        this.hp = 3;
        this.deathAnimFrameCount = 0;
    }

    /**
     * Aktualizuje textúru vojaka na základe jeho stavu a uhla voči hráčovi.
     *
     * @param hrac objekt hráča, použitý na výpočet uhla
     */
    public void updateTexture(Hrac hrac) {
        double delta = this.viewAngle - hrac.getHracAngle();
        delta += Tools.korekciaUhla(delta);
        delta = Math.toDegrees(delta);

        if (this.state != State.DEAD) {
            if (delta > 315 || delta <= 45) {
                this.setTexture(super.getEntityTextures().get("front").getFirst());
                super.getEntityTextures().get("front").addLast(super.getEntityTextures().get("front").getFirst());
                super.getEntityTextures().get("front").removeFirst();
            } else if (delta > 45 && delta <= 135) {
                this.setTexture(super.getEntityTextures().get("left").getFirst());
                super.getEntityTextures().get("left").addLast(super.getEntityTextures().get("left").getFirst());
                super.getEntityTextures().get("left").removeFirst();
            } else if (delta > 135 && delta <= 225) {
                this.setTexture(super.getEntityTextures().get("back").getFirst());
                super.getEntityTextures().get("back").addLast(super.getEntityTextures().get("back").getFirst());
                super.getEntityTextures().get("back").removeFirst();
            } else {
                this.setTexture(super.getEntityTextures().get("right").getFirst());
                super.getEntityTextures().get("right").addLast(super.getEntityTextures().get("right").getFirst());
                super.getEntityTextures().get("right").removeFirst();
            }
        }

        if (this.state == State.DEAD && this.hp != -1) {
            this.setTexture(super.getEntityTextures().get("death").getFirst());
            super.getEntityTextures().get("death").addLast(super.getEntityTextures().get("death").getFirst());
            super.getEntityTextures().get("death").removeFirst();
            this.deathAnimFrameCount++;
            if (this.deathAnimFrameCount == super.getEntityTextures().get("death").size()) {
                this.hp = -1;
            }
        }

        if (this.state == State.SHOOT && this.shooting) {
            this.setTexture(super.getEntityTextures().get("shoot").getFirst());
            super.getEntityTextures().get("shoot").addLast(super.getEntityTextures().get("shoot").getFirst());
            super.getEntityTextures().get("shoot").removeFirst();
            this.shootAnimFrameCount++;
            if (this.shootAnimFrameCount == super.getEntityTextures().get("shoot").size()) {
                this.state = State.CHASE;
                this.shooting = false;
                this.shootAnimFrameCount = 0;
            }
        }
    }

    /**
     * Aktualizuje správanie vojaka na základe pozície hráča a kolízií v leveli.
     *
     * @param level objekt levelu, použitý na kontrolu kolízií
     * @param hrac objekt hráča, použitý na výpočet vzdialenosti a uhla
     */
    public void updatePhase(Level level, Hrac hrac) {
        if (this.hp == 0) {
            this.state = State.DEAD;
        }

        double dx = hrac.getHracX() - super.getX();
        double dy = hrac.getHracY() - super.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < 100 && this.state != State.DEAD) {
            this.state = State.CHASE;
        }

        if (this.state == State.PATROL) {
            double newX = super.getX() + (this.rychlost * this.patrolDirection);
            int mapX = (int)newX;
            int mapY = (int)super.getY();

            if (!level.kolizia(mapY / 64, mapX / 64)) {
                super.setX(newX); // ak nenastala kolizia tak sa posunie
            } else {
                this.patrolDirection *= -1; // Zmena smeru pri kolizii
                this.viewAngle += Math.toRadians(180);
                this.viewAngle += Tools.korekciaUhla(this.viewAngle);
            }
        } else if (this.state == State.CHASE) {
            double angleToPlayer = Math.atan2(dy, dx);
            double newX = super.getX() + this.rychlost * Math.cos(angleToPlayer);
            double newY = super.getY() + this.rychlost * Math.sin(angleToPlayer);

            int mapX = (int)(newX / 64);
            int mapY = (int)(newY / 64);

            Random random = new Random();
            int sancaNaShoot = random.nextInt(0, 100);
            if (sancaNaShoot <= 2) {
                this.shooting = true;
                this.state = State.SHOOT;
            }

            if (distance > 400) {
                this.state = State.PATROL;
            }

            if (!level.kolizia(mapY, mapX)) {
                super.setX(newX);
                super.setY(newY);
                this.viewAngle = hrac.getHracAngle(); //nahana hraca, musi sa pozerat na neho
            }
        }
    }

    /**
     * Znižuje zdravie vojaka o 1 pri zásahu.
     *
     * @return true, ak bol zásah úspešný
     */
    public boolean hit() {
        this.hp--;
        return true;
    }

    /**
     * Vracia aktuálny stav vojaka.
     *
     * @return stav vojaka (PATROL, CHASE, SHOOT, DEAD)
     */
    public State getState() {
        return this.state;
    }
}