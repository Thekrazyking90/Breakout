package ids.univpm.breakout.controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ids.univpm.breakout.communication.emergenza.EmergenzaScanner;
import ids.univpm.breakout.communication.beacon.BeaconScanner;
import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.Piano;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;

/**
 * Questa classe gestisce alcuni elementi riguarda la logica dell'applicazione.
 *
 */

public class MainApplication {

        //struttura dati per identificare i piani di un edificio (K: nome piano, V: piano)
    private static HashMap<String,Piano> floors;
        //struttura dati per identificare i beacon di un edificio (K: id beacon, V: beacon)
    private static HashMap<String, Beacon> sensors;
        //flag che permette di capire se è presente o meno un'emergenza
    private static boolean emergency;
        //struttura utilizzata per interfacciarsi con il bluetooth
    private static BluetoothAdapter mBluetoothAdapter;
        //scanner per ricercare i dispositivi beacon
    private static BeaconScanner scanner;

    private static EmergenzaScanner emergencyScanner;

    public static EmergenzaScanner getEmergencyScanner() {
        return emergencyScanner;
    }

    //identifica la home
    private static Activity activity;

        //identifica l'activity corrente dell'app
    private static Activity currentActivity;

        //flag che indica se l'activity è visibile o meno (serve per vedere se l'app è in background o meno)
    private static boolean visible;

        //costante usata per attivare il bluetooth
    private static final int REQUEST_ENABLE_BT = 1;

        //filtro usato per discriminare quali messaggi deve ricevere MainApplication
    private static IntentFilter intentFilter;
        //identificativo del messaggio che si può ricevere
    public static final String TERMINATED_SCAN = "TerminatedScan";
        //modalità di funzionamento dell'applicazione (per gestire le comunicazioni col server)
    private static boolean onlineMode = true;
        //parametri per la durata dello scan (presi dal server)
    private static HashMap<String, Long> scanParameters; //TODO controllare nell'app vecchia quando e se viene usato il setScanParameters
        //flag che indica quando l'applicazione sta per essere chiusa (passando dal backbutton)
    private static boolean isFinishing;


    /**
     * Metodo che inizializza i parametri legati all'applicazione
     * @param a, activity in cui vengono inizializzati i parametri (activity home)
     */
    public static void start(Activity a) {
        activity = a;
        initializeFilter();
        emergency = false;
            //registrato il receiver
        //activity.getBaseContext().registerReceiver(broadcastReceiver,intentFilter);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            //attivazione del bluetooth (qualora non sia già funzionante)
        if(!controlBluetooth()) activateBluetooth();
            //creazione dello scanner
        if(mBluetoothAdapter!=null){
            initializeScanner(activity);
            initializeEmergencyScanner(activity);
        }
            //inizializzata la struttura dati legata all'utente
//TODO studiare e modificare        UserHandler.init();
            //creata struttura dati legata ai beacon nell'edificio, leggendo dal file salvato in memoria intera
        BeaconManager beaconMng = new BeaconManager(activity.getBaseContext());
        ArrayList<Beacon> beaconList = beaconMng.findAll();
        loadSensors(beaconList);

            //creata struttura dati legata alle stanze nell'edificio, leggendo dal file salvato in memoria intera
/*TODO in caso da fare leggendo i dati nel db locale       ArrayList<String[]> roomsList = CSVHandler.readCSV("roomlist",activity.getBaseContext());
        loadRooms(roomsList);
        if(application.MainApplication.getOnlineMode()){
            UserHandler.initializePosition();
        }*/

        isFinishing = false;
    }

    private static void initializeEmergencyScanner(Activity a) {
        emergencyScanner = new EmergenzaScanner(a);
    }

    /**
     * Metodo per impostare la modalità di funzionamento dell'applicazione
     * @param b, valore che si vuole assegnare al flag per la modalità di funzionamento
     */
    public static void setOnlineMode(boolean b) {
        onlineMode = b;
    }

