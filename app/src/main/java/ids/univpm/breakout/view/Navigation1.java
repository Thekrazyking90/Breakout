package ids.univpm.breakout.view;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import ids.univpm.breakout.R;
import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.Mappa.MappaManager;
import ids.univpm.breakout.model.database.Utente.UtenteManager;

public class Navigation1 extends AppCompatActivity {

    private TextView connection_status;
    private ImageView non_connesso;
    private ImageView connesso;

    private int backpress;


    // inserire check per la connessione al server --> sotto icona rossa oppure verde se connesso
    // far si che appaia la mappa del piano in cui mi trovo, in base al beacon a cui sono connesso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication.setCurrentActivity(this);
        setContentView(R.layout.navigation1);

        connection_status= findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        connesso= findViewById(R.id.connesso);

        Controller.checkConnection(Navigation1.this);

        if(MainApplication.getOnlineMode()) {
            non_connesso.setVisibility(View.INVISIBLE);
            connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Connected");
        }else{
            connesso.setVisibility(View.INVISIBLE);
            non_connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Disconnected");
        }

        MainApplication.start(this);

        //ottiene l'oggetto Intent dall'activity che lo richiama
        Intent intent = this.getIntent();

        if(MainApplication.getEmergency()) {
            MainApplication.initializeScanner(this,"EMERGENCY");
        }
        else {
            MainApplication.initializeScanner(this,"SEARCHING");
        }

