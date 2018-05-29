package ids.univpm.breakout.controller;

import android.content.Context;

import java.util.ArrayList;

import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.database.Mappa.MappaManager;

public class Controller {


    public static boolean checkLog() {
        return false;
    }


    public static boolean checkMappe() {
        return false;
    }

    public static ArrayList<Mappa> getMappe(Context ctx){
        ArrayList<Mappa> mappe;
        MappaManager mappaMng = new MappaManager(ctx);
        mappe = mappaMng.getMappe();
        return mappe;
    }

    public static boolean checkConnection() {
        return false;
    }
}