    /**
     * Metodo che indica se il funzionamento dell'applicazione è in modalità online oppure offline
     * @return true se modalità online, false se offline
     */
    public static boolean getOnlineMode(){ return onlineMode;}
    /**
     * Metodo per impostare l'activity in cui si trova a lavorare l'applicazione
     * @param a, activity in cui si trova a lavorare l'applicazione
     */
    public static void setCurrentActivity (Activity a) {
        currentActivity = a;
    }

    /**
     * Metodo che restituisce l'activity in cui si trova l'applicazione in quel momento
     * @return activity in cui si trova a lavorare l'applicazione
     */
    public static Activity getCurrentActivity () {
        return currentActivity;
    }
    /**
     * Metodo per impostare la visibilità o meno dell'applicazione. Considerando che ad ogni cambio di activity
     * una viene chiusa (il flag diviene false) e l'altra viene aperta (il flag diviene true), quando si il metodo restituisce
     * un valore false si presuppone che l'applicazione stia lavorando in background
     * @param b, valore che si vuole assegnare al flag per la visibilità dell'activity
     */
    public static void setVisible(boolean b) {
        visible = b;
//        Log.i("visible",""+visible);
    }
    /**
     * Metodo che restituisce la visibilità dell'applicazione
     * @return visible, visibilità dell'applicazione
     */
    public static boolean getVisible() {
        return visible;
    }
    /**
     * Metodo che restituisce l'istanza contenente le durata per le varie fasi dello scan, ricevute dal server
     * @return scanParameters, hashmap (K: nome parametro, V:sua durata in millisecondi) contenente i valori per le varie fasi dello scan
     */
    public static HashMap<String,Long> getScanParameters() {
        return scanParameters;
    }

    /**
     * Metodo imposta l'attributo isFinishing, che indica se l'applicazione sta per essere chiusa
     * @param b, booleano che indica se l'applicazione sta per essere chiusa o meno
     */
    public static void setIsFinishing(boolean b) {
        isFinishing = b;
    }

    /**
     * Metodo che assegna l'istanza contenente le durate per le varie fasi dello scan, ricevute dal server
     * @param s, hashmap (K: nome parametro, V:sua durata in millisecondi) contenente i valori per le varie fasi dello scan
     */
    public static void setScanParameters(HashMap<String,Long> s) {
        scanParameters = s;
    }

    /**
     * Metodo che restituisce la struttura dati in cui sono memorizzati i beacon
     * @return sensors, struttura dati contenente i beacon
     */
    public static HashMap<String,Beacon> getSensors() {
        return sensors;
    }
    /**
     * Metodo che restituisce l'adapter necessario per la comunicazione bluetooth
     * @return mBluetoothAdapter, adapter per la comunicazione bluetooth
     */
    public static BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    /**
     * Metodo che restituisce la struttura dati in cui sono memorizzati i piani di un edificio
     * @return floors, struttura dati contenente i piani di un edificio
     */
    public static HashMap<String,Piano> getFloors(){
        return floors;
    }
    /**
     * Metodo che indica se sia presente o meno un'emergenza nell'edificio
     * @return emergency, presenza o meno di un'emergenza
     */
    public static boolean getEmergency() {
        return emergency;
    }
    /**
     * Metodo per impostare lo stato di emergenza dell'applicazione
     * @param e, flag per indicare la presenza di un'emergenza (true indica la presenza di un'emergenza, false l'assenza)
     */
    public static void setEmergency(boolean e) {
        emergency = e;
        Log.i("emergency","emergency: " + emergency);
        scanner.suspendScan();
    }

    /**
     * Metodo che restituisce l'oggetto utilizzato per lo scan dei sensori
     * @return scanner, scanner dei sensori
     */
    public static BeaconScanner getScanner() {
        return scanner;
    }
    /**
     * Metodo che restituisce l'attributo activity
     * @return activity, attributo activity
     */
    public static Activity getActivity() {
        return activity;
    }

