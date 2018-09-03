package ids.univpm.breakout.model.database.Modifiche;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.sql.Timestamp;

import ids.univpm.breakout.model.Modifica;
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

    public boolean save(Integer id, Integer id_ogg_mod, String type, String table, String date)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        dbHelper.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(ModificheStrings.FIELD_ID, id);
        cv.put(ModificheStrings.FIELD_DATE, date);
        cv.put(ModificheStrings.FIELD_TYPE, type);
        cv.put(ModificheStrings.FIELD_TBL, table);
        cv.put(ModificheStrings.FIELD_ID_OGG_MOD, id_ogg_mod);

        boolean flag;

        try
        {
            db.insert(ModificheStrings.TBL_NAME, null,cv);
            flag =true;
        }
        catch (SQLiteException sqle)
        {
            flag = false;
        }
        db.close();

        return flag;
    }

    public boolean deleteByID(Integer id)
    {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            if (db.delete(ModificheStrings.TBL_NAME, ModificheStrings.FIELD_ID+"=?", new String[]{Integer.toString(id)})>0)
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

        }
        return crs;
    }

    public Modifica findLast(){
        Modifica mod = new Modifica();
        Cursor crs;

        try
        {
            SQLiteDatabase db= dbHelper.getReadableDatabase();
            crs=db.query(ModificheStrings.TBL_NAME, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }

            if(crs.moveToLast()){
                mod.setID(crs.getInt(crs.getColumnIndex(ModificheStrings.FIELD_ID)));
                mod.setData(crs.getString(crs.getColumnIndex(ModificheStrings.FIELD_DATE)));
                mod.setTabella(crs.getString(crs.getColumnIndex(ModificheStrings.FIELD_TBL)));
                mod.setTipo(crs.getString(crs.getColumnIndex(ModificheStrings.FIELD_TYPE)));
                mod.setID_ogg_mod(crs.getInt(crs.getColumnIndex(ModificheStrings.FIELD_ID_OGG_MOD)));
            }else{
                mod = null;
            }

        crs.close();

        return mod;
    }

    public boolean resetTable(){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        try
        {
            db.execSQL("DROP TABLE "+ ModificheStrings.TBL_NAME+";");
            db.execSQL("CREATE TABLE "+ ModificheStrings.TBL_NAME+
                    " (" + ModificheStrings.FIELD_ID + " INTEGER PRIMARY KEY," +
                    ModificheStrings.FIELD_DATE +" NUMERIC NOT NULL," +
                    ModificheStrings.FIELD_ID_OGG_MOD +" INTEGER NOT NULL," +
                    ModificheStrings.FIELD_TBL +" TEXT NOT NULL," +
                    ModificheStrings.FIELD_TYPE +" TEXT NOT NULL);");
            return true;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }
}
