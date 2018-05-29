package ids.univpm.breakout.model.database.Utente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.DBHelper;

public class UtenteManager {


        private DBHelper dbHelper;

        public UtenteManager(Context ctx)
        {
            dbHelper =new DBHelper(ctx);
        }

        public void save(String email, String name, String password, String surname, String username, boolean is_logged)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            dbHelper.getWritableDatabase();

            ContentValues cv=new ContentValues();
            cv.put(UtenteStrings.FIELD_EMAIL, email);
            cv.put(UtenteStrings.FIELD_NAME, name);
            cv.put(UtenteStrings.FIELD_PSW, password);
            cv.put(UtenteStrings.FIELD_SURNAME, surname);
            cv.put(UtenteStrings.FIELD_USER, username);
            cv.put(UtenteStrings.FIELD_IS_LOGGED, is_logged);
            try
            {
                db.insert(UtenteStrings.TBL_NAME, null,cv);
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
                if (db.delete(UtenteStrings.TBL_NAME, UtenteStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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
                crs=db.query(UtenteStrings.TBL_NAME, null, null, null, null, null, null, null);
            }
            catch(SQLiteException sqle)
            {
                return null;
            }
            return crs;
        }


}
