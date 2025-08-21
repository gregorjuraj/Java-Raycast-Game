package sprites.sprites.staticSprites;

import entity.Hrac;
import levely.levelData.Level;
import sprites.SprityEnum;
import sprites.spriteData.StaticSprite;

/**
 * Trieda Bullet rozširuje triedu StaticSprite a reprezentuje strelu.
 * Spravuje pohyb strely podľa uhla hráča a sleduje jej dolet.
 */
public class Bullet extends StaticSprite {
    private double angle;
    /** Počet aktualizácií doletu strely, používa sa na určenie, kedy strela zmizne. */
    private int dolet;

    /**
     * Konštruktor triedy Bullet.
     * Inicializuje strelu na pozícii blízko hráča s uhlom podľa hráčovho pohľadu.
     *
     * @param x X-ová súradnica pozície strely
     * @param y Y-ová súradnica pozície strely
     * @param hrac objekt hráča, použitý na získanie uhla pohľadu
     */
    public Bullet(double x, double y, Hrac hrac) {
        super(x + 1, y + 1, SprityEnum.BULLET); // posun aby nebol priamo na poziciach hraca
        this.angle = hrac.getHracAngle();
        this.dolet = 0;
    }

    @Override
    public void update(Level level, Hrac hrac) {
        super.update(level, hrac);
        super.setX(super.getX() + 50 * Math.cos(this.angle));
        super.setY(super.getY() - 50 * Math.sin(this.angle));
        if (this.getDolet()) {
            super.beRemoved();
        }
    }

    /**
     * Vracia uhol strely.
     *
     * @return uhol strely (v radiánoch)
     */
    public double getBulletAngle() {
        return this.angle;
    }

    /**
     * Kontroluje a aktualizuje dolet strely.
     * Zvyšuje počet aktualizácií doletu a vracia, či strela dosiahla maximálny dolet.
     *
     * @return true, ak strela dosiahla maximálny dolet (5 aktualizácií), inak false
     */
    private boolean getDolet() {
        if (this.dolet != 5) {
            this.dolet++;
            return false;
        }
        this.dolet = 0;
        return true;
    }
}