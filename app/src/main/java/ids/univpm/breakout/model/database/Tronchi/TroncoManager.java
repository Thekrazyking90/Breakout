package ids.univpm.breakout.model.database.Tronchi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.Scala;
import ids.univpm.breakout.model.Tronco;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;
import ids.univpm.breakout.model.database.DBHelper;

public class TroncoManager {


    private DBHelper dbHelper;
    private Context context;

    public TroncoManager(Context ctx)
    {
        setContext(ctx);
        dbHelper =new DBHelper(ctx);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void save(Integer id, Integer node1, Integer node2, Integer beacon, float length)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        dbHelper.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(TroncoStrings.FIELD_ID, id);
        cv.put(TroncoStrings.FIELD_ID_NODE1, node1);
        cv.put(TroncoStrings.FIELD_ID_NODE2, node2);
        cv.put(TroncoStrings.FIELD_ID_BEACON, beacon);
        cv.put(TroncoStrings.FIELD_LENGTH, length);
        try
        {
            db.insert(TroncoStrings.TBL_NAME, null,cv);
        }
        catch (SQLiteException sqle)
        {
            // Gestione delle eccezioni
        }
    }

    public boolean deleteByID(Integer id)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            if (db.delete(TroncoStrings.TBL_NAME, TroncoStrings.FIELD_ID+"=?", new String[]{Integer.toString(id)})>0)
                return true;
            return false;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }

    }

    public Cursor query()
    {
        Cursor crs=null;
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(TroncoStrings.TBL_NAME, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }


    public Integer[] getArcsByNode_Integer(Integer id_nodo) {
        Cursor crs=null;
        String[] args = new String[] {Integer.toString(id_nodo), Integer.toString(id_nodo)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(TroncoStrings.TBL_NAME, null, TroncoStrings.FIELD_ID_NODE1 + " = ? OR " + TroncoStrings.FIELD_ID_NODE2 + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        Integer[] listaTronchi = null;
        int i = 0;

        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            listaTronchi[i] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID));
            i++;
        }

        return listaTronchi;
    }

    private Beacon getBeacon(Integer id_beacon) {
        BeaconManager beaconMng = new BeaconManager(context);
        return beaconMng.findById(id_beacon);
    }

    public Tronco findByIdGeneric(Integer arcId) {
        Cursor crs=null;
        Tronco arc = new Tronco();
        String[] args = new String[] {Integer.toString(arcId)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(TroncoStrings.TBL_NAME, null, TroncoStrings.FIELD_ID + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        crs.moveToFirst();
        arc.setID(arcId);
        arc.setBeacon(getBeacon(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_BEACON))));

        arc.setLarghezza_media(context);

        arc.setCosto_totale_normalizzato();

        Integer[] nodes = null;
        nodes[0] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_NODE1));
        nodes[1] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_NODE2));
        arc.setNodi_Integer(nodes);

        return arc;
    }

    public Scala getByBeaconID(Integer id_beacon) {
        return null;
    }

    public boolean resetTable(){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            db.execSQL("DROP TABLE "+ TroncoStrings.TBL_NAME+";");
            db.execSQL("CREATE TABLE "+ TroncoStrings.TBL_NAME+
                    " (" + TroncoStrings.FIELD_ID + " INTEGER PRIMARY KEY," +
                    TroncoStrings.FIELD_ID_NODE1 +" TEXT NOT NULL UNIQUE," +
                    TroncoStrings.FIELD_ID_BEACON +" REAL NOT NULL," +
                    TroncoStrings.FIELD_ID_NODE2 +" REAL NOT NULL," +
                    TroncoStrings.FIELD_LENGTH +" REAL NOT NULL);");
            return true;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }

    public Tronco findByIdBeacon(Integer id_beacon) {
        Cursor crs=null;
        Tronco arc = new Tronco();
        String[] args = new String[] {Integer.toString(id_beacon)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(TroncoStrings.TBL_NAME, null, TroncoStrings.FIELD_ID_BEACON + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        crs.moveToFirst();
        arc.setID(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID)));
        arc.setBeacon(getBeacon(id_beacon));

        arc.setLarghezza_media(context);

        arc.setCosto_totale_normalizzato();

        Integer[] nodes = null;
        nodes[0] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_NODE1));
        nodes[1] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_NODE2));
        arc.setNodi_Integer(nodes);

        return arc;
    }

    public ArrayList<Tronco> findByIdMapAndRoute(Integer idMap, ArrayList<Integer> cammino) {

    }
}
