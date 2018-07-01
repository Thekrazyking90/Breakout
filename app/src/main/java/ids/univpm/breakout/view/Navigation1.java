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
        // tutti i puntini =beacon , nel disegno --> TODO prendere dati dal file excel?
        // METTO ANCHE I NODI
        // le varie b0x e b0y sono le coordinate in PIXEL dei beacon


        //scale vicino g1 --> coordinate x=98, y=58 ora copiate a mano dal file excel
        int db_b1x=98;
        int db_b1y=58;

        int b0x,b0y,b1x,b1y,b2x,b2y,b3x,b3y,b4x,b4y,b5x,b5y;

        //cordinate iniziali
        b0x=31;
        b0y=39;

        //conversione nei pixel dell'immagine
        b1x=(int) (db_b1x*2.7)+31;;
        b1y=(int) ((db_b1y)*3.7)-38;

        //esempio beacon CORRIDOIO biblio (TODO da prendere dal db)
        int db_b2x=305;
        int db_b2y=205;

        b2x=(int) (db_b2x*2.7)+31;
        b2y=(int) ((db_b2y)*3.3)-38;

        //esempio 150/2
        b3x= (int) (220*2.7)+31;
        b3y= (int) ((72)*3.3)-39;


        //esempio corridoio atelier
        b4x=(int) ((285)*2.7)+30;
        b4y=(int) ((70)*3.3)-38;

        //g1 aula
        b5x=(int) ((126)*2.7)+31;
        b5y=(int) ((109)*3.3)-38;


        //TODO anche qui va messa l'iterazione per disegnare puntini e le linee
        canvas.drawCircle(b0x, b0y, 12, paint);
        canvas.drawCircle(b1x, b1y, 12, paint);
        canvas.drawCircle(b2x, b2y, 12, paint);
        canvas.drawCircle(b3x, b3y, 12, paint);
        canvas.drawCircle(b4x, b4y, 12, paint);
        canvas.drawCircle(b5x, b5y, 12, paint);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        //DISEGNO LA LINEA che collega i beacon
        canvas.drawLine(b1x,b1y,b5x,b5y, paint);


        //TODO: in base a quale beacon sono collegato, faccio apparire il simbolo gps e ne disegno
        //le coordinate con drawBitmap (sotto)

        gps_pic= BitmapFactory.decodeResource(getResources(),R.drawable.gps);
        gps= Bitmap.createScaledBitmap(gps_pic,gps_pic.getWidth()/50,gps_pic.getHeight()/50,true);

        //metto il segnale gps in corrispondenza del primo beacon
        gps_x=b1x;
        gps_y=b1y;


        canvas.drawBitmap(gps,gps_x -(gps.getWidth()/2),gps_y-(gps.getHeight()),null);

        //Ho cosi inserito il simbolo gps nella mappa


        //la Bitmap disegnata con il Canvas diventa il contenuto della ImageView
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)findViewById(R.id.immagine150);
        imageView.setImage(ImageSource.bitmap(bitmap));


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

