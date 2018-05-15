package ids.univpm.breakout.model.database.Beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ids.univpm.breakout.model.database.DBHelper;

public class BeaconManager {


        private DBHelper dbHelper;

        public BeaconManager(Context ctx)
        {
            dbHelper =new DBHelper(ctx);
        }

        public void save(long id, long pdi, float coordx, float coordy, float fire, float smoke, float los, float risk)
        {
            SQLiteDatabase db= dbHelper.getWritableDatabase();
            dbHelper.getWritableDatabase();

            ContentValues cv=new ContentValues();
            cv.put(BeaconStrings.FIELD_ID, id);
            cv.put(BeaconStrings.FIELD_ID_PDI, pdi);
            cv.put(BeaconStrings.FIELD_COORD_X, coordx);
            cv.put(BeaconStrings.FIELD_COORD_Y, coordy);
            cv.put(BeaconStrings.FIELD_FIRE, fire);
            cv.put(BeaconStrings.FIELD_SMOKE, smoke);
            cv.put(BeaconStrings.FIELD_LOS, los);
            cv.put(BeaconStrings.FIELD_RISK, risk);
            try
            {
                db.insert(BeaconStrings.TBL_NAME, null,cv);
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
                if (db.delete(BeaconStrings.TBL_NAME, BeaconStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
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
                crs=db.query(BeaconStrings.TBL_NAME, null, null, null, null, null, null, null);
            }
            catch(SQLiteException sqle)
            {
                return null;
            }
            return crs;
        }


}
