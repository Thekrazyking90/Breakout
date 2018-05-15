package ids.univpm.breakout.model.database.Mappa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.DBHelper;

public class MappaManager {


        private DBHelper dbHelper;

        public MappaManager(Context ctx)
        {
            dbHelper =new DBHelper(ctx);
        }

        public void save(long id, long piano, String img, String name)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            dbHelper.getWritableDatabase();

            ContentValues cv=new ContentValues();
            cv.put(MappaStrings.FIELD_ID, id);
            cv.put(MappaStrings.FIELD_IMG, img);
            cv.put(MappaStrings.FIELD_NAME, name);
            cv.put(MappaStrings.FIELD_ID_PIANO, piano);
            try
            {
                db.insert(MappaStrings.TBL_NAME, null,cv);
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
                if (db.delete(MappaStrings.TBL_NAME, MappaStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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
                crs=db.query(MappaStrings.TBL_NAME, null, null, null, null, null, null, null);
            }
            catch(SQLiteException sqle)
            {
                return null;
            }
            return crs;
        }


}
