package ids.univpm.breakout.model.database.Nodi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import ids.univpm.breakout.model.Nodo;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;
import ids.univpm.breakout.model.database.DBHelper;
import ids.univpm.breakout.model.database.Tronchi.TroncoManager;

public class NodoManager{

    private Context context;
    private DBHelper dbHelper;

    public NodoManager(Context ctx)
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

    public void save(Integer id, Integer piano, float coordx, float coordy, String code, float width, float length, boolean is_pdi, String type)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        dbHelper.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(NodoStrings.FIELD_ID, id);
        cv.put(NodoStrings.FIELD_ID_MAPPA, piano);
        cv.put(NodoStrings.FIELD_COORD_X, coordx);
        cv.put(NodoStrings.FIELD_COORD_Y, coordy);
        cv.put(NodoStrings.FIELD_CODE, code);
        cv.put(NodoStrings.FIELD_WIDTH, width);
        cv.put(NodoStrings.FIELD_LENGTH, length);
        cv.put(NodoStrings.FIELD_IS_PDI, is_pdi);
        cv.put(NodoStrings.FIELD_TYPE, type);
        try
        {
            db.insert(NodoStrings.TBL_NAME, null,cv);
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
            if (db.delete(NodoStrings.TBL_NAME, NodoStrings.FIELD_ID+"=?", new String[]{Integer.toString(id)})>0)
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
            crs=db.query(NodoStrings.TBL_NAME, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }

    public ArrayList<Pdi> findAllPdi() {
        ArrayList<Pdi> listaPdi = new ArrayList<>();
        Cursor crs = query();
        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            Pdi pdi = findPdiByID(crs.getInt(crs.getColumnIndex(NodoStrings.FIELD_ID)));
            listaPdi.add(pdi);
        }
        return listaPdi;
    }

    private Pdi findPdiByID(Integer id_pdi) {
        Cursor crs=null;
        Pdi pdi = new Pdi();
        String[] args = new String[] {Integer.toString(id_pdi)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(NodoStrings.TBL_NAME, null, NodoStrings.FIELD_ID + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        crs.moveToFirst();
        pdi.setID(id_pdi);
        pdi.setBeacon_Integer(getBeacons_Integer(id_pdi));
        pdi.setLunghezza(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_LENGTH)));
        pdi.setLarghezza(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_WIDTH)));
        pdi.setTronchi_stella_Integer(getStar_Integer(id_pdi));
        pdi.setTipo(crs.getString(crs.getColumnIndex(NodoStrings.FIELD_TYPE)));
        pdi.setCodice(crs.getString(crs.getColumnIndex(NodoStrings.FIELD_CODE)));
        pdi.setCoord_X(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_COORD_X)));
        pdi.setCoord_Y(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_COORD_Y)));
        pdi.setID_mappa(crs.getInt(crs.getColumnIndex(NodoStrings.FIELD_ID_MAPPA)));

        return pdi;
    }

    private Integer[] getStar_Integer(Integer id_pdi) {
        TroncoManager troncoMng = new TroncoManager(context);
        return troncoMng.getArcsByNode_Integer(id_pdi);
    }

    private Integer[] getBeacons_Integer(Integer id_pdi) {
        BeaconManager beaconMng = new BeaconManager(context);
        return beaconMng.getBeaconsByPdi_Integer(id_pdi);
    }

    public Nodo findById(Integer id) {
        Cursor crs=null;
        Nodo nodo = new Nodo();
        String[] args = new String[] {Integer.toString(id)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(NodoStrings.TBL_NAME, null, NodoStrings.FIELD_ID + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        crs.moveToFirst();
        nodo.setID(id);
        nodo.setLarghezza(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_WIDTH)));
        nodo.setTronchi_stella_Integer(getStar_Integer(id));
        nodo.setCodice(crs.getString(crs.getColumnIndex(NodoStrings.FIELD_CODE)));
        nodo.setCoord_X(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_COORD_X)));
        nodo.setCoord_Y(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_COORD_Y)));
        nodo.setID_mappa(crs.getInt(crs.getColumnIndex(NodoStrings.FIELD_ID_MAPPA)));

        return nodo;
    }
}
