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
import ids.univpm.breakout.model.database.Beacon.BeaconStrings;
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

    public void save(Integer id, Integer map, Integer floor, Integer node1, Integer node2, Integer beacon, float length)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        dbHelper.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(TroncoStrings.FIELD_ID, id);
        cv.put(TroncoStrings.FIELD_ID_NODE1, node1);
        cv.put(TroncoStrings.FIELD_ID_NODE2, node2);
        cv.put(TroncoStrings.FIELD_ID_MAP, map);
        cv.put(TroncoStrings.FIELD_ID_FLOOR, floor);
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
        db.close();

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
        Cursor crs;
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


    public ArrayList<Integer> getArcsByNode_Integer(Integer id_nodo) {
        Cursor crs;
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

        ArrayList<Integer> listaTronchi = new ArrayList<>();

        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            listaTronchi.add(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID)));
        }

        crs.close();

        return listaTronchi;
    }

    private Beacon getBeacon(Integer id_beacon) {
        BeaconManager beaconMng = new BeaconManager(context);
        return beaconMng.findById(id_beacon);
    }

    public Tronco findByIdGeneric(Integer arcId) {
        Cursor crs;
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

        if(crs.moveToFirst()) {
            arc.setID(arcId);
            arc.setBeacon(getBeacon(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_BEACON))));
            arc.setID_mappa(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_MAP)));
            arc.setID_Piano(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_FLOOR)));

            Integer[] nodes = new Integer[2];
            nodes[0] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_NODE1));
            nodes[1] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_NODE2));
            arc.setNodi_Integer(nodes);

            arc.setLarghezza_media(context);

            arc.setLunghezza(crs.getFloat(crs.getColumnIndex(TroncoStrings.FIELD_LENGTH)));

            arc.setCosto_totale_normalizzato();


        }
        crs.close();

        return arc;
    }


    public boolean resetTable(){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            db.execSQL("DROP TABLE "+ TroncoStrings.TBL_NAME+";");
            db.execSQL("CREATE TABLE "+ TroncoStrings.TBL_NAME+
                    " (" + TroncoStrings.FIELD_ID + " INTEGER PRIMARY KEY," +
                    TroncoStrings.FIELD_ID_NODE1 +" INTEGER," +
                    TroncoStrings.FIELD_ID_BEACON +" INTEGER," +
                    TroncoStrings.FIELD_ID_MAP +" INTEGER," +
                    TroncoStrings.FIELD_ID_FLOOR +" INTEGER," +
                    TroncoStrings.FIELD_ID_NODE2 +" INTEGER," +
                    TroncoStrings.FIELD_LENGTH +" REAL NOT NULL);");
            return true;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }

    public Tronco findByIdBeacon(Integer id_beacon) {
        Cursor crs;
        Tronco arc = new Tronco();
        String[] args = new String[] {Integer.toString(id_beacon)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(TroncoStrings.TBL_NAME, null, TroncoStrings.FIELD_ID_BEACON + " = ? ", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        if(crs.moveToFirst()) {
            arc.setID(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID)));
            arc.setBeacon(getBeacon(id_beacon));
            arc.setID_mappa(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_MAP)));
            arc.setID_Piano(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_FLOOR)));

            Integer[] nodes = new Integer[2];
            nodes[0] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_NODE1));
            nodes[1] = crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID_NODE2));
            arc.setNodi_Integer(nodes);

            arc.setLarghezza_media(context);

            arc.setLunghezza(crs.getFloat(crs.getColumnIndex(TroncoStrings.FIELD_LENGTH)));

            arc.setCosto_totale_normalizzato();


        }
        crs.close();

        return arc;
    }

    public ArrayList<Tronco> findByIdMapAndRoute(Integer idMap, ArrayList<Integer> cammino) {
        Cursor crs;
        Tronco arc;
        ArrayList<Tronco> listaTronchi = new ArrayList<>();
        String[] args = new String[] {Integer.toString(idMap)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(TroncoStrings.TBL_NAME, null, TroncoStrings.FIELD_ID_MAP + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            if(cammino.contains(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID)))) {
                arc = findByIdGeneric(crs.getInt(crs.getColumnIndex(TroncoStrings.FIELD_ID)));
                listaTronchi.add(arc);
            }
        }

        crs.close();

        return listaTronchi;
    }
}
