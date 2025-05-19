package engine;

import engine.tools.Tools;

/**
 * Trieda reprezentujúca dvere v hernom engine, spravuje ich pozíciu, animáciu a stav.
 */
public class Door {
    private int x;
    private int y;
    private int step;
    private int state; //Stav dverí (0 - otváranie, 1 - zatváranie, 2 - otvorené)
    private int waitStep; //pocitadlo cakania
    private boolean isAnimacia;
    private boolean open;

    /**
     * Konštruktor pre vytvorenie dverí na zadanej pozícii.
     *
     * @param x X-ová súradnica dverí
     * @param y Y-ová súradnica dverí
     */
    public Door(int x, int y) {
        this.x = x;
        this.y = y;
        this.step = 64;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * Spravuje animáciu dverí (otváranie, zatváranie, čakanie).
     */
    public void animacia() {
        if (this.state == 0) {
            this.step--;
        } else if (this.state == 1) {
            this.step++;
        } else {
            if (this.waitStep == 1) {
                this.open = true;
            } else if (this.waitStep == 239) {
                this.open = false;
            }
            this.waitStep++;
        }

        if (this.step == 0) {
            this.state = 2;
        } else if (this.step == 64) {
            this.state = 0;
            this.isAnimacia = false;
        }

        if (this.waitStep == Tools.sekundyNaTick(4)) {
            this.state = 1;
            this.waitStep = 0;
        }
    }

    /**
     * Spustí animáciu otvárania dverí.
     */
    public void startAnimacia() {
        this.isAnimacia = true;
        this.state = 0;  // 0 - otvaranie, 1 - zatvaranie, 2 - otvorene
        this.waitStep = 0;
    }

    public boolean isAnimacia() {
        return this.isAnimacia;
    }

    public int getStep() {
        return this.step;
    }

    public boolean isOpen() {
        return this.open;
    }
}