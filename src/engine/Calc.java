package engine;

import levely.levelData.Level;
import textures.TexturyEnum;
import entity.Hrac;

import static engine.tools.Tools.PI;

public class Calc {

    private double vertikalneDlzka;
    private int vertikalneDlzkaX;
    private int vertikalneDlzkaY;
    private boolean vertikalneDvereHit;

    private double horizontalneDlzka;
    private int horizontalneDlzkaX;
    private int horizontalneDlzkaY;
    private boolean horizontalneDvereHit;

    private double finalDlzka;
    private int finalDlzkaX;
    private int finalDlzkaY;
    private double finalUhol;
    private boolean horizontalHit;
    private TexturyEnum finalTextura;

    private boolean dvereHit;
    private int drawStep;
    private Door hitDoor;

    public void finalCiara(Hrac hrac, Level levelOne, double angle) {
        this.finalUhol = angle;
        this.dvereHit = false;
        this.hitDoor = null;
        this.horizontalneCiary(hrac, levelOne, angle);
        this.vertikalneCiary(hrac, levelOne, angle);

        if (this.horizontalneDlzka < this.vertikalneDlzka) {
            this.finalDlzkaX = this.horizontalneDlzkaX;
            this.finalDlzkaY = this.horizontalneDlzkaY;
            this.finalDlzka = this.horizontalneDlzka;
            this.horizontalHit = true;
            this.dvereHit = this.horizontalneDvereHit;
            this.finalTextura = levelOne.getTexturaZMapy(this.finalDlzkaY / levelOne.getCellVelkost(), this.finalDlzkaX / levelOne.getCellVelkost(), 0);
        } else {
            this.finalDlzkaX = this.vertikalneDlzkaX;
            this.finalDlzkaY = this.vertikalneDlzkaY;
            this.finalDlzka = this.vertikalneDlzka;
            this.horizontalHit = false;
            this.dvereHit = this.vertikalneDvereHit;
            this.finalTextura = levelOne.getTexturaZMapy(this.finalDlzkaY / levelOne.getCellVelkost(), this.finalDlzkaX / levelOne.getCellVelkost(), 1);
        }
    }

