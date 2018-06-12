package ids.univpm.breakout.model.database.Modifiche;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.sql.Timestamp;

import ids.univpm.breakout.model.database.DBHelper;
import ids.univpm.breakout.model.database.Mappa.MappaStrings;

public class ModificheManager {

    private Context context;
    private DBHelper dbHelper;

    public ModificheManager(Context ctx)
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

    public void save(long id, long id_ogg_mod, String type, String table, int date)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        dbHelper.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(ModificheStrings.FIELD_ID, id);
        cv.put(ModificheStrings.FIELD_DATE, date);
        cv.put(ModificheStrings.FIELD_TYPE, type);
        cv.put(ModificheStrings.FIELD_TBL, table);
        cv.put(ModificheStrings.FIELD_ID_OGG_MOD, id_ogg_mod);
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
            if (db.delete(ModificheStrings.TBL_NAME, ModificheStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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
            crs=db.query(ModificheStrings.TBL_NAME, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }
}
