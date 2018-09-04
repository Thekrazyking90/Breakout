package ids.univpm.breakout.view;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ids.univpm.breakout.R;
import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.communication.message.MessageBuilder;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.Nodo;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.Scala;
import ids.univpm.breakout.model.Tronco;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;
import ids.univpm.breakout.model.database.Mappa.MappaManager;
import ids.univpm.breakout.model.database.Nodi.NodoManager;
import ids.univpm.breakout.model.database.Tronchi.TroncoManager;
import ids.univpm.breakout.model.database.Utente.UtenteManager;
import ids.univpm.breakout.utility.CamminoMinimo;
import ids.univpm.breakout.utility.Percorso;

public class Navigation1 extends AppCompatActivity {

    private TextView connection_status;
    private ImageView non_connesso;
    private ImageView connesso;

    private int backpress;

    public static Bitmap bitmap;

    public static Integer idSelectedPdi;

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

        if(intent.hasExtra("ID_Activity") || intent.hasExtra("ID_Notifica")){

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
                case "From_Welcome" : {
                    String idMappaString = intent.getExtras().getString("ID_Mappa");
                    if(idMappaString.equals("null")){
                        startActivity(new Intent(Navigation1.this, SelPiano.class));
                    }else {
                        Integer idMappa = Integer.parseInt(idMappaString);
                        MappaManager mappaManager = new MappaManager(Navigation1.this);
                        Mappa mappa = mappaManager.findByID(idMappa);
                        setTitle("Mappa : " + mappa.getNome());
                        bitmap = disegnoMappa(idMappa);
                    }
                    break;
                }
                case "From_Login":{
                    String idMappaString = intent.getExtras().getString("ID_Mappa");
                    if(idMappaString.equals("null" )){
                        startActivity(new Intent(Navigation1.this, SelPiano.class));
                    }else {
                        Integer idMappa = Integer.parseInt(idMappaString);
                        MappaManager mappaManager = new MappaManager(Navigation1.this);
                        Mappa mappa = mappaManager.findByID(idMappa);
                        setTitle("Mappa : " + mappa.getNome());
                        bitmap = disegnoMappa(idMappa);
                    }
                    break;
                    //get id mappa from intent

                }
                // Se la chiamata arriva da SelPiano, si apre la schermata con la sola visualizzazione
                // della mappa zoomabile dei piani. Per 150 si possono lasciare anche i pallini dei nodi,
                // ma di 145 e 155 lascerei la visualizzazione così
                case "From_SelPiano":{
                    Integer idMappa = intent.getExtras().getInt("ID_Mappa");
                    MappaManager mappaManager = new MappaManager(Navigation1.this);
                    Mappa mappa = mappaManager.findByID(idMappa);
                    setTitle("Mappa : " + mappa.getNome());
                    bitmap = disegnoMappa(idMappa);
                    break;
                }
                case "From_Registra":{
                    String idMappaString = intent.getExtras().getString("ID_Mappa");
                    if(idMappaString.equals("null")){
                        startActivity(new Intent(Navigation1.this, SelPiano.class));
                    }else {
                        Integer idMappa = Integer.parseInt(idMappaString);
                        MappaManager mappaManager = new MappaManager(Navigation1.this);
                        Mappa mappa = mappaManager.findByID(idMappa);
                        setTitle("Mappa : " + mappa.getNome());
                        bitmap = disegnoMappa(idMappa);
                    }

                    break;
                }
                case "From_RicercaPDI": {
                    Integer idMappa = intent.getExtras().getInt("ID_Mappa");
                    Integer idPDISelezionato = intent.getExtras().getInt("ID_PDI");

                    idSelectedPdi = idPDISelezionato;

                    if (Controller.getPosizioneCorrente(Navigation1.this) != null) {
                        BeaconManager beaconManager = new BeaconManager(Navigation1.this);
                        Beacon beacon = beaconManager.findById(Controller.getPosizioneCorrente(Navigation1.this));

                        if (beacon.getID_pdi() == null) {
                            TroncoManager troncoManager = new TroncoManager(Navigation1.this);
                            Scala tronco = troncoManager.findByIdBeacon(beacon.getID_beacon());

                            CamminoMinimo camminoMinimo = new CamminoMinimo(Navigation1.this);
                            ArrayList<Integer> percorso = camminoMinimo.Dijkstra_Tronco(idPDISelezionato, tronco.getID());

                            Percorso.setGestionePercorso(true);
                            Percorso.cammino = percorso;
                            MappaManager mappaManager = new MappaManager(Navigation1.this);
                            Mappa mappa = mappaManager.findByID(idMappa);
                            setTitle("Mappa : " + mappa.getNome());
                            bitmap = disegnoMappa(idMappa);
                        } else {
                            CamminoMinimo camminoMinimo = new CamminoMinimo(Navigation1.this);
                            ArrayList<Integer> percorso = camminoMinimo.Dijkstra_Tronco(idPDISelezionato, beacon.getID_pdi());

                            Percorso.setGestionePercorso(true);
                            Percorso.cammino = percorso;
                            MappaManager mappaManager = new MappaManager(Navigation1.this);
                            Mappa mappa = mappaManager.findByID(idMappa);
                            setTitle("Mappa : " + mappa.getNome());
                            bitmap = disegnoMappa(idMappa);
                        }

                    } else {
                        MappaManager mappaManager = new MappaManager(Navigation1.this);
                        Mappa mappa = mappaManager.findByID(idMappa);
                        setTitle("Mappa : " + mappa.getNome());
                        bitmap = disegnoMappa(idMappa);
                    }
                    break;
                }
            }

        }else{
            Log.d("ERROR: ","Riconoscimento intent non riuscito");
        }

        //la Bitmap disegnata con il Canvas diventa il contenuto della ImageView
        SubsamplingScaleImageView imageView = findViewById(R.id.immagine);
        imageView.setImage(ImageSource.bitmap(bitmap));

    }

    public static void aggiornaMappa(Integer idMappa){
        Activity activity = MainApplication.getCurrentActivity();

        if(activity.getTitle().toString().contains("Mappa")){
            MappaManager mappaManager = new MappaManager(activity.getApplicationContext());
            Mappa mappa = mappaManager.findByID(idMappa);
            activity.setTitle("Mappa : " + mappa.getNome());

            SubsamplingScaleImageView imageView = activity.findViewById(R.id.immagine);
            imageView.setImage(ImageSource.bitmap(bitmap));
        }
    }

    public static Bitmap disegnoMappa(Integer idMap) {
        Context ctx = MainApplication.getCurrentActivity().getApplicationContext();
        MappaManager mappaManager = new MappaManager(ctx);
        Mappa mappa = mappaManager.findByID(idMap);



        //Preparazione Bitmap
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;

        Bitmap workingBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                + "/Breakout/ImmaginiMappe/" + mappa.getImmagine());

        //Bitmap workingBitmap = Bitmap.createBitmap(chosenFrame);
        Bitmap bitmapDisegnata = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


        //istanzio un Canvas: lo uso per disegnare su una Bitmap
        Canvas canvas = new Canvas(bitmapDisegnata);


        Paint paintBeacon = new Paint();
        paintBeacon.setColor(Color.RED);

        Paint paintNodi = new Paint();
        paintNodi.setColor(Color.BLUE);
        //usando il Paint disegno un cerchio nella Drawable

        BeaconManager beaconManager = new BeaconManager(ctx);
        ArrayList<Beacon> listaBeacon = beaconManager.findAllByIdMap(idMap);
        ArrayList<Integer> listaIDBeacon = new ArrayList<>();
        int coordx;
        int coordy;

        for (Beacon beacon: listaBeacon) {
            listaIDBeacon.add(beacon.getID_beacon());
            coordx = (int) beacon.getCoord_X() * 115/18;
            coordy = (int) beacon.getCoord_Y() * 115/18;

            canvas.drawCircle(coordx, coordy, 8, paintBeacon);
        }

        NodoManager nodoManager = new NodoManager(ctx);
        ArrayList<Nodo> listaNodi = nodoManager.findAllByIdMap(idMap);

        for (Nodo nodo: listaNodi) {
            coordx = (int) nodo.getCoord_X() * 115/18;
            coordy = (int) nodo.getCoord_Y() * 115/18;

            canvas.drawCircle(coordx, coordy, 8, paintNodi);
        }


        if(Percorso.cammino != null && !Percorso.cammino.isEmpty() && Percorso.isGestionePercorso()) {
            //DISEGNO PERCORSO
            Paint paintPercorso = new Paint();
            paintPercorso.setColor(Color.GREEN);
            paintPercorso.setStrokeWidth(5);

            ArrayList<Tronco> listaTronchi;
            TroncoManager troncoManager = new TroncoManager(ctx);

            listaTronchi = troncoManager.findByIdMapAndRoute(idMap, Percorso.cammino);

            for (Tronco tronco : listaTronchi) {
                Nodo nodo1 = nodoManager.findById(tronco.getNodi_Integer()[0]);
                Nodo nodo2 = nodoManager.findById(tronco.getNodi_Integer()[1]);

                canvas.drawLine(nodo1.getCoord_X() * 115/18,
                        nodo1.getCoord_Y() * 115/18,
                        nodo2.getCoord_X() * 115/18,
                        nodo2.getCoord_Y() * 115/18,
                        paintPercorso);
            }
        }

        Integer idBeaconPosizione = Controller.getPosizioneCorrente(ctx);
        Beacon beaconPosizione = new Beacon();
        if(idBeaconPosizione!=null) {
            beaconPosizione = beaconManager.findById(idBeaconPosizione);
        }
        //In base a quale beacon sono collegato, faccio apparire il simbolo gps
        if(Controller.getPosizioneCorrente(ctx) != null && listaIDBeacon.contains(beaconPosizione.getID_beacon())) {

            Bitmap gps_pic = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.gps, opt);
            Bitmap gps = Bitmap.createScaledBitmap(gps_pic, gps_pic.getWidth() / 26, gps_pic.getHeight() / 26, true);

            int gps_x = (int) beaconPosizione.getCoord_X() * 115/18;
            int gps_y = (int) beaconPosizione.getCoord_Y() * 115/18;

            canvas.drawBitmap(gps, gps_x - (gps.getWidth() / 2), gps_y - (gps.getHeight()*2), null);
        }

        if(idSelectedPdi != null) {
            Pdi pdi = nodoManager.findPdiByID(idSelectedPdi);
            Bitmap flag_pic = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.flag, opt);
            Bitmap flag = Bitmap.createScaledBitmap(flag_pic, flag_pic.getWidth() / 18, flag_pic.getHeight() / 18, true);

            int flag_x = (int) pdi.getCoord_X() * 115/18;
            int flag_y = (int) pdi.getCoord_Y() * 115/18;

            canvas.drawBitmap(flag, flag_x, flag_y - (flag.getHeight() * 2), null);
        }


        return bitmapDisegnata;
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
                Intent intent = new Intent(Navigation1.this, RicercaPDI.class);
                intent.putExtra("value" , query);
                startActivity(intent);

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

                    Server.logoutUtente(user.getUsername());
                    idSelectedPdi = null;
                    Percorso.setGestionePercorso(false);
                    Percorso.cammino = new ArrayList<>();
                    returnBtn = new Intent(getApplicationContext(), Welcome.class);

                }else{
                    returnBtn = new Intent(getApplicationContext(), Login.class);
                }

                startActivity(returnBtn);
                break;

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (!MainApplication.getEmergency()) {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

            if (backpress>1) {
                MainApplication.getScanner().suspendScan();
                MainApplication.getEmergencyScanner().suspendScan();

                UtenteManager utenteManager = new UtenteManager(Navigation1.this);
                if(utenteManager.AnyIsLoggato()) {
                    Utente user = utenteManager.findByIsLoggato();
                    Server.logoutUtente(user.getUsername());
                }
                MainApplication.getCurrentActivity().moveTaskToBack(true);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), " C'è un'emergenza in corso ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Controller.sendNullPosition();
    }
}