    private void vertikalneCiary(Hrac hrac, Level levelOne, double angle) {
        double aY = 1;
        double aX = 1;
        double krokX = 1;
        double krokY = 1;
        double dlzka = 0;
        this.vertikalneDvereHit = false;
        int aYCoords = 1;
        int aXCoords = 1;

        if ((angle >= 0 && angle < PI / 2) || (angle >= (3 * PI) / 2 && angle <= 2 * PI)) { //smer vpravo
            aX = Math.floor(hrac.getHracX() / levelOne.getCellVelkost()) * levelOne.getCellVelkost() + levelOne.getCellVelkost(); //podla najblizsiej vertikalnej steny
        } else {
            aX = Math.floor(hrac.getHracX() / levelOne.getCellVelkost()) * levelOne.getCellVelkost() - 0.000001;
        }
        aY = hrac.getHracY() + (hrac.getHracX() - aX) * Math.tan(angle);
        aYCoords = (int)(aY / levelOne.getCellVelkost());
        aXCoords = (int)(aX / levelOne.getCellVelkost());

        if (aYCoords <= levelOne.getPocRiadkov() && aXCoords <= levelOne.getPocStlpcov() && aYCoords > -1 && aXCoords > -1) {
            int aYCoordsTemp = 0;
            int aXCoordsTemp = 0;
            double aXTemp = 0;
            double aYTemp = 0;
            boolean test = false;
            if (levelOne.koliziaDvere(aYCoords, aXCoords)) {
                test = true;
                aYCoordsTemp = aYCoords;
                aXCoordsTemp = aXCoords;
                aXTemp = aX;
                aYTemp = aY;
            }
            if (levelOne.kolizia(aYCoords, aXCoords) && !levelOne.koliziaDvere(aYCoords, aXCoords)) {
                this.vertikalneDlzkaX = (int)aX;
                this.vertikalneDlzkaY = (int)aY;
                dlzka = Math.sqrt(Math.pow(this.vertikalneDlzkaX - hrac.getHracX(), 2) + Math.pow(this.vertikalneDlzkaY - hrac.getHracY(), 2));
                this.vertikalneDlzka = dlzka;
            } else {
                if ((angle >= 0 && angle < PI / 2) || (angle >= (3 * PI) / 2 && angle <= 2 * PI)) {
                    krokX = levelOne.getCellVelkost();
                    krokY = -levelOne.getCellVelkost() * Math.tan(angle);
                } else {
                    krokX = -levelOne.getCellVelkost();
                    krokY = levelOne.getCellVelkost() * Math.tan(angle);
                }
                while (true) {
                    aX += krokX;
                    aY += krokY;
                    aYCoords = (int)(aY / levelOne.getCellVelkost());
                    aXCoords = (int)(aX / levelOne.getCellVelkost());

                    if (aYCoords <= levelOne.getPocRiadkov() && aXCoords <= levelOne.getPocStlpcov() && aYCoords > -1 && aXCoords > -1) {
                        if (levelOne.kolizia(aYCoords, aXCoords)) {
                            this.vertikalneDlzkaX = (int)aX;
                            this.vertikalneDlzkaY = (int)aY;

                            if (test) {
                                aYCoords = aYCoordsTemp;
                                aXCoords = aXCoordsTemp;
                                this.vertikalneDlzkaX = (int)aXTemp;
                                this.vertikalneDlzkaY = (int)aYTemp;
                                aX = aXTemp;
                                aY = aYTemp;
                            }

                            if (levelOne.koliziaDvere(aYCoords, aXCoords)) {
                                this.vertikalneDvereHit = true;
                                for (Door door : levelOne.getDoors()) {
                                    if (door.getX() == aXCoords && door.getY() == aYCoords) {
                                        this.setHitDoor(door);
                                        break;
                                    }
                                }
                                if ((angle >= 0 && angle < PI / 2) || (angle >= (3 * PI) / 2 && angle <= 2 * PI)) {
                                    this.vertikalneDlzkaX += 32;
                                    this.vertikalneDlzkaY -= (int)(32 * Math.tan(angle));
                                    if (this.vertikalneDlzkaY <= (levelOne.getCellVelkost() * aYCoords) + 64 && this.vertikalneDlzkaY >= (levelOne.getCellVelkost() * aYCoords) + this.hitDoor.getStep()) {
                                        this.vertikalneDvereHit = false;
                                        test = false;
                                        continue;
                                    }
                                } else {
                                    this.vertikalneDlzkaX -= 32;
                                    this.vertikalneDlzkaY += (int)(32 * Math.tan(angle));
                                    if (this.vertikalneDlzkaY <= (levelOne.getCellVelkost() * aYCoords) + 64 && this.vertikalneDlzkaY >= (levelOne.getCellVelkost() * aYCoords) + this.hitDoor.getStep()) {
                                        this.vertikalneDvereHit = false;
                                        test = false;
                                        continue;
                                    }
                                }
                                this.drawStep = this.hitDoor.getStep();
                            }

                            dlzka = Math.sqrt(Math.pow(this.vertikalneDlzkaX - hrac.getHracX(), 2) + Math.pow(this.vertikalneDlzkaY - hrac.getHracY(), 2));
                            this.vertikalneDlzka = dlzka;
                            break;
                        }
                    } else {
                        this.vertikalneDlzka = Double.MAX_VALUE;
                        break;
                    }
                }
            }
        } else {
            dlzka = Math.sqrt(Math.pow(aX - hrac.getHracX(), 2) + Math.pow(aY - hrac.getHracY(), 2));
            this.vertikalneDlzkaX = (int)aX;
            this.vertikalneDlzkaY = (int)aY;
            this.vertikalneDlzka = dlzka;
            //teoreticky nemozne
        }
    }

