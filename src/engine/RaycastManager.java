package engine;

import engine.tools.Tools;
import entity.Hrac;
import levely.levelData.Level;
import levely.levelData.WallData;

import static engine.tools.Tools.STUPNENARADIANY;

public class RaycastManager {
    private final Calc[] raycast;
    private final double[] wallDistances;

    private Door targetDoor;

    public RaycastManager() {
        this.raycast = new Calc[320];
        this.wallDistances = new double[320];
        for (int i = 0; i < 320; i++) {
            this.raycast[i] = new Calc();
        }
    }

    public void update(Hrac hrac, Level levelOne, Kreslenie kreslenie) {
        double tempUhol = hrac.getHracAngle() - 30 * STUPNENARADIANY;
        for (int i = 0; i < 320; i++) {
            tempUhol += Tools.korekciaUhla(tempUhol);
            this.raycast[i].finalCiara(hrac, levelOne, tempUhol);
            tempUhol += 0.1875 * STUPNENARADIANY;

            double dlzka = this.raycast[i].getFinalDlzka() * Math.cos(hrac.getHracAngle() - this.raycast[i].getFinalUhol());
            this.wallDistances[i] = dlzka;
        }

        this.openDoor(hrac, levelOne, kreslenie);
    }

    private void openDoor(Hrac hrac, Level levelOne, Kreslenie kreslenie) {
        // interakcia s dvermi
        if (hrac.isHracUse()) {
            hrac.stopHracUse();
            int centerRay = 160;
            Calc centerCalc = this.raycast[centerRay];
            if (centerCalc.isDvereHit()) {
                int targetDoorX = centerCalc.getFinalDlzkaX() / 64;
                int targetDoorY = centerCalc.getFinalDlzkaY() / 64;
                for (Door door : levelOne.getDoors()) {
                    if (targetDoorX == door.getX() && targetDoorY == door.getY()) {
                        this.targetDoor = door;
                        this.targetDoor.startAnimacia();
                    }
                }
            }
        }

        for (Door door : levelOne.getDoors()) {
            if (door.isAnimacia()) {
                door.animacia();
                kreslenie.setOffset(door.getStep());
                this.doorKolizia(door.isOpen(), levelOne, door);
            }
        }
    }

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




