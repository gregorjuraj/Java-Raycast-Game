package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Trieda Ovladanie implementuje rozhranie KeyListener na spracovanie vstupov z klávesnice.
 * Slúži na sledovanie stavu kláves pre pohyb, pohľad, akcie a zamknutie myši.
 */
public class Ovladanie implements KeyListener {

    private boolean upPress;
    private boolean downPress;
    private boolean leftPress;
    private boolean rightPress;
    private boolean lookUpPress;
    private boolean lookDownPress;

    private boolean lockMouse;
    private int mousePress;

    private boolean ePress;
    private boolean eHoldCheck;

    private boolean shootPress;
    private boolean shootHoldCheck;

    /**
     * Spracováva udalosti písania kláves. Používa sa na detekciu stlačenia medzerníka pre akciu 'E'.
     * @param e udalosť klávesy
     */
    @Override
    public void keyTyped(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_SPACE) {
            this.ePress = true;
        }
    }

    /**
     * Spracováva stlačenie kláves. Nastavuje príslušné príznaky pre pohyb, pohľad, akcie a zamknutie myši.
     * @param e udalosť klávesy
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            this.upPress = true;
        }
        if (code == KeyEvent.VK_S) {
            this.downPress = true;
        }
        if (code == KeyEvent.VK_A) {
            this.leftPress = true;
        }
        if (code == KeyEvent.VK_D) {
            this.rightPress = true;
        }
        if (code == KeyEvent.VK_G) {
            this.lookUpPress = true;
        }
        if (code == KeyEvent.VK_H) {
            this.lookDownPress = true;
        }
        if (code == KeyEvent.VK_L) {
            //zamknutie mysy
            switch (this.mousePress) {
                case 0:
                    this.lockMouse = true;
                    this.mousePress++;
                    break;
                case 1:
                    this.lockMouse = false;
                    this.mousePress--;
                    break;
            }
        }

        if (code == KeyEvent.VK_E) {
            this.ePress = true;
        }

        if (code == KeyEvent.VK_SPACE) {
            this.shootPress = true;
        }
    }

    /**
     * Spracováva uvoľnenie kláves. Resetuje príznaky pre pohyb, pohľad a akcie.
     * @param e udalosť klávesy
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            this.upPress = false; // Koniec pohybu nahor
        }
        if (code == KeyEvent.VK_S) {
            this.downPress = false; // Koniec pohybu nadol
        }
        if (code == KeyEvent.VK_A) {
            this.leftPress = false; // Koniec pohybu doľava
        }
        if (code == KeyEvent.VK_D) {
            this.rightPress = false; // Koniec pohybu doprava
        }
        if (code == KeyEvent.VK_G) {
            this.lookUpPress = false; // Koniec pohľadu nahor
        }
        if (code == KeyEvent.VK_H) {
            this.lookDownPress = false; // Koniec pohľadu nadol
        }
        if (code == KeyEvent.VK_E) {
            this.ePress = false; // Koniec akcie 'E'
            this.eHoldCheck = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            this.shootPress = false; // Koniec streľby
            this.shootHoldCheck = false;
        }
    }

    /**
     * Kontroluje, či bola klávesa 'E' stlačená iba raz (zabraňuje opakovanému spusteniu pri držaní).
     * @return true, ak bola klávesa 'E' stlačená raz, inak false
     */
    public boolean isEPressedOnce() {
        if (this.ePress && !this.eHoldCheck) {
            this.eHoldCheck = true;
            return true;
        }
        return false;
    }

    /**
     * Kontroluje, či bola klávesa pre streľbu stlačená iba raz (zabraňuje opakovanému spusteniu pri držaní).
     * @return true, ak bola klávesa pre streľbu stlačená raz, inak false
     */
    public boolean isShootPressedOnce() {
        if (this.shootPress && !this.shootHoldCheck) {
            this.shootHoldCheck = true;
            return true;
        }
        return false;
    }

    public boolean isUpPress() {
        return this.upPress;
    }

    public boolean isDownPress() {
        return this.downPress;
    }

    public boolean isLeftPress() {
        return this.leftPress;
    }

    public boolean isRightPress() {
        return this.rightPress;
    }

    public boolean isLookUpPress() {
        return this.lookUpPress;
    }

    public boolean isLookDownPress() {
        return this.lookDownPress;
    }

    public boolean isLockMouse() {
        return this.lockMouse;
    }

    public boolean isEPress() {
        return this.ePress;
    }

    public boolean isShootPress() {
        return this.shootPress;
    }
}