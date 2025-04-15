package levely.levelData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoadLevel {
    private int pocetRiadok;
    private int pocetSlpcov;
    private WallData[][] mapa;

    public LoadLevel(String mapName) {
        try {
            File init = new File("src/levely/levelyMapyLayout/" + mapName);
            Scanner initReader = new Scanner(init);
            int riadky = 0;
            int stlpce = 0;

            while (initReader.hasNextLine()) {
                String data = initReader.nextLine();
                riadky++;
                if (riadky == 1) {
                    stlpce = data.split(" ").length;
                }
            }
            initReader.close();
            this.pocetRiadok = riadky;
            this.pocetSlpcov = stlpce;

            this.mapa = new WallData[riadky][stlpce];

            File load = new File("src/levely/levelyMapyLayout/" + mapName);
            Scanner mapaReader = new Scanner(load);

            int m = 0;
            while (mapaReader.hasNextLine()) {
                String data = mapaReader.nextLine();
                String[] riadok = data.split(" ");
                int n = 0;
                for (String stena : riadok) {
                    this.mapa[m][n] = new WallData();
                    String[] cisla = stena.split("/");
                    this.mapa[m][n].setX(n);
                    this.mapa[m][n].setY(m);
                    this.mapa[m][n].setHorizontalnaStenaTextura(Integer.parseInt(cisla[0]));
                    this.mapa[m][n].setVertikalnaStenaTextura(Integer.parseInt(cisla[1]));
                    n++;
                }
                m++;
            }
            mapaReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("subor sa nenasiel");
            e.printStackTrace();
        }
    }

    public int getPocetRiadok() {
        return this.pocetRiadok;
    }

    public int getPocetSlpcov() {
        return this.pocetSlpcov;
    }

    public WallData[][] getMapa() {
        return this.mapa;
    }
}
