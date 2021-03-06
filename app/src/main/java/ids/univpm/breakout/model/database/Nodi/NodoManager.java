package ids.univpm.breakout.model.database.Nodi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.Nodo;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;
import ids.univpm.breakout.model.database.DBHelper;
import ids.univpm.breakout.model.database.Tronchi.TroncoManager;
import ids.univpm.breakout.model.database.Tronchi.TroncoStrings;

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

    public void save(Integer id, Integer idMappa, float coordx, float coordy, String code, float width, float length, boolean is_pdi, String type)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        dbHelper.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(NodoStrings.FIELD_ID, id);
        cv.put(NodoStrings.FIELD_ID_MAPPA, idMappa);
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
        db.close();

    }

    public boolean deleteByID(Integer id)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            return db.delete(NodoStrings.TBL_NAME, NodoStrings.FIELD_ID + "=?", new String[]{Integer.toString(id)}) > 0;
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
            crs=db.query(NodoStrings.TBL_NAME, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }

    public ArrayList<Pdi> findAllPdi() {
        Cursor crs;
        ArrayList<Pdi> listaPdi = new ArrayList<>();
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(NodoStrings.TBL_NAME, null, NodoStrings.FIELD_TYPE+ " is not null", null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            Pdi pdi = findPdiByID(crs.getInt(crs.getColumnIndex(NodoStrings.FIELD_ID)));
            listaPdi.add(pdi);
        }
        return listaPdi;
    }

    public Pdi findPdiByID(Integer id_pdi) {
        Cursor crs;
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

        if(crs.moveToFirst()) {
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
        }
        crs.close();

        return pdi;
    }

    private Integer[] getStar_Integer(Integer id_pdi) {
        TroncoManager troncoMng = new TroncoManager(context);
        ArrayList<Integer> listaId = troncoMng.getArcsByNode_Integer(id_pdi);

        Integer[] arrayId = new Integer[listaId.size()];

        int i = 0;

        for (Integer id: listaId) {
            arrayId[i] = id;
            i++;
        }

        return arrayId;
    }

    private Integer[] getBeacons_Integer(Integer id_pdi) {
        BeaconManager beaconMng = new BeaconManager(context);
        ArrayList<Integer> listaId = beaconMng.getBeaconsByPdi_Integer(id_pdi);

        Integer[] arrayId = new Integer[listaId.size()];

        int i = 0;

        for (Integer id: listaId) {
            arrayId[i] = id;
            i++;
        }

        return arrayId;
    }

    public Nodo findById(Integer id) {
        Cursor crs;
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

        if(crs.moveToFirst()) {
            nodo.setID(id);
            nodo.setLarghezza(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_WIDTH)));
            nodo.setTronchi_stella_Integer(getStar_Integer(id));
            nodo.setCodice(crs.getString(crs.getColumnIndex(NodoStrings.FIELD_CODE)));
            nodo.setCoord_X(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_COORD_X)));
            nodo.setCoord_Y(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_COORD_Y)));
            nodo.setID_mappa(crs.getInt(crs.getColumnIndex(NodoStrings.FIELD_ID_MAPPA)));
        }
        crs.close();

        return nodo;
    }

    public boolean resetTable(){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            db.execSQL("DROP TABLE "+ NodoStrings.TBL_NAME+";");
            db.execSQL("CREATE TABLE "+ NodoStrings.TBL_NAME+
                    " (" + NodoStrings.FIELD_ID + " INTEGER PRIMARY KEY," +
                    NodoStrings.FIELD_ID_MAPPA + " INTEGER," +
                    NodoStrings.FIELD_CODE +" TEXT NOT NULL UNIQUE," +
                    NodoStrings.FIELD_COORD_Y +" REAL NOT NULL," +
                    NodoStrings.FIELD_COORD_X +" REAL NOT NULL," +
                    NodoStrings.FIELD_WIDTH +" REAL NOT NULL, " +
                    NodoStrings.FIELD_LENGTH +" REAL, " +
                    NodoStrings.FIELD_IS_PDI +" NUMERIC NOT NULL DEFAULT 0, " +
                    NodoStrings.FIELD_TYPE +" TEXT);");
            return true;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }

    public ArrayList<Nodo> findAllByIdMap(Integer idMap) {
        Cursor crs;
        Nodo nodo;
        ArrayList<Nodo> listaNodi = new ArrayList<>();
        String[] args = new String[] {Integer.toString(idMap)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(NodoStrings.TBL_NAME, null, NodoStrings.FIELD_ID_MAPPA + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
                nodo = findById(crs.getInt(crs.getColumnIndex(NodoStrings.FIELD_ID)));
                listaNodi.add(nodo);
        }

        crs.close();

        return listaNodi;

    }

    public Pdi findByTipo(String value) {
        Cursor crs;
        Pdi pdi = new Pdi();
        String[] args = new String[] {value};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(NodoStrings.TBL_NAME, null, NodoStrings.FIELD_TYPE + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        if(crs.moveToFirst()) {
            pdi.setID(crs.getInt(crs.getColumnIndex(NodoStrings.FIELD_ID)));
            pdi.setBeacon_Integer(getBeacons_Integer(pdi.getID()));
            pdi.setLunghezza(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_LENGTH)));
            pdi.setLarghezza(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_WIDTH)));
            pdi.setTronchi_stella_Integer(getStar_Integer(pdi.getID()));
            pdi.setTipo(crs.getString(crs.getColumnIndex(NodoStrings.FIELD_TYPE)));
            pdi.setCodice(crs.getString(crs.getColumnIndex(NodoStrings.FIELD_CODE)));
            pdi.setCoord_X(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_COORD_X)));
            pdi.setCoord_Y(crs.getFloat(crs.getColumnIndex(NodoStrings.FIELD_COORD_Y)));
            pdi.setID_mappa(crs.getInt(crs.getColumnIndex(NodoStrings.FIELD_ID_MAPPA)));
        }
        crs.close();

        return pdi;
    }
}
