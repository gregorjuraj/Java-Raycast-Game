package levely.levelData;

import engine.Door;
import engine.TextureManager;
import sprites.spriteData.Sprite;
import textures.TexturyEnum;

import java.util.ArrayList;

public class Level {
    private WallData[][] mapa;
    private LoadLevel loadLevel;
    private ArrayList<Sprite> sprites;
    private ArrayList<Door> doors;

    private int velkostPolicka;
    private int pocRiadkov;
    private int pocStlpcov;

    public Level(String mapName) {
        this.loadLevel = new LoadLevel(mapName);

        this.velkostPolicka = 64;
        this.pocRiadkov = this.loadLevel.getPocetRiadok() - 1;
        this.pocStlpcov = this.loadLevel.getPocetSlpcov() - 1;
        this.mapa = this.loadLevel.getMapa();

        this.sprites = new ArrayList<>();
        this.doors = new ArrayList<>();
        for (WallData[] wallData : this.mapa) {
            for (WallData wallType : wallData) {
                if (wallType.getVertikalnaStenaTextura() == 9 || wallType.getHorizontalnaStenaTextura() == 9)  {
                    this.doors.add(new Door(wallType.getX(), wallType.getY()));
                }
            }

        }
    }
    public int getPocRiadkov() {
        return this.pocRiadkov;
    }

    public int getPocStlpcov() {
        return this.pocStlpcov;
    }

    public int getCellVelkost() {
        return this.velkostPolicka;
    }

    public boolean kolizia(int m, int n) {
        if (m >= 0 && m <= this.pocRiadkov && n >= 0 && n <= this.pocStlpcov) {
            return this.mapa[m][n].getHorizontalnaStenaTextura() > 0 || this.mapa[m][n].getVertikalnaStenaTextura() > 0;
        }
        return false;
    }
    public boolean koliziaDvere(int m, int n) {
        if (m >= 0 && m <= this.pocRiadkov && n >= 0 && n <= this.pocStlpcov) {
            return this.mapa[m][n].getHorizontalnaStenaTextura() == 9 || this.mapa[m][n].getVertikalnaStenaTextura() == 9;
        }
        return false;
    }

    public TexturyEnum getTexturaZMapy(int m, int n, int hOrV) {
        if (hOrV == 0) {
            if (m >= 0 && m <= this.pocRiadkov && n >= 0 && n <= this.pocStlpcov) {
                return TextureManager.getTexturaZEnumu(this.mapa[m][n].getHorizontalnaStenaTextura());
            } else {
                return TextureManager.getTexturaZEnumu(4);
            }
        } else {
            if (m >= 0 && m <= this.pocRiadkov && n >= 0 && n <= this.pocStlpcov) {
                return TextureManager.getTexturaZEnumu(this.mapa[m][n].getVertikalnaStenaTextura());
            } else {
                return TextureManager.getTexturaZEnumu(4);
            }
        }
    }

    public ArrayList<Sprite> getSprites() {
        return this.sprites;
    }

    public ArrayList<Door> getDoors() {
        return this.doors;
    }

    public WallData[][] getMapa() {
        return this.mapa;
    }
}
