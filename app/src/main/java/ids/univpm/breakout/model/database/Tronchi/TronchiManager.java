package ids.univpm.breakout.model.database.Tronchi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.DBHelper;

public class TronchiManager {


        private DBHelper dbHelper;

        public TronchiManager(Context ctx)
        {
            dbHelper =new DBHelper(ctx);
        }

        public void save(long node1, long node2, long beacon, long map, float length)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            dbHelper.getWritableDatabase();

            ContentValues cv=new ContentValues();
            cv.put(TronchiStrings.FIELD_ID_NODE1, node1);
            cv.put(TronchiStrings.FIELD_ID_NODE2, node2);
            cv.put(TronchiStrings.FIELD_ID_MAPPA, map);
            cv.put(TronchiStrings.FIELD_ID_BEACON, beacon);
            cv.put(TronchiStrings.FIELD_LENGTH, length);
            try
            {
                db.insert(TronchiStrings.TBL_NAME, null,cv);
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
                if (db.delete(TronchiStrings.TBL_NAME, TronchiStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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
                crs=db.query(TronchiStrings.TBL_NAME, null, null, null, null, null, null, null);
            }
            catch(SQLiteException sqle)
            {
                return null;
            }
            return crs;
        }


}
