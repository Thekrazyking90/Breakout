package ids.univpm.breakout.model.database.PDI;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.DBHelper;

public class PDIManager {


        private DBHelper dbHelper;

        public PDIManager(Context ctx)
        {
            dbHelper =new DBHelper(ctx);
        }

        public void save(long id, float coordx, float coordy, String descr, String type, float width, float length)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            dbHelper.getWritableDatabase();

            ContentValues cv=new ContentValues();
            cv.put(PDIStrings.FIELD_ID, id);
            cv.put(PDIStrings.FIELD_COORD_X, coordx);
            cv.put(PDIStrings.FIELD_COORD_Y, coordy);
            cv.put(PDIStrings.FIELD_TYPE, type);
            cv.put(PDIStrings.FIELD_DESCRIPTION, descr);
            cv.put(PDIStrings.FIELD_WIDTH, width);
            cv.put(PDIStrings.FIELD_LENGTH, length);
            try
            {
                db.insert(PDIStrings.TBL_NAME, null,cv);
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
                if (db.delete(PDIStrings.TBL_NAME, PDIStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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
                crs=db.query(PDIStrings.TBL_NAME, null, null, null, null, null, null, null);
            }
            catch(SQLiteException sqle)
            {
                return null;
            }
            return crs;
        }


}
