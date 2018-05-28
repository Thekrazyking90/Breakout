package ids.univpm.breakout.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ids.univpm.breakout.model.database.Beacon.BeaconStrings;
import ids.univpm.breakout.model.database.Mappa.MappaStrings;
import ids.univpm.breakout.model.database.Nodi.NodiStrings;
import ids.univpm.breakout.model.database.PDI.PDIStrings;
import ids.univpm.breakout.model.database.Piano.PianoStrings;
import ids.univpm.breakout.model.database.Scala.ScalaStrings;
import ids.univpm.breakout.model.database.Tronchi.TronchiStrings;
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

        String q2="CREATE TABLE "+ TronchiStrings.TBL_NAME+
                " (" + TronchiStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                TronchiStrings.FIELD_ID_MAPPA + " INTEGER," +
                TronchiStrings.FIELD_ID_NODE1 +" TEXT NOT NULL UNIQUE," +
                TronchiStrings.FIELD_ID_BEACON +" REAL NOT NULL," +
                TronchiStrings.FIELD_ID_NODE2 +" REAL NOT NULL," +
                TronchiStrings.FIELD_LENGTH +" REAL NOT NULL, " +
                "FOREIGN KEY(" + TronchiStrings.FIELD_ID_MAPPA + ") REFERENCES Mappa ( "+ MappaStrings.FIELD_ID +" ) ON DELETE CASCADE," +
                "FOREIGN KEY(" + TronchiStrings.FIELD_ID_NODE1 + ") REFERENCES Nodi ( "+ NodiStrings.FIELD_ID +" ) ON DELETE CASCADE," +
                "FOREIGN KEY(" + TronchiStrings.FIELD_ID_NODE2 + ") REFERENCES Nodi ( "+ NodiStrings.FIELD_ID +" ) ON DELETE CASCADE," +
                "FOREIGN KEY(" + TronchiStrings.FIELD_ID_BEACON + ") REFERENCES Beacon ( "+ BeaconStrings.FIELD_ID +" ) ON DELETE CASCADE);";

        String q3="CREATE TABLE "+ ScalaStrings.TBL_NAME+
                " (" + ScalaStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                ScalaStrings.FIELD_ID_NODE1 + " INTEGER," +
                ScalaStrings.FIELD_ID_NODE2 +" INTEGER," +
                ScalaStrings.FIELD_LENGTH +" REAL NOT NULL," +
                ScalaStrings.FIELD_ID_BEACON +" INTEGER," +
                "FOREIGN KEY(" + ScalaStrings.FIELD_ID_NODE1 + ") REFERENCES Nodi ( "+ NodiStrings.FIELD_ID +" ) ON DELETE CASCADE," +
                "FOREIGN KEY(" + ScalaStrings.FIELD_ID_NODE2 + ") REFERENCES Nodi ( "+ NodiStrings.FIELD_ID +" ) ON DELETE CASCADE," +
                "FOREIGN KEY(" + ScalaStrings.FIELD_ID_BEACON + ") REFERENCES Beacon ( "+ BeaconStrings.FIELD_ID +" ) ON DELETE SET NULL );";

        String q4="CREATE TABLE "+ PianoStrings.TBL_NAME+
                " (" + PianoStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                PianoStrings.FIELD_ALTITUDE + " TEXT NOT NULL );";

        String q5="CREATE TABLE "+ PDIStrings.TBL_NAME+
                " (" + PDIStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                PDIStrings.FIELD_TYPE + " TEXT NOT NULL," +
                PDIStrings.FIELD_DESCRIPTION +" TEXT NOT NULL," +
                PDIStrings.FIELD_COORD_Y +" REAL NOT NULL," +
                PDIStrings.FIELD_COORD_X +" REAL NOT NULL," +
                PDIStrings.FIELD_WIDTH +" REAL, " +
                PDIStrings.FIELD_LENGTH +" REAL );";

        String q6="CREATE TABLE "+ NodiStrings.TBL_NAME+
                " (" + NodiStrings.FIELD_ID + "INTEGER PRIMARY KEY," +
                NodiStrings.FIELD_ID_PIANO + " INTEGER," +
                NodiStrings.FIELD_CODE +" TEXT NOT NULL UNIQUE," +
                NodiStrings.FIELD_COORD_Y +" REAL NOT NULL," +
                NodiStrings.FIELD_COORD_X +" REAL NOT NULL," +
                NodiStrings.FIELD_WIDTH +" REAL NOT NULL, " +
                "FOREIGN KEY(" + NodiStrings.FIELD_ID_PIANO + ") REFERENCES Piano ( "+ PianoStrings.FIELD_ID +" ) ON DELETE CASCADE);";

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
                "FOREIGN KEY(" + BeaconStrings.FIELD_ID_PDI + ") REFERENCES PDI ( "+ PDIStrings.FIELD_ID +" ) ON DELETE SET NULL);";
        db.execSQL(q1);
        db.execSQL(q2);
        db.execSQL(q3);
        db.execSQL(q4);
        db.execSQL(q5);
        db.execSQL(q6);
        db.execSQL(q7);
        db.execSQL(q8);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UtenteStrings.TBL_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + TronchiStrings.TBL_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + ScalaStrings.TBL_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + PianoStrings.TBL_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + PDIStrings.TBL_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + NodiStrings.TBL_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + MappaStrings.TBL_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + BeaconStrings.TBL_NAME );
        onCreate(db);

    }
}
