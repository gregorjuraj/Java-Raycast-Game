package levely.levelData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Trieda na načítanie dát herného levelu zo súboru.
 * Parsuje mapu levelu a inicializuje údaje o stenách.
 */
public class LoadLevel {
    private int pocetRiadok;
    private int pocetSlpcov;
    private WallData[][] mapa;

    /**
     * Konštruktor pre načítanie levelu.
     * Načíta mapu zo súboru, inicializuje rozmery mapy a vyplní pole údajov o stenách.
     * @param mapName Názov súboru s mapou levelu
     */
    public LoadLevel(String mapName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("levelyMapyLayout/" + mapName);
            if (inputStream == null) {
                throw new IllegalStateException("Súbor nenájdený: levelyMapyLayout/" + mapName);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            int riadky = 0;
            int stlpce = 0;
            String line = "";
            // vypocet poctu riadkov a stlpcov
            while ((line = reader.readLine()) != null) {
                riadky++;
                if (riadky == 1) {
                    stlpce = line.split(" ").length;
                }
            }
            reader.close();
            this.pocetRiadok = riadky;
            this.pocetSlpcov = stlpce;

            this.mapa = new WallData[riadky][stlpce];

            // naplnenie udajov
            inputStream = getClass().getClassLoader().getResourceAsStream("levelyMapyLayout/" + mapName);
            if (inputStream == null) {
                throw new IllegalStateException("Súbor nenájdený: levelyMapyLayout/" + mapName);
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            int m = 0;
            while ((line = reader.readLine()) != null) {
                String[] riadok = line.split(" ");
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
            reader.close();

        } catch (IOException e) {
            throw new IllegalStateException("Chyba pri načítavaní súboru: levelyMapyLayout/" + mapName, e);
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