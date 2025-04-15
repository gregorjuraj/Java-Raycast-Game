package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;


public class Ovladanie implements KeyListener {

    private boolean upPress;
    private boolean downPress;
    private boolean leftPress;
    private boolean rightPress;
    private boolean lookUpPress;
    private boolean lookDownPress;

    private boolean lockMouse;
    private int mousePress;

    private boolean spacePress;
    private boolean spaceHoldCheck;
    @Override
    public void keyTyped(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_SPACE) {
            this.spacePress = true;
        }
    }

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

        if (code == KeyEvent.VK_SPACE) {
            this.spacePress = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            this.upPress = false;
        }
        if (code == KeyEvent.VK_S) {
            this.downPress = false;
        }
        if (code == KeyEvent.VK_A) {
            this.leftPress = false;
        }
        if (code == KeyEvent.VK_D) {
            this.rightPress = false;
        }
        if (code == KeyEvent.VK_G) {
            this.lookUpPress = false;
        }
        if (code == KeyEvent.VK_H) {
            this.lookDownPress = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            this.spacePress = false;
            this.spaceHoldCheck = false;
        }
    }

    public boolean isSpacePressedOnce() {
        if (this.spacePress && !this.spaceHoldCheck) {
            this.spaceHoldCheck = true;
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

    public boolean isSpacePress() {
        return this.spacePress;
    }
}