        if(intent != null){

            Integer ID_Notifica= intent.getExtras().getInt("ID_Notifica");
            if (ID_Notifica != null) {
                //TODO far apparire il percorso di uscita calcolato (Dijkstra),
                // dopo aver preso la posizione dell'utente
            }

            // Distinguiamo da dove arriva la chiamata: se da Login, disegno la mappa
            // con i nodi presi dal db (i tronchi penso si vedano solo in caso di emergenza, ma il
            // codice con DrawLine lho lasciato per ora)

            String fromActivity = intent.getExtras().getString("ID_Activity");

            switch (fromActivity){
                case "From_Login":{
                    //get id mappa from intent
                    disegnoMappa();

                }
                // Se la chiamata arriva da SelPiano, si apre la schermata con la sola visualizzazione
                // della mappa zoomabile dei piani. Per 150 si possono lasciare anche i pallini dei nodi,
                // ma di 145 e 155 lascerei la visualizzazione così
                case "From_SelPiano":{
                    Integer idMappa = intent.getExtras().getInt("ID_Mappa");

                    MappaManager mappaManager = new MappaManager(Navigation1.this);
                    Mappa map = mappaManager.findByID(idMappa);

                    //Preparazione Bitmap
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inScaled = false;

                    Bitmap workingBitmap;

                    workingBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                            + "/Breakout/ImmaginiMappe/" + map.getImmagine());

                    Bitmap bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    SubsamplingScaleImageView imageView = findViewById(R.id.immagine);
                    imageView.setImage(ImageSource.bitmap(bitmap));

                }
            }

        }else{
            Log.d("ERROR: ","Riconoscimento intent non riuscito");
        }

    }



    //TODO da mettere in MainApplication
    public void disegnoMappa() {

        //Preparazione Bitmap
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;
        //TODO: deve prendere la mappa in base a che piano sta
        Bitmap workingbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.q150_color, opt);

        //Bitmap workingBitmap = Bitmap.createBitmap(chosenFrame);
        Bitmap bitmap = workingbitmap.copy(Bitmap.Config.ARGB_8888, true);


        //istanzio un Canvas: lo uso per disegnare su una Bitmap
        Canvas canvas = new Canvas(bitmap);


        //Per disegnare il tratto con cui il Canvas disegnerà --> Paint
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //usando il Paint disegno un cerchio nella Drawable

        //cordinate iniziali 0 0


        //TODO: le coordinate dei beacon le devo prendere dal db e fare un ciclo for per mettere
        // tutti i puntini =beacon , nel disegno --> TODO prendere dati dal file excel?
        // METTO ANCHE I NODI

        //scale vicino g1 --> coordinate ora copiate a mano dal file excel, ma vanno prese dal db
        int server_b1x = 218;
        int server_b1y = 129;

        //conversione nei pixel dell'immagine: scale vicino g1
        int b1x = server_b1x;
        int b1y = server_b1y;

        //esempio beacon CORRIDOIO biblio
        int b2x = 678;
        int b2y = 456;


        //PROVO A DISEGNARE ALTRI PUNTI NELLA MAPPA
        //esempio 150/2
        int b3x = 489;
        int b3y = 160;

        //esempio corridoio atelier
        int b4x = 633;
        int b4y = 156;

        //g1 aula
        int b5x = 280;
        int b5y = 242;
        // uscita em
        int b6x = (int) 544;
        int b6y = (int) 11;


        //TODO anche qui va messa l'iterazione per disegnare puntini e le linee

        canvas.drawCircle(b1x, b1y, 8, paint);
        canvas.drawCircle(b2x, b2y, 8, paint);
        canvas.drawCircle(b3x, b3y, 8, paint);
        canvas.drawCircle(b4x, b4y, 8, paint);
        canvas.drawCircle(b5x, b5y, 8, paint);
        canvas.drawCircle(b6x, b6y, 8, paint);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        //DISEGNO unA LINEA --> anche qui ci va un'ITERAZIONE PER DISEGNARE IL PERCORSO
        canvas.drawLine(b1x, b1y, b5x, b5y, paint);


        //TODO: in base a quale beacon sono collegato, faccio apparire il simbolo gps e ne disegno
        //        le coordinate con drawBitmap (sotto)


        Bitmap gps_pic = BitmapFactory.decodeResource(getResources(), R.drawable.gps, opt);
        Bitmap gps = Bitmap.createScaledBitmap(gps_pic, gps_pic.getWidth() / 20, gps_pic.getHeight() / 20, true);


        //metto il segnale gps in corrispondenza del primo beacon per ora
        int gps_x = b1x;
        int gps_y = b1y;

        canvas.drawBitmap(gps, gps_x - (gps.getWidth() / 2), gps_y - (gps.getHeight()), null);

        //le coordinate da mettere nel drawbitmap (riga sopra) sono da prendere in riferimento
        // al beacon a cui sono collegato --> così ho il simbolo gps inserito nella mappa


        //la Bitmap disegnata con il Canvas diventa il contenuto della ImageView
        SubsamplingScaleImageView imageView = findViewById(R.id.immagine);
        imageView.setImage(ImageSource.bitmap(bitmap));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        //Se non loggato, cambia titolo in login nel menu(!)
        UtenteManager utenteManager = new UtenteManager(Navigation1.this);

        MenuItem mLogButton = menu.findItem(R.id.log_status);

        if(utenteManager.AnyIsLoggato()){
            String username = utenteManager.findByIsLoggato().getUsername();
            mLogButton.setTitle("Logout " + username);
        }else{
            mLogButton.setTitle("Login");
        }

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

                UtenteManager utenteManager = new UtenteManager(Navigation1.this);

                Intent returnBtn;
                if(utenteManager.AnyIsLoggato()){
                    Utente user = utenteManager.findByIsLoggato();
                    utenteManager.updateIs_loggato(user ,false);

                    Server.logoutUtente();

                    returnBtn = new Intent(getApplicationContext(), Welcome.class);

                }else{
                    returnBtn = new Intent(getApplicationContext(), Login.class);
                }

                startActivity(returnBtn);
                break;

        }
        return false;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!MainApplication.getEmergency()) {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

            if (backpress>1) {
                MainApplication.getScanner().suspendScan();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), " C'è un'emergenza in corso ", Toast.LENGTH_SHORT).show();
        }
    }
}

