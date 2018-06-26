package ids.univpm.breakout.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import ids.univpm.breakout.R;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;

public class Navigation1 extends AppCompatActivity {

    Bitmap gps_pic, gps;
    int gps_x, gps_y;
    private TextView connection_status;
    private ImageView non_connesso;
    private ImageView connesso;

    // inserire check per la connessione al server --> sotto icona rossa oppure verde x connesso
    // far si che appaia la mappa del piano in cui mi trovo, in base al beacon a cui sono connesso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation1);

        MainApplication.start(this);

        connection_status= findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        connesso= findViewById(R.id.connesso);

        if(Controller.checkConnection(Navigation1.this)) {
            non_connesso.setVisibility(View.INVISIBLE);
            connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Connected");
        }else{
            connesso.setVisibility(View.INVISIBLE);
            non_connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Disconnected");
        }

//TODO caricamento mappa
        if(getIntent() != null){
            //get id mappa from intent
        }else{

        }

      //TODO: da inserire TUTTO nella parte di caricamento mappa
        //Prendere dimensioni pixel del display
        Display display = ((WindowManager)
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int displayWidth = display.getWidth();
        int displayHeight = display.getHeight();


        //Preparazione Bitmap
        //TODO: deve prendere la mappa in base a che piano sta
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.q150_color);
        int originalHeight= bitmap.getHeight();
        int originalWidth=bitmap.getWidth();

        //Adatto la larghezza facendo una proporzione: altezza display (scalata) sta a altezza
        // originale dell'immagine, come la larghezza scalata sta alla larghezza originale
        // cosi mantengo le proporzioni dell'immagine, quindi lascio display Height, ma per
        // la larghezza scalata ho --> altezzaDisplay*larghezza originale/altezza originale
        bitmap = Bitmap.createScaledBitmap(bitmap, displayHeight*originalWidth/originalHeight, displayHeight, true);


        //istanzio un Canvas: lo uso per disegnare su una Bitmap
        Canvas canvas = new Canvas(bitmap);

        //Per disegnare il tratto con cui il Canvas disegnerà --> Paint
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //usando il Paint disegno un cerchio nella Drawable

        //TODO: le coordinate dei beacon le devo prendere dal db e fare un ciclo for per mettere
        // i beacon li disegno con dei puntini nella mappa
        // le varie cx e cy sono le coordinate in PIXEL dei beacon
        int cx1=110;
        int cy1=200;
        int cx2=500;
        int cy2=300;
        canvas.drawCircle(cx1, cy1, 12, paint);
        canvas.drawCircle(cx2, cy2, 12, paint);

        //disegno le linee che collegano i beacon
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        canvas.drawLine(cx1,cy1,cx2,cy2, paint);


        //TODO: in base a quale beacon sono collegato, faccio apparire il simbolo gps
        //Qui lo inserisco nel primo punto, cioe cx1,cy1
        gps_pic= BitmapFactory.decodeResource(getResources(),R.drawable.gps);
        gps= Bitmap.createScaledBitmap(gps_pic,gps_pic.getWidth()/50,gps_pic.getHeight()/50,true);

        gps_x=cx1;
        gps_y=cy1;
        canvas.drawBitmap(gps,gps_x -(gps.getWidth()/2),gps_y-(gps.getHeight()),null);
        //Ho cosi inserito il simbolo gps nella mappa

        //la Bitmap disegnata con il Canvas diventa il contenuto della ImageView
        SubsamplingScaleImageView imageView = findViewById(R.id.immagine150);
        imageView.setImage(ImageSource.bitmap(bitmap));
        //fine del disegno su mappa

    }



















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        //Se non loggato, cambia titolo in login nel menu(!)

        MenuItem item = menu.findItem(R.id.ricerca);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                startActivity(new Intent(Navigation1.this, RicercaPDI.class));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.SelPiano:

                //Apertura nuova activity per scelta del piano
                startActivity(new Intent(Navigation1.this, SelPiano.class));

                break;

            case R.id.pdi:
                startActivity(new Intent(Navigation1.this, RicercaPDI.class));
                break;


            case R.id.log_status:

                //Inserire codice per LogOut se deve fare logout
                Intent returnBtn = new Intent(getApplicationContext(), Welcome.class);

                startActivity(returnBtn);
                break;

        }
        return false;
        //return super.onOptionsItemSelected(item);
    }
}

