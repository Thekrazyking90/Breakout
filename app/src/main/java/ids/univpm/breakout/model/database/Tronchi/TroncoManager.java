package ids.univpm.breakout.model.database.Tronchi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.Beacon.BeaconStrings;
import ids.univpm.breakout.model.database.DBHelper;

public class TroncoManager {


        private DBHelper dbHelper;

        public TroncoManager(Context ctx)
        {
            dbHelper =new DBHelper(ctx);
        }

        public void save(long id, long node1, long node2, long beacon, float length)
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

        public boolean deleteByID(long id)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            try
            {
                if (db.delete(TroncoStrings.TBL_NAME, TroncoStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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


    public static Long[] getArcsByNode_Long(long id_nodo, DBHelper dbHlpr) {
        Cursor crs=null;
        String[] args = new String[] {Long.toString(id_nodo), Long.toString(id_nodo)};
        try
        {
            SQLiteDatabase db= dbHlpr.getReadableDatabase();
            crs=db.query(TroncoStrings.TBL_NAME, null, TroncoStrings.FIELD_ID_NODE1 + " = ? OR " + TroncoStrings.FIELD_ID_NODE2 + " = ?", args, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

        Long[] listaTronchi = null;
        int i = 0;

        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            listaTronchi[i] = crs.getLong(crs.getColumnIndex(TroncoStrings.FIELD_ID));
            i++;
        }

        return listaTronchi;
    }
}