    /**
     * Metodo utilizzato per inizializzare lo scanner, con il setup di default (NORMAL condition).
     * @param a, activity in cui viene creato lo scan
     */
    public static void initializeScanner(Activity a) {
        scanner = new BeaconScanner(a);
    }

    /**
     * Metodo utilizzato per inizializzare lo scanner, con il setup dato dal parametro cond.
     * @param a, activity in cui viene creato lo scan
     * @param cond, identificativo del tipo di setup con cui si vuole costruire lo scanner
     */
    public static void initializeScanner(Activity a, String cond) {
        scanner = new BeaconScanner(a,cond);
    }

    /**
     * Metodo per caricare nella struttura dati dei beacon, i valori passati come parametro
     * @param b, i valori dei sensori da caricare in memoria
     */
    private static void loadSensors(ArrayList<Beacon> b) {
        String cod;
        sensors = new HashMap<>();

        for(Beacon beacon : b) {
            cod = beacon.getAddress();
            sensors.put(cod, beacon);
        }

    }


    /*private static void loadRooms(ArrayList<String[]> b) {  TODO credo non serva questo metodo, però teniamolo e rivediamolo in caso dovesse servire

        int[] coords = new int[2];
        double width;
        String cod;
        HashMap<String,Piano> f = new HashMap<>();
        floors = f;
        for (String[] roomslist : b) {
            if(floors.containsKey(roomslist[2])) {    //il piano esiste
                coords[0] = Integer.parseInt(roomslist[0]); //x
                coords[1] = Integer.parseInt(roomslist[1]); //y
                width = Double.parseDouble(roomslist[3].replace(",","."));
                cod = roomslist[4];
                floors.get(roomslist[2]).addRoom(cod,new Room(cod,coords.clone(),roomslist[2],width));
            }else{
                floors.put(roomslist[2],new Floor(roomslist[2]));//aggiungo il nuovo piano
                //aggiunto il nodo
                coords[0] = Integer.parseInt(roomslist[0]); //x
                coords[1] = Integer.parseInt(roomslist[1]); //y
                width = Double.parseDouble(roomslist[3].replace(",","."));     //larghezza
                cod = roomslist[4];                         //codice
                floors.get(roomslist[2]).addRoom(cod,new Room(cod,coords.clone(),roomslist[2],width));
            }

        }

    }*/


