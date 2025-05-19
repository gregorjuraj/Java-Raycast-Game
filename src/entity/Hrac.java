package entity;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import engine.tools.Tools;
import levely.levelData.Level;
import main.HernyPanel;
import main.Ovladanie;

import javax.swing.JLabel;

import static engine.tools.Tools.PI;

/**
 * Trieda reprezentujúca hráča v hernom prostredí.
 * Spravuje pohyb, orientáciu, interakcie a zobrazenie hráča.
 */
public class Hrac {
    private Ovladanie ovladanie;
    private int hracX;
    private int hracY;
    private int rychlost;
    private int pitch; //vertikalny uhol pohladu hraca
    private double hracAngle; //uhol pohladu hraca (radiany)
    private HernyPanel hp; //herny panel
    private int scoreValue;

    private JLabel xCoords; //textove pole pre X suradnicu
    private JLabel yCoords; //textove pole pre Y suradnicu
    private JLabel angleText; //textove pole pre uhol pohladu hraca (hracAngle)
    private JLabel score;

    private Robot robot; //ovladanie mysi (test)
    private int posledneX;
    private int posledneY;
    private boolean mouseMovedThisFrame;
    private boolean hracUse;

    /**
     * Konštruktor hráča.
     * Inicializuje ovládanie, herný panel, nástroje a predvolené hodnoty.
     * @param ovl Ovládanie hráča
     * @param hp Herný panel
     */
    public Hrac(Ovladanie ovl, HernyPanel hp) {
        this.ovladanie = ovl;
        this.hp = hp;
        this.setDefaultValues();
        this.suradniceText();

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nastaví predvolené hodnoty pre pozíciu, rýchlosť, uhol a ďalšie atribúty hráča.
     */
    public void setDefaultValues() {
        this.hracX = 100;
        this.hracY = 100;
        this.pitch = 0;
        this.rychlost = 6;
        this.hracAngle = Math.toRadians(350);
        this.hracUse = false;
        this.scoreValue = 0;
    }

    /**
     * Inicializuje textové polia pre zobrazenie súradníc a uhla hráča na hernom paneli.
     */
    public void suradniceText() {
        this.xCoords = new JLabel("X : ");
        this.xCoords.setFont(new Font("Verdana", Font.BOLD, 20));
        this.xCoords.setBounds(10, 10, 50, 30);
        this.xCoords.setForeground(Color.white);
        this.hp.add(this.xCoords);

        this.yCoords = new JLabel("Y : ");
        this.yCoords.setFont(new Font("Verdana", Font.BOLD, 20));
        this.yCoords.setBounds(10, 30, 50, 30);
        this.yCoords.setForeground(Color.white);
        this.hp.add(this.yCoords);

        this.angleText = new JLabel("Angle : ");
        this.angleText.setFont(new Font("Verdana", Font.BOLD, 20));
        this.angleText.setBounds(10, 50, 100, 30);
        this.angleText.setForeground(Color.white);
        this.hp.add(this.angleText);

        this.score = new JLabel("0");
        this.score.setFont(new Font("Verdana", Font.BOLD, 30));
        this.score.setBounds(350, 655, 100, 40);
        this.score.setForeground(Color.white);
        this.hp.add(this.score);
    }

    /**
     * Aktualizuje stav hráča na základe vstupov a kolízií s levelom.
     * Spracováva pohyb, otáčanie, streľbu a interakcie.
     * @param level Aktuálny herný level
     */
    public void update(Level level) {
        // Pohyb dopredu s kontrolou kolizie
        int checkKoliziaXUp = this.hracX + (int)(20 * Math.cos(this.hracAngle));
        int checkKoliziaYUp = this.hracY - (int)(20 * Math.sin(this.hracAngle));
        if ((this.ovladanie.isUpPress() && !level.kolizia(checkKoliziaYUp / 64, checkKoliziaXUp / 64))) {
            this.hracX += (int)(this.rychlost * Math.cos(this.hracAngle));
            this.hracY -= (int)(this.rychlost * Math.sin(this.hracAngle));
        }

        // Pohyb dozadu s kontrolou kolizie
        int checkKoliziaXDown = this.hracX - (int)(20 * Math.cos(this.hracAngle));
        int checkKoliziaYDown = this.hracY + (int)(20 * Math.sin(this.hracAngle));
        if (this.ovladanie.isDownPress() && !level.kolizia(checkKoliziaYDown / 64, checkKoliziaXDown / 64)) {
            this.hracX -= (int)(this.rychlost * Math.cos(this.hracAngle));
            this.hracY += (int)(this.rychlost * Math.sin(this.hracAngle));
        }

        // Otacanie dolava
        if (this.ovladanie.isLeftPress()) {
            this.hracAngle -= PI / 90; // 2 stupne
            this.hracAngle += Tools.korekciaUhla(this.hracAngle);
        }

        // Otacanie doprava
        if (this.ovladanie.isRightPress()) {
            this.hracAngle += PI / 90;
            this.hracAngle += Tools.korekciaUhla(this.hracAngle);
        }

        // Pohlad hore
        if (this.ovladanie.isLookUpPress()) {
            this.pitch -= 10;
        }
        // Pohlad dole
        if (this.ovladanie.isLookDownPress()) {
            this.pitch += 10;
        }

        // Strelba
        if (this.ovladanie.isShootPress()) {
            if (this.ovladanie.isShootPressedOnce()) {
                new Shot(this, level);
            }
        }

        // Interakcia
        if (this.ovladanie.isEPress()) {
            if (this.ovladanie.isEPressedOnce()) {
                this.hracUse = true;
            }
        }

        // Ovladanie mysou (zamknutie a pohyb) [test]
        if (this.ovladanie.isLockMouse()) {
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            int currentX = mousePos.x;
            int currentY = mousePos.y;

            if (!this.mouseMovedThisFrame) {
                // Horizontalne
                if (currentX != this.posledneX) {
                    int deltaX = currentX - this.posledneX;
                    this.hracAngle += (deltaX * PI) / 180 / 30;
                    this.hracAngle += Tools.korekciaUhla(this.hracAngle);
                    this.posledneX = currentX;
                    this.mouseMovedThisFrame = true;
                }
                // Vertikalne
                if (currentY != this.posledneY) {
                    int deltaY = currentY - this.posledneY;
                    if (this.pitch + (deltaY / 2) > -400 && this.pitch + (deltaY / 2) < 400) {
                        this.pitch += deltaY / 2;
                    }
                    this.posledneY = currentY;
                    this.mouseMovedThisFrame = true;
                }
            }

            // centrovanie mysky na stred okna
            this.robot.mouseMove(this.hp.getSuradnicePanela("x") + 640, this.hp.getSuradnicePanela("y") + 320);
            this.posledneX = this.hp.getSuradnicePanela("x") + 640;
            this.posledneY = this.hp.getSuradnicePanela("y") + 320;
            this.mouseMovedThisFrame = false;
        }

        this.xCoords.setText(String.valueOf(this.hracX));
        this.yCoords.setText(String.valueOf(this.hracY));
        this.angleText.setText(String.valueOf(Math.toDegrees(this.hracAngle)));
        this.score.setText(String.valueOf(this.scoreValue));
    }

    /**
     * Vykreslí hráča na herný panel ako biely obdĺžnik. (nevyuzite, iba na debug)
     * @param g2 Grafický kontext
     */
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(this.hracX, this.hracY, 10, 10);
    }


    public double getHracAngle() {
        return this.hracAngle;
    }


    public int getHracY() {
        return this.hracY;
    }


    public int getHracX() {
        return this.hracX;
    }


    public int getPitch() {
        return this.pitch;
    }

    /**
     * Vracia, či hráč vykonáva interakciu.
     * @return Stav interakcie
     */
    public boolean isHracUse() {
        return this.hracUse;
    }

    /**
     * Zastaví interakciu hráča.
     */
    public void stopHracUse() {
        this.hracUse = false;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue += scoreValue;
    }
}