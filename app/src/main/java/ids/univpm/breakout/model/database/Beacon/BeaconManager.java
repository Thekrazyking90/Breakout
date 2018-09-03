package ids.univpm.breakout.model.database.Beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.database.DBHelper;

public class BeaconManager {

    private Context context;
        private DBHelper dbHelper;

        public BeaconManager(Context ctx)
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

    public void save(Integer id, Integer idMap, Integer idFloor, String address, Integer pdi, float coordx, float coordy, float fire, float smoke, float ncd, float risk)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            dbHelper.getWritableDatabase();

            ContentValues cv=new ContentValues();
            cv.put(BeaconStrings.FIELD_ID, id);
            cv.put(BeaconStrings.FIELD_ID_MAP, idMap);
            cv.put(BeaconStrings.FIELD_ID_FLOOR, idFloor);
            cv.put(BeaconStrings.FIELD_ADDRESS, address);
            cv.put(BeaconStrings.FIELD_ID_PDI, pdi);
            cv.put(BeaconStrings.FIELD_COORD_X, coordx);
            cv.put(BeaconStrings.FIELD_COORD_Y, coordy);
            cv.put(BeaconStrings.FIELD_FIRE, fire);
            cv.put(BeaconStrings.FIELD_SMOKE, smoke);
            cv.put(BeaconStrings.FIELD_NCD, ncd);
            cv.put(BeaconStrings.FIELD_RISK, risk);
            try
            {
                db.insert(BeaconStrings.TBL_NAME, null,cv);
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
                if (db.delete(BeaconStrings.TBL_NAME, BeaconStrings.FIELD_ID+"=?", new String[]{Integer.toString(id)})>0)
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
                crs=db.query(BeaconStrings.TBL_NAME, null, null, null, null, null, null, null);
            }
            catch(SQLiteException sqle)
            {
                return null;
            }
            return crs;
        }


    public ArrayList<Integer> getBeaconsByPdi_Integer(Integer id_pdi) {
        Cursor crs;
        String[] args = new String[] {Integer.toString(id_pdi)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(BeaconStrings.TBL_NAME, null, BeaconStrings.FIELD_ID_PDI + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        ArrayList<Integer> listaBeacon = new ArrayList<>();

        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            listaBeacon.add(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID)));
        }

        crs.close();

        return listaBeacon;
    }

    public Beacon findById(Integer id) {
        Cursor crs;
        Beacon beacon = new Beacon();
        String[] args = new String[] {Integer.toString(id)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(BeaconStrings.TBL_NAME, null, BeaconStrings.FIELD_ID + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        if(crs.moveToFirst()) {
            beacon.setID_beacon(id);
            beacon.setAddress(crs.getString(crs.getColumnIndex(BeaconStrings.FIELD_ADDRESS)));
            beacon.setCoord_X(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_COORD_X)));
            beacon.setCoord_Y(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_COORD_Y)));
            beacon.setID_pdi(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID_PDI)));
            beacon.setID_map(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID_MAP)));
            beacon.setID_floor(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID_FLOOR)));
            beacon.setInd_fumi(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_SMOKE)));
            beacon.setInd_fuoco(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_FIRE)));
            beacon.setInd_rischio(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_RISK)));
            beacon.setInd_NCD(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_NCD)));
        }
        crs.close();

        return beacon;
    }

    public ArrayList<Beacon> findAll() {
        ArrayList<Beacon> listaBeacon = new ArrayList<>();
        Cursor crs = query();
        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            Beacon beacon = findById(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID)));
            listaBeacon.add(beacon);
        }
        crs.close();

        return listaBeacon;
    }

    public boolean resetTable(){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            db.execSQL("DROP TABLE "+ BeaconStrings.TBL_NAME+";");
            db.execSQL("CREATE TABLE "+ BeaconStrings.TBL_NAME+
                    " (" + BeaconStrings.FIELD_ID + " INTEGER PRIMARY KEY," +
                    BeaconStrings.FIELD_ADDRESS + "TEXT NOT NULL UNIQUE," +
                    BeaconStrings.FIELD_ID_PDI + " INTEGER," +
                    BeaconStrings.FIELD_ID_MAP +" INTEGER," +
                    BeaconStrings.FIELD_ID_FLOOR +" INTEGER," +
                    BeaconStrings.FIELD_FIRE +" REAL NOT NULL," +
                    BeaconStrings.FIELD_COORD_Y +" REAL NOT NULL," +
                    BeaconStrings.FIELD_COORD_X +" REAL NOT NULL," +
                    BeaconStrings.FIELD_SMOKE +" REAL NOT NULL, " +
                    BeaconStrings.FIELD_NCD +" REAL NOT NULL, " +
                    BeaconStrings.FIELD_RISK +" REAL NOT NULL);");
            return true;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }

    public Beacon findByAddress(String address) {
        Cursor crs;
        Beacon beacon = new Beacon();
        String[] args = new String[] {address};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(BeaconStrings.TBL_NAME, null, BeaconStrings.FIELD_ADDRESS + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        if(crs.moveToFirst()) {
            beacon.setID_beacon(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID)));
            beacon.setAddress(address);
            beacon.setCoord_X(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_COORD_X)));
            beacon.setCoord_Y(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_COORD_Y)));
            beacon.setID_pdi(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID_PDI)));
            beacon.setID_map(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID_MAP)));
            beacon.setID_floor(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID_FLOOR)));
            beacon.setInd_fumi(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_SMOKE)));
            beacon.setInd_fuoco(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_FIRE)));
            beacon.setInd_rischio(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_RISK)));
            beacon.setInd_NCD(crs.getFloat(crs.getColumnIndex(BeaconStrings.FIELD_NCD)));
        }
        crs.close();

        return beacon;

    }

    public ArrayList<Beacon> findAllByIdMap(Integer idMap) {
        Cursor crs;
        Beacon beacon;
        ArrayList<Beacon> listaBeacon = new ArrayList<>();
        String[] args = new String[] {Integer.toString(idMap)};
        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(BeaconStrings.TBL_NAME, null, BeaconStrings.FIELD_ID_MAP + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            beacon = findById(crs.getInt(crs.getColumnIndex(BeaconStrings.FIELD_ID)));
            listaBeacon.add(beacon);
        }

        crs.close();

        return listaBeacon;
    }

}
