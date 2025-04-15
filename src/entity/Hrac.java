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

public class Hrac {
    private Ovladanie ovladanie;

    private int hracX;
    private int hracY;
    private int rychlost;
    private int pitch;
    private double hracAngle;
    private Tools tools;
    private HernyPanel hp;

    private JLabel xCoords;
    private JLabel yCoords;
    private JLabel angleText;

    private Robot robot;
    private int posledneX;
    private int posledneY;
    private boolean mouseMovedThisFrame;

    private boolean hracUse;


    public Hrac(Ovladanie ovl, HernyPanel hp) {
        this.tools = new Tools();
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

    public void setDefaultValues() {
        this.hracX = 100;
        this.hracY = 100;
        this.pitch = 0;
        this.rychlost = 6;
        this.hracAngle = Math.toRadians(350);
        this.hracUse = false;
    }

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
    }

    public void update(Level levelOne) {
        int checkKoliziaXUp = this.hracX + (int)(20 * Math.cos(this.hracAngle));
        int checkKoliziaYUp = this.hracY - (int)(20 * Math.sin(this.hracAngle));
        if ((this.ovladanie.isUpPress() && !levelOne.kolizia(checkKoliziaYUp / 64, checkKoliziaXUp / 64))) {
            this.hracX += (int)(this.rychlost * Math.cos(this.hracAngle));
            this.hracY -= (int)(this.rychlost * Math.sin(this.hracAngle));
        }

        int checkKoliziaXDown = this.hracX - (int)(20 * Math.cos(this.hracAngle));
        int checkKoliziaYDown = this.hracY + (int)(20 * Math.sin(this.hracAngle));
        if (this.ovladanie.isDownPress() && !levelOne.kolizia(checkKoliziaYDown / 64, checkKoliziaXDown / 64)) {
            this.hracX -= (int)(this.rychlost * Math.cos(this.hracAngle));
            this.hracY += (int)(this.rychlost * Math.sin(this.hracAngle));
        }

        if (this.ovladanie.isLeftPress()) {
            this.hracAngle -= PI / 90; // 180 = 1stupnov, 12 = 15stupnov
            this.hracAngle += this.tools.korekciaUhla(this.hracAngle);
        }

        if (this.ovladanie.isRightPress()) {
            this.hracAngle += PI / 90;
            this.hracAngle += this.tools.korekciaUhla(this.hracAngle);
        }

        if (this.ovladanie.isLookUpPress()) {
            this.pitch -= 10;
        }
        if (this.ovladanie.isLookDownPress()) {
            this.pitch += 10;
        }

        if (this.ovladanie.isSpacePress()) {
            if (this.ovladanie.isSpacePressedOnce()) {
                this.hracUse = true;
            }
        }

        if (this.ovladanie.isLockMouse()) {
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            int currentX = mousePos.x;
            int currentY = mousePos.y;

            if (!this.mouseMovedThisFrame) {

                if (currentX != this.posledneX) {
                    int deltaX = currentX - this.posledneX;
                    this.hracAngle += (deltaX * PI) / 180 / 30;
                    this.hracAngle += this.tools.korekciaUhla(this.hracAngle);
                    this.posledneX = currentX;
                    this.mouseMovedThisFrame = true;
                }
                if (currentY != this.posledneY) {
                    int deltaY = currentY - this.posledneY;
                    if (this.pitch + (deltaY / 2) > -400 && this.pitch + (deltaY / 2) < 400) {
                        this.pitch += deltaY / 2;
                    }
                    this.posledneY = currentY;
                    this.mouseMovedThisFrame = true;
                }
            }

            this.robot.mouseMove(this.hp.getSuradnicePanela("x") + 640, this.hp.getSuradnicePanela("y") + 320);
            this.posledneX = this.hp.getSuradnicePanela("x") + 640;
            this.posledneY = this.hp.getSuradnicePanela("y") + 320;
            this.mouseMovedThisFrame = false;
        }

        this.xCoords.setText(String.valueOf(this.hracX));
        this.yCoords.setText(String.valueOf(this.hracY));
        this.angleText.setText(String.valueOf(Math.toDegrees(this.hracAngle)));

    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(this.hracX, this.hracY, 10, 10);
        /*
        for (int i = 0; i < 320; i++) {
            if (i == 160) {
                g2.setColor(Color.red);
                g2.drawLine(this.hracX + 5, this.hracY + 5, this.raycast.get(i).getFinalDlzkaX(), this.raycast.get(i).getFinalDlzkaY());
            }
        }

         */


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

    public boolean isHracUse() {
        return this.hracUse;
    }

    public void stopHracUse() {
        this.hracUse = false;
    }
}
