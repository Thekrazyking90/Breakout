package ids.univpm.breakout.utility;


import java.util.ArrayList;

public class Percorso {
    private static boolean gestionePercorso;
    public static ArrayList<Integer> cammino = new ArrayList<>();

    public static boolean isGestionePercorso() {
        return gestionePercorso;
    }

    public static void setGestionePercorso(boolean gestionePercorso) {
        Percorso.gestionePercorso = gestionePercorso;
    }
}
