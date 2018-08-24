package ids.univpm.breakout.model.database.Piano;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.DBHelper;

public class PianoManager {

    private Context context;
    private DBHelper dbHelper;

    public PianoManager(Context ctx)
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

    public void save(Integer id, String alt)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        dbHelper.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(PianoStrings.FIELD_ID, id);
        cv.put(PianoStrings.FIELD_ALTITUDE, alt);
        try
        {
            db.insert(PianoStrings.TBL_NAME, null,cv);
        }
        catch (SQLiteException sqle)
        {
            // Gestione delle eccezioni
        }
    }

    public boolean deleteByID(Integer id)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            if (db.delete(PianoStrings.TBL_NAME, PianoStrings.FIELD_ID+"=?", new String[]{Integer.toString(id)})>0)
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
            crs=db.query(PianoStrings.TBL_NAME, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }

    public boolean resetTable(){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            db.execSQL("DROP TABLE "+ PianoStrings.TBL_NAME+";");
            db.execSQL("CREATE TABLE "+ PianoStrings.TBL_NAME+
                    " (" + PianoStrings.FIELD_ID + " INTEGER PRIMARY KEY," +
                    PianoStrings.FIELD_ALTITUDE + " TEXT NOT NULL );");
            return true;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }
}