    //TODO metodo per quando viene chiusa l'app -> da rivedere e modificare
    /*public static void closeApp(GetReceiver httpServerThread) {

//        if (scanner!=null) {
//            if (scanner.getConnection()!=null) scanner.getConnection().close();
//            scanner.closeScan();
//        }
//        activity.getBaseContext().unregisterReceiver(broadcastReceiver);
        scanner.suspendScan();



        if(application.MainApplication.getOnlineMode()) {
            if (httpServerThread.status()) {
                try {
                    httpServerThread.closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                ServerComunication.deletePosition(UserHandler.getIpAddress());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/

        //il broadcast receiver deputato alla ricezione dei messaggi
    /*private static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Log.i("MESSAGE ARRIVED","ricevuto broadcast: " + intent.getAction());
            //MainApplication può ricevere solo questo messaggio, che indica il fatto che lo scanner
            //ha terminato il proprio ciclo di funzionamento, se ne può quindi far partire un altro
        if(intent.getAction().equals("TerminatedScan")) {
            if (isFinishing) {
                if(broadcastReceiver!=null) activity.unregisterReceiver(broadcastReceiver);
                scanner.closeScan();
                scanner = null;
//              activity.getBaseContext().unregisterReceiver(broadcastReceiver);
                activity.finish();
            }
            else {
                //viene gestito il cambio di activity nel caso in cui ci sia un'emergenza
                if(emergency) {
                    //se lo scanner fino a quel momento ha lavorato in modalità normale
                    //significa che l'applicazione non si trovava in modalità SEARCHING
                    if(scanner.getSetup().getState().equals("NORMAL")) {
                        scanner.closeScan();
                        scanner = null;
                        UtenteManager utenteMng = new UtenteManager(MainApplication.getCurrentActivity().getBaseContext());
                        Utente user = new Utente();
                        if (utenteMng.AnyIsLoggato()) user = utenteMng.findByIsLoggato();
                        //presi i dati riferiti alla posizione per poter inizializzare l'activity Navigation1
                        //TODO da rivedere
                        //viene messo come obiettivo da raggiungere la via di fuga nel piano
                        String mex = "beacon_ID = " + user.getUltima_posizione().toString().concat(";").concat("EMERGENCY");
                        Log.i("mex",mex);
                        Intent intentTWO = new Intent(context,
                                Navigation1.class);
                        intentTWO.putExtra("MAP_ID",mex);
                        context.startActivity(intentTWO);

                    }
                    //se già ci si trova nella FullScreenMaps (la modalità di funzionamento è quindi
                    //SEARCHING oppure EMERGENCY
                    else {
                        //viene riinizializzato lo scan, in modalità emergenza
                        scanner.closeScan();
                        scanner = null;
                        scanner = new BeaconScanner(activity,"EMERGENCY");
                    }

                }
                //viene gestito il cambio di activity nel caso in cui non sia presente un'emergenza
                else {
                    //se lo scanner in quel momento si trova in modalità normale,
                    //significa che non è in corso una ricerca
                    if(scanner.getSetup().getState().equals("NORMAL")) {
                        scanner.closeScan();
                        scanner = null;
                        //viene inviato un messaggio per creare la FullScreenMap activity
                        context.sendBroadcast(new Intent("STARTMAPS"));

                    }
                    //se lo scan sta lavorando in modalità diversa da NORMAL, significa che si trova nella
                    //FullScreenMaps
                    else {
                        //viene inviato il messaggio per chiudere la FullScreenMaps
                        context.sendBroadcast(new Intent("EXIT_MAPS"));
                        scanner.closeScan();
                        scanner = null;
                        //riinizializzato lo scanner in modalità NORMAL
                        initializeScanner(activity);
                    }
                }
            }

        }
        }
    };*/

    /**
     * Metodo per costruire il filtro per i messaggi che può ricevere il broadcastReceiver
     */
    private static void initializeFilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(TERMINATED_SCAN);
    }


    /**
     * Metodo che controlla se il dispositivo su cui sta lavorando l'applicazione ha il bluetooth acceso
     */
    public static boolean controlBluetooth() {
        boolean b;
        if (getmBluetoothAdapter()==null || !getmBluetoothAdapter().isEnabled()) b = false;
        else b = true;
        return b;
    }

    /**
     * Metodo per lanciare una notifica push, qualora l'applicazione sia in background nel momento
     * in cui sopraggiunge un'emergenza
     */
    /*public static void launchNotification() {  TODO modificare questo metodo e vedere se spostarlo nel controller
            //activity creata al click della notifica
        Intent intent = new Intent(activity, Home.class);
        intent.putExtra("MESSAGE","EMERGENCY");

        PendingIntent pIntent = PendingIntent.getActivity(activity, (int) System.currentTimeMillis(), intent, 0);
            //creazione della notifica vera e propria
        Notification n  = new Notification.Builder(activity)
                .setContentTitle("Progetto Ingegneria")
                .setContentText("C'è un'emergenza")
                .setSmallIcon(R.drawable.danger)
                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.danger))
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.danger, "Open", pIntent).build();

        notificationManager =
                (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
            //lancio della notifica
        notificationManager.notify(0, n);
    }*/


    /**
     * Metodo all'interno del quale viene richiesta l'attivazione del bluetooth
     */
    public static void activateBluetooth () {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

}

