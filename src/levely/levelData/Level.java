package levely.levelData;

import engine.Door;
import engine.TextureManager;
import sprites.spriteData.Sprite;
import sprites.TexturyEnum;

import java.util.ArrayList;

/**
 * Trieda reprezentujúca herný level.
 * Spravuje mapu, steny, dvere, sprity a kolízie v hernom prostredí.
 */
public class Level {
    private WallData[][] mapa; // 2D pole obsahujuce udaje mapy
    private LoadLevel loadLevel;
    private ArrayList<Sprite> sprites;
    private ArrayList<Door> doors;

    private final int velkostPolicka = 64; //in-game velkost jedneho tilu
    private int pocRiadkov;
    private int pocStlpcov;

    /**
     * Konštruktor levelu.
     * Načíta mapu zo zadaného názvu, inicializuje rozmery a vytvorí zoznamy spritov a dverí.
     * @param mapName Názov mapy na načítanie
     */
    public Level(String mapName) {
        this.loadLevel = new LoadLevel(mapName);

        this.pocRiadkov = this.loadLevel.getPocetRiadok() - 1;
        this.pocStlpcov = this.loadLevel.getPocetSlpcov() - 1;
        this.mapa = this.loadLevel.getMapa();

        this.sprites = new ArrayList<>();
        this.doors = new ArrayList<>();
        // ID 9 = dvere
        for (WallData[] wallData : this.mapa) {
            for (WallData wallType : wallData) {
                if (wallType.getVertikalnaStenaTextura() == 9 || wallType.getHorizontalnaStenaTextura() == 9)  {
                    this.doors.add(new Door(wallType.getX(), wallType.getY()));
                }
            }
        }
    }

    /**
     * Kontroluje, či je na zadaných súradniciach kolízia so stenou.
     * @param m Riadok v mape
     * @param n Stĺpec v mape
     * @return True, ak je kolízia, inak false
     */
    public boolean kolizia(int m, int n) {
        if (m >= 0 && m <= this.pocRiadkov && n >= 0 && n <= this.pocStlpcov) {
            return this.mapa[m][n].getHorizontalnaStenaTextura() > 0 || this.mapa[m][n].getVertikalnaStenaTextura() > 0;
        }
        return false;
    }

    /**
     * Kontroluje, či je na zadaných súradniciach kolízia s dverami.
     * @param m Riadok v mape
     * @param n Stĺpec v mape
     * @return True, ak sú na súradniciach dvere, inak false
     */
    public boolean koliziaDvere(int m, int n) {
        if (m >= 0 && m <= this.pocRiadkov && n >= 0 && n <= this.pocStlpcov) {
            return this.mapa[m][n].getHorizontalnaStenaTextura() == 9 || this.mapa[m][n].getVertikalnaStenaTextura() == 9;
        }
        return false;
    }

    /**
     * Vracia textúru na zadaných súradniciach pre horizontálnu alebo vertikálnu stenu.
     * @param m Riadok v mape
     * @param n Stĺpec v mape
     * @param hOrV Určuje, či ide o horizontálnu (0) alebo vertikálnu (1) stenu
     * @return Textúra zo zadaného miesta alebo predvolená textúra (ID 4), ak súradnice sú mimo mapy
     */
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

    public int getPocRiadkov() {
        return this.pocRiadkov;
    }

    public int getPocStlpcov() {
        return this.pocStlpcov;
    }

    public int getCellVelkost() {
        return this.velkostPolicka;
    }
}