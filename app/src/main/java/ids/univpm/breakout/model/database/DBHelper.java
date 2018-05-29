package ids.univpm.breakout.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ids.univpm.breakout.model.database.Beacon.BeaconStrings;
import ids.univpm.breakout.model.database.Mappa.MappaStrings;
import ids.univpm.breakout.model.database.Modifiche.ModificheStrings;
import ids.univpm.breakout.model.database.Nodi.NodoStrings;
import ids.univpm.breakout.model.database.Piano.PianoStrings;
import ids.univpm.breakout.model.database.Tronchi.TroncoStrings;
import ids.univpm.breakout.model.database.Utente.UtenteStrings;

public class DBHelper extends SQLiteOpenHelper{

    public static final String DBNAME="Breakout_DB";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String q1="CREATE TABLE "+UtenteStrings.TBL_NAME+
                " (" + UtenteStrings.FIELD_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                UtenteStrings.FIELD_USER + " TEXT NOT NULL UNIQUE," +
                UtenteStrings.FIELD_PSW+" TEXT NOT NULL," +
                UtenteStrings.FIELD_EMAIL+" TEXT NOT NULL UNIQUE," +
                UtenteStrings.FIELD_NAME+" TEXT NOT NULL," +
                UtenteStrings.FIELD_SURNAME+" TEXT NOT NULL);";

        String q2="CREATE TABLE "+ TroncoStrings.TBL_NAME+
                " (" + TroncoStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                TroncoStrings.FIELD_ID_NODE1 +" TEXT NOT NULL UNIQUE," +
                TroncoStrings.FIELD_ID_BEACON +" REAL NOT NULL," +
                TroncoStrings.FIELD_ID_NODE2 +" REAL NOT NULL," +
                TroncoStrings.FIELD_LENGTH +" REAL NOT NULL, " +
                "FOREIGN KEY(" + TroncoStrings.FIELD_ID_NODE1 + ") REFERENCES Nodi ( "+ NodoStrings.FIELD_ID +" ) ON DELETE CASCADE," +
                "FOREIGN KEY(" + TroncoStrings.FIELD_ID_NODE2 + ") REFERENCES Nodi ( "+ NodoStrings.FIELD_ID +" ) ON DELETE CASCADE," +
                "FOREIGN KEY(" + TroncoStrings.FIELD_ID_BEACON + ") REFERENCES Beacon ( "+ BeaconStrings.FIELD_ID +" ) ON DELETE CASCADE);";

        String q3="CREATE TABLE "+ ModificheStrings.TBL_NAME+
                " (" + ModificheStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                ModificheStrings.FIELD_DATE +" NUMERIC NOT NULL," +
                ModificheStrings.FIELD_ID_OGG_MOD +" INTEGER NOT NULL," +
                ModificheStrings.FIELD_TBL +" TEXT NOT NULL," +
                ModificheStrings.FIELD_TYPE +" TEXT NOT NULL);";

        String q4="CREATE TABLE "+ PianoStrings.TBL_NAME+
                " (" + PianoStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                PianoStrings.FIELD_ALTITUDE + " TEXT NOT NULL );";

        String q6="CREATE TABLE "+ NodoStrings.TBL_NAME+
                " (" + NodoStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                NodoStrings.FIELD_ID_MAPPA + " INTEGER," +
                NodoStrings.FIELD_CODE +" TEXT NOT NULL UNIQUE," +
                NodoStrings.FIELD_COORD_Y +" REAL NOT NULL," +
                NodoStrings.FIELD_COORD_X +" REAL NOT NULL," +
                NodoStrings.FIELD_WIDTH +" REAL NOT NULL, " +
                NodoStrings.FIELD_LENGTH +" REAL, " +
                NodoStrings.FIELD_IS_PDI +" NUMERIC NOT NULL DEFAULT 0, " +
                NodoStrings.FIELD_TYPE +" TEXT, " +
                "FOREIGN KEY(" + NodoStrings.FIELD_ID_MAPPA + ") REFERENCES Piano ( "+ PianoStrings.FIELD_ID +" ) ON DELETE CASCADE);";

        String q7="CREATE TABLE "+ MappaStrings.TBL_NAME+
                " (" + MappaStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                MappaStrings.FIELD_ID_PIANO + " INTEGER," +
                MappaStrings.FIELD_NAME +" TEXT NOT NULL," +
                MappaStrings.FIELD_IMG +" TEXT NOT NULL," +
                "FOREIGN KEY(" + MappaStrings.FIELD_ID_PIANO + ") REFERENCES Piano ( "+ PianoStrings.FIELD_ID +" ) ON DELETE CASCADE);";

        String q8="CREATE TABLE "+ BeaconStrings.TBL_NAME+
                " (" + BeaconStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                BeaconStrings.FIELD_ID_PDI + " INTEGER," +
                BeaconStrings.FIELD_FIRE +" REAL NOT NULL," +
                BeaconStrings.FIELD_COORD_Y +" REAL NOT NULL," +
                BeaconStrings.FIELD_COORD_X +" REAL NOT NULL," +
                BeaconStrings.FIELD_SMOKE +" REAL NOT NULL, " +
                BeaconStrings.FIELD_LOS +" REAL NOT NULL, " +
                BeaconStrings.FIELD_RISK +" REAL NOT NULL, " +
                "FOREIGN KEY(" + BeaconStrings.FIELD_ID_PDI + ") REFERENCES Nodo ( "+ NodoStrings.FIELD_ID +" ) ON DELETE SET NULL);";
        db.execSQL(q1);
        db.execSQL(q2);
        db.execSQL(q3);
        db.execSQL(q4);
        db.execSQL(q6);
        db.execSQL(q7);
        db.execSQL(q8);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