    private void horizontalneCiary(Hrac hrac, Level levelOne, double angle) {
        double aY = 1;
        double aX = 1;
        double krokX = 1;
        double krokY = 1;
        double dlzka = 0;
        int aYCoords = 1;
        int aXCoords = 1;
        this.horizontalneDvereHit = false;

        if (angle > 0 && angle <= PI) { //ak smeruje luc smerom hore
            aY = (Math.floor(hrac.getHracY() / levelOne.getCellVelkost()) * levelOne.getCellVelkost()) - 0.000001;
        } else {
            aY = (Math.floor(hrac.getHracY() / levelOne.getCellVelkost()) * levelOne.getCellVelkost()) + levelOne.getCellVelkost(); //podla najblizsiej horizontalnej steny
        }
        aX = hrac.getHracX() + (hrac.getHracY() - aY) / Math.tan(angle); //aX potom vieme najst pomocou aY a pozicii kamery.
        aYCoords = (int)(aY / levelOne.getCellVelkost()); // tu si len zistime v akom stvorci na mriezke sa kamera nachadza (suradnice)
        aXCoords = (int)(aX / levelOne.getCellVelkost());

        if (aYCoords <= levelOne.getPocRiadkov() && aXCoords <= levelOne.getPocStlpcov() && aYCoords > -1 && aXCoords > -1) { //ci sa nachadza lúč v mriežke
            int aYCoordsTemp = 0;
            int aXCoordsTemp = 0;
            double aXTemp = 0;
            double aYTemp = 0;
            boolean test = false;
            if (levelOne.koliziaDvere(aYCoords, aXCoords)) {
                test = true;
                aYCoordsTemp = aYCoords;
                aXCoordsTemp = aXCoords;
                aXTemp = aX;
                aYTemp = aY;
            }
            if (levelOne.kolizia(aYCoords, aXCoords) && !levelOne.koliziaDvere(aYCoords, aXCoords)) { //pokial prebehne kolizia uz hned od hraca tak sa vypočíťa finálna dĺžka
                this.horizontalneDlzkaX = (int)aX;
                this.horizontalneDlzkaY = (int)aY;
                dlzka = Math.sqrt(Math.pow(this.horizontalneDlzkaX - hrac.getHracX(), 2) + Math.pow(this.horizontalneDlzkaY - hrac.getHracY(), 2));
                this.horizontalneDlzka = dlzka;
            } else { //ak neprebehla kolizia tak robíme ten istý výpočet ale už vieme túto hodnotu násobiť lebo vždy bude hodnota taká istá
                if (angle > 0 && angle <= PI) { // ak luč smeruje hore
                    krokY = -levelOne.getCellVelkost();
                    krokX = levelOne.getCellVelkost() / Math.tan(angle);
                } else { // ak luč smeruje dole
                    krokY = levelOne.getCellVelkost();
                    krokX = -levelOne.getCellVelkost() / Math.tan(angle);
                }
                while (true) {
                    aX += krokX; //pripočítavanie kroku k pôvodnemu aX dokým nenastane kolízia
                    aY += krokY; //pripočítavanie kroku k pôvodnemu aY dokým nenastane kolízia
                    aYCoords = (int)(aY / levelOne.getCellVelkost());
                    aXCoords = (int)(aX / levelOne.getCellVelkost());

                    if (aYCoords <= levelOne.getPocRiadkov() && aXCoords <= levelOne.getPocStlpcov() && aYCoords > -1 && aXCoords > -1) {
                        if (levelOne.kolizia(aYCoords, aXCoords)) {
                            this.horizontalneDlzkaX = (int)aX;
                            this.horizontalneDlzkaY = (int)aY;

                            if (test) {
                                aYCoords = aYCoordsTemp;
                                aXCoords = aXCoordsTemp;
                                this.horizontalneDlzkaX = (int)aXTemp;
                                this.horizontalneDlzkaY = (int)aYTemp;
                                aX = aXTemp;
                                aY = aYTemp;
                            }

                            if (levelOne.koliziaDvere(aYCoords, aXCoords)) {
                                this.horizontalneDvereHit = true;
                                for (Door door : levelOne.getDoors()) {
                                    if (door.getX() == aXCoords && door.getY() == aYCoords) {
                                        this.setHitDoor(door);
                                        break;
                                    }
                                }
                                if (angle > 0 && angle <= PI) {
                                    this.horizontalneDlzkaY -= 32 ;
                                    this.horizontalneDlzkaX += (int)(32 / Math.tan(angle));
                                    if (this.horizontalneDlzkaX <= (levelOne.getCellVelkost() * aXCoords) + 64 && this.horizontalneDlzkaX >= (levelOne.getCellVelkost() * aXCoords) + this.hitDoor.getStep()) {
                                        this.horizontalneDvereHit = false;
                                        test = false;
                                        continue;
                                    }
                                } else {
                                    this.horizontalneDlzkaY += 32 ;
                                    this.horizontalneDlzkaX -= (int)(32 / Math.tan(angle));
                                    if (this.horizontalneDlzkaX <= (levelOne.getCellVelkost() * aXCoords) + 64 && this.horizontalneDlzkaX >= (levelOne.getCellVelkost() * aXCoords) + this.hitDoor.getStep()) {
                                        this.horizontalneDvereHit = false;
                                        test = false;
                                        continue;
                                    }
                                }
                                this.drawStep = this.hitDoor.getStep();
                            }

                            dlzka = Math.sqrt(Math.pow(this.horizontalneDlzkaX - hrac.getHracX(), 2) + Math.pow(this.horizontalneDlzkaY - hrac.getHracY(), 2));
                            this.horizontalneDlzka = dlzka;
                            break;
                        }
                    } else {
                        this.horizontalneDlzka = Double.MAX_VALUE;
                        break;
                    }
                }
            }
        } else {
            dlzka = Math.sqrt(Math.pow(aX - hrac.getHracX(), 2) + Math.pow(aY - hrac.getHracY(), 2));
            this.horizontalneDlzka = dlzka;
            this.horizontalneDlzkaX = (int)aX;
            this.horizontalneDlzkaY = (int)aY;
            //teoreticky nemozne
        }
    }

    public double getFinalDlzka() {
        return this.finalDlzka;
    }

    public int getFinalDlzkaX() {
        return this.finalDlzkaX;
    }

    public int getFinalDlzkaY() {
        return this.finalDlzkaY;
    }

    public double getFinalUhol() {
        return this.finalUhol;
    }

    public boolean isHorizontalHit() {
        return this.horizontalHit;
    }

    public TexturyEnum getTextura() {
        return this.finalTextura;
    }

    private void setHitDoor(Door door) {
        this.hitDoor = door;
    }

    public boolean isDvereHit() {
        return this.dvereHit;
    }


    public int getDrawStep() {
        return this.drawStep;
    }
}

