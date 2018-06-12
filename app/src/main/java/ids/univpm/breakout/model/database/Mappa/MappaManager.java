package ids.univpm.breakout.model.database.Mappa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.database.DBHelper;

public class MappaManager {

    private Context context;
        private DBHelper dbHelper;

        public MappaManager(Context ctx)
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

        public Mappa findByID (long id){
            Cursor crs=null;
            Mappa mappa = new Mappa();
            String[] args = new String[] {Long.toString(id)};
            try
            {
                SQLiteDatabase db= dbHelper.getReadableDatabase();
                crs=db.query(MappaStrings.TBL_NAME, null, "id_mappa = ?", args, null, null, null, null);
            }
            catch(SQLiteException sqle)
            {
                return null;
            }

            crs.moveToFirst();
            mappa.setID_mappa(id);
            mappa.setID_piano(crs.getLong(crs.getColumnIndex(MappaStrings.FIELD_ID_PIANO)));
            mappa.setNome(crs.getString(crs.getColumnIndex(MappaStrings.FIELD_NAME)));
            mappa.setUrlImmagine(crs.getString(crs.getColumnIndex(MappaStrings.FIELD_IMG)));

            return mappa;
        }

    public ArrayList<Mappa> findAll() {
        ArrayList<Mappa> listaMappe = new ArrayList<>();
        Cursor crs = query();
        for(crs.moveToFirst(); !crs.isAfterLast(); crs.moveToNext()) {
            Mappa mappa = findByID(crs.getLong(crs.getColumnIndex(MappaStrings.FIELD_ID)));
            listaMappe.add(mappa);
        }
        return listaMappe;
    }
}
