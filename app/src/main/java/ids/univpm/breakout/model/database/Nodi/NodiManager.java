package ids.univpm.breakout.model.database.Nodi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.DBHelper;

public class NodiManager {


        private DBHelper dbHelper;

        public NodiManager(Context ctx)
        {
            dbHelper =new DBHelper(ctx);
        }

        public void save(long id, long piano, float coordx, float coordy, String code, float width)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            dbHelper.getWritableDatabase();

            ContentValues cv=new ContentValues();
            cv.put(NodiStrings.FIELD_ID, id);
            cv.put(NodiStrings.FIELD_ID_PIANO, piano);
            cv.put(NodiStrings.FIELD_COORD_X, coordx);
            cv.put(NodiStrings.FIELD_COORD_Y, coordy);
            cv.put(NodiStrings.FIELD_CODE, code);
            cv.put(NodiStrings.FIELD_WIDTH, width);
            try
            {
                db.insert(NodiStrings.TBL_NAME, null,cv);
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
                if (db.delete(NodiStrings.TBL_NAME, NodiStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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
                crs=db.query(NodiStrings.TBL_NAME, null, null, null, null, null, null, null);
            }
            catch(SQLiteException sqle)
            {
                return null;
            }
            return crs;
        }


}
