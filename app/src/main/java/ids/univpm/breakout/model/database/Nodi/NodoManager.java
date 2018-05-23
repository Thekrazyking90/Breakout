package ids.univpm.breakout.model.database.Nodi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.DBHelper;

public class NodoManager {


        private DBHelper dbHelper;

        public NodoManager(Context ctx)
        {
            dbHelper =new DBHelper(ctx);
        }

        public void save(long id, long piano, float coordx, float coordy, String code, float width, float length, boolean is_pdi, String type)
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

        public boolean deleteByID(long id)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            try
            {
                if (db.delete(NodoStrings.TBL_NAME, NodoStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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


}
