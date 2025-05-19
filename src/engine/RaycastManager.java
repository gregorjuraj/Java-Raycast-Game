package engine;

import engine.tools.Tools;
import entity.Hrac;
import levely.levelData.Level;
import levely.levelData.WallData;

import static engine.tools.Tools.STUPNENARADIANY;

/**
 * Trieda pre správu raycastingu, ktorá vypočítava lúče a interakcia s dverami v hernom svete.
 */
public class RaycastManager {
    private final Calc[] raycast;
    private final double[] wallDistances;
    private Door targetDoor;

    /**
     * Konštruktor pre inicializáciu raycastingu s 320 lúčmi.
     */
    public RaycastManager() {
        this.raycast = new Calc[320];
        this.wallDistances = new double[320];
        for (int i = 0; i < 320; i++) {
            this.raycast[i] = new Calc();
        }
    }

    /**
     * Aktualizuje raycasting pre hráča a level, vrátane interakcie s dverami.
     *
     * @param hrac Hráč, z ktorého pohľadu sa raycasting vykonáva
     * @param level Level, v ktorej sa raycasting uskutočňuje
     * @param kreslenie Objekt pre kreslenie, ktorý sa aktualizuje pri animácii dverí
     */
    public void update(Hrac hrac, Level level, Kreslenie kreslenie) {
        //vytvaranie lucov +- 30 stupnov od hlavneho luca
        double tempUhol = hrac.getHracAngle() - 30 * STUPNENARADIANY;
        for (int i = 0; i < 320; i++) {
            tempUhol += Tools.korekciaUhla(tempUhol);
            this.raycast[i].finalCiara(hrac, level, tempUhol);
            tempUhol += 0.1875 * STUPNENARADIANY;

            double dlzka = this.raycast[i].getFinalDlzka() * Math.cos(hrac.getHracAngle() - this.raycast[i].getFinalUhol()); //fish eye fix
            this.wallDistances[i] = dlzka;
        }

        this.openDoor(hrac, level, kreslenie);
    }

    /**
     * Spracováva interakciu hráča s dvermi a ich animáciu.
     *
     * @param hrac Hráč, ktorý interaguje s dverami
     * @param level Úroveň, v ktorej sa dvere nachádzajú
     * @param kreslenie Objekt pre kreslenie, ktorý sa aktualizuje pri animácii dverí
     */
    private void openDoor(Hrac hrac, Level level, Kreslenie kreslenie) {
        // Interakcia s dvermi
        if (hrac.isHracUse()) {
            hrac.stopHracUse();
            int centerRay = 160;
            Calc centerCalc = this.raycast[centerRay];
            if (centerCalc.isDvereHit()) {
                int targetDoorX = centerCalc.getFinalDlzkaX() / 64;
                int targetDoorY = centerCalc.getFinalDlzkaY() / 64;
                for (Door door : level.getDoors()) {
                    if (targetDoorX == door.getX() && targetDoorY == door.getY()) {
                        int hracX = hrac.getHracX() / 64;
                        int hracY = hrac.getHracY() / 64;
                        // ak sa nachadza hrac pred dvermi
                        if ( (targetDoorX == hracX && targetDoorY == hracY - 1) || (targetDoorX == hracX && targetDoorY == hracY + 1) ||
                                (targetDoorX == hracX + 1 && targetDoorY == hracY) || (targetDoorX == hracX - 1 && targetDoorY == hracY) ) {
                            this.targetDoor = door;
                            this.targetDoor.startAnimacia();
                        }
                    }
                }
            }
        }

        for (Door door : level.getDoors()) {
            if (door.isAnimacia()) {
                door.animacia();
                kreslenie.setOffset(door.getStep()); //pridava offset do kreslenia (posuvanie textury)
                this.doorKolizia(door.isOpen(), level, door);
            }
        }
    }

    /**
     * Spravuje kolíziu dverí na základe ich stavu (otvorené/zatvorené).
     *
     * @param open Indikátor, či sú dvere otvorené
     * @param level Level, v ktorej sa dvere nachádzajú
     * @param door Dvere, ktorých kolízia sa spravuje
     */
    private void doorKolizia(boolean open, Level level, Door door) {
        if (open) {
            for (WallData[] wallData : level.getMapa()) {
                for (WallData wallDat : wallData) {
                    if (wallDat.getX() == door.getX() && wallDat.getY() == door.getY()) {
                        wallDat.setHorizontalnaStenaTextura(0);
                        wallDat.setVertikalnaStenaTextura(0);
                    }
                }
            }
        } else {
            for (WallData[] wallData : level.getMapa()) {
                for (WallData wallDat : wallData) {
                    if (wallDat.getX() == door.getX() && wallDat.getY() == door.getY()) {
                        wallDat.setHorizontalnaStenaTextura(9);
                        wallDat.setVertikalnaStenaTextura(9);
                    }
                }
            }
        }
    }


    public Calc[] getRaycast() {
        return this.raycast;
    }

    public double[] getWallDistances() {
        return this.wallDistances;
    }
}