package ids.univpm.breakout.communication.beacon;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.communication.StateMachine;
import ids.univpm.breakout.communication.message.MessageBuilder;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.Utente.UtenteManager;

/**
    Classe utilizzata per gestire la connessione con un beacon
 */

public class BeaconConnection extends StateMachine {

        //alcuni possibili messaggi che si possono ricevere durante
        //la connessione(vengono utilizzati come parametri per l'intenFilter)
    public static final String DATA_CHANGED = "dataChanged";
    public static final String ACKNOWLEDGE = "ACKNOWLEDGE";
    public static final String STOP_CONNECTION = "STOP CONNECTION";

        //indirizzi UUID riferiti alla temperatura (il primo indica il servizio, il secondo la configurazione del sensore
        //mentre il terzo i dati veri e proprio
    private static final String UUIDTemp = "f000aa00-0451-4000-b000-000000000000";
    private static final String UUIDTempConfig = "f000aa02-0451-4000-b000-000000000000";
    private static final String UUIDTempData = "f000aa01-0451-4000-b000-000000000000";

        //indirizzi UUID riferiti al barometro (il primo indica il servizio, il secondo la configurazione del sensore
        //mentre il terzo i dati veri e proprio
    private static final String UUIDBarometer = "f000aa40-0451-4000-b000-000000000000";
    private static final String UUIDBarometerConfig = "f000aa42-0451-4000-b000-000000000000";
    private static final String UUIDBarometerData = "f000aa41-0451-4000-b000-000000000000";

        //indirizzi UUID riferiti al sensore di movimento (il primo indica il servizio, il secondo la configurazione del sensore
        //mentre il terzo i dati veri e proprio
    private static final String UUIDMovement = "f000aa80-0451-4000-b000-000000000000";
    private static final String UUIDMovementConfig = "f000aa82-0451-4000-b000-000000000000";
    private static final String UUIDMovementData = "f000aa81-0451-4000-b000-000000000000";

        //indirizzi UUID riferiti al sensore di luce (il primo indica il servizio, il secondo la configurazione del sensore
        //mentre il terzo i dati veri e proprio
    private static final String UUIDLuxometer = "f000aa70-0451-4000-b000-000000000000";
    private static final String UUIDLuxometerConfig = "f000aa72-0451-4000-b000-000000000000";
    private static final String UUIDLuxometerData = "f000aa71-0451-4000-b000-000000000000";

    private static final String TAG = "MyBroadcastReceiver";

        //activity in cui viene creata la connessione
    private Activity activity;
        //filtro dei messaggi per il broadcast receiver
    private IntentFilter intentFilter;

        //contatore utilizzato per scandire i servizi del beacon
    private int cont;
        //timer utilizzato per sbloccare la macchina a stati, nel caso in cui la comunicazione
    private Handler timer;
        //durata in millisecondi del timer
    private static final long timerPeriod = 50000l;
        //flag che indica si dati sono stati letti o meno dal sensore
    private boolean readData;

        //identifica il servizio di cui si stanno leggendo i valori
    private BeaconService currentService;
        //identifica il dispositivo (beacon) con cui ci si è collegati
    private BluetoothDevice device;
        //flag che indica se è stata stabilita la connessione
    private boolean connectionStarted;

        //lista di servizi trovati dalla callback
    private ArrayList<BluetoothGattService> findServices;
        //lista di servizi ricercati
    private ArrayList<BeaconService> services;

    /**
     * Costruttore dell'oggetto BeaconConnection
     * @param a, activity in cui viene creata la connessione
     * @param d, dispositivo bluetooth a cui ci si deve collegare
     */
    public BeaconConnection(Activity a, BluetoothDevice d) {
        super();
        Log.i("CONNECTION","new connection");
        connectionStarted = false;
        device = d;
        cont = 0;
        readData = false;
        activity = a;
        timer = new Handler();
        timer.postDelayed(runnable,timerPeriod);
        initializeFilter();
        initializeServices();
        connectionStarted = true;
        findServices = new ArrayList<>();
        activity.getBaseContext().registerReceiver(broadcastReceiver,intentFilter);
    }

    /**
     * Metodo richiamato quando viene attivata la connessione (esegue lo stato 0 della macchina a stati)
     */
    public void start() {
        executeState();
    }


    protected int nextState() {
        int next = 0;
        Log.i("RUNNING","running " + running);
        switch(currentState) {
            case(0):
                    //caso in cui la connessione vada chiusa
                if (!running) next = 8;
                else
                    next = 1;
                break;
            case(1):
                    //caso in cui si debba chiudere la connessione o siano stati letti tutti i servizi
                if (!running || cont>=services.size())
                    next = 8;
                else
                    next = 2;
                break;
            case(2):
                    //caso in cui si debba chiudere la connessione
                if (!running) next = 6;
                else next = 3;
                break;
            case(3):
                    //caso in cui si debba chiudere la connessione
                if (!running) next = 5;
                else next = 4;
                break;
            case(4):
                next = 5;
                break;
            case(5):
                next = 6;
                break;
            case(6):
                    //caso in cui vadano letti i dati presi dal sensore
                if(readData) next = 7;
                else next = 8;
                break;
            case(7):
                    //caso in cui si debba chiudere la connessione o siano stati letti tutti i servizi
                if (!running || cont>=services.size()) next = 8;
                else next = 1;
                break;

        }
        return next;
    }


    protected void executeState() {
        Log.i("State","execute connection state " + getState());
        switch(currentState) {
            case(0):
                    //viene effettuata la connessione al sensore
                GattLeService.execute(device, activity.getBaseContext());
                break;
            case(1):
                readData = false;
                    //vengono letti i servizi disponibili
                findServices = GattLeService.getServices();
                    //ci si collega al servizio
                searchService();
                break;
            case(2):
                    //attivato il sensore riferito al servizio da leggere
                GattLeService.changeStateSensor(GattLeService.getmBluetoothGatt(), currentService, true);
                break;
            case(3):
                    //scritta maschera di bit in modo che il sensore invii dati tramite il bluetooth
                GattLeService.changeNotificationState(GattLeService.getmBluetoothGatt(), currentService, true);
                break;
            case(4):
                    //letti i dati dal sensore
                GattLeService.initializeData();
                readData = true;
                break;
            case(5):
                GattLeService.setSampleFlag(false);
                    //disattivato il sensore riferito al servizio da leggere
                GattLeService.changeNotificationState(GattLeService.getmBluetoothGatt(), currentService, false);
                break;
            case(6):
                    //scritta maschera di bit in modo che il sensore smetta di inviare dati tramite il bluetooth
                GattLeService.changeStateSensor(GattLeService.getmBluetoothGatt(), currentService, false);
                break;
            case(7):
                    //analizzati i dati letti dal sensore
                GattLeService.analyzeData();
                break;
            case(8):
                    //impacchettato il messaggio ed eventualmente viene inviato al server
                if(MainApplication.getOnlineMode()) {
                    String mex = packingMessage();
                    try {
                        Server.sendValue(mex);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                timer.removeCallbacks(runnable);
                    //chiusa la connessione e cancellata la registrazione del broadcast receiver
                close();
                GattLeService.closeConnection();
                activity.getBaseContext().sendBroadcast(new Intent("ScanPhaseFinished"));
                break;

        }
    }

    /**
     * Metodo per gestire la ricezione dei messaggi
     * @param intent, indica il messaggio ricevuto
     */
    private void messageHandler(Intent intent) {
        switch (intent.getAction()) {
                //messaggio ricevuto in condizioni normali, inviato quando uno stato della macchina termina le istruzioni da eseguire
            case(ACKNOWLEDGE):
                if (GattLeService.getmConnectionState()==0) running = false;
                    //viene calcolato il nuovo stato e vengono eseguite le istruzioni
                int next = nextState();
                changeState(next);
                executeState();
                break;
                    //messaggio ricevuto quando va stoppata la connessione per il sopraggiungere di un evento esterno
            case(STOP_CONNECTION):
                running = false;
                break;
                    //messaggio ricevuto quando sono stati letti un numero sufficente di dati da un sensore
            case (DATA_CHANGED):
                Log.i(TAG,"data changed");
                    //viene estratto il contenuto del messaggio
                Bundle b = intent.getExtras();
                    //vengono salvati i dati estratti dal messaggio (nel caso dell'accelerometro tre componenti)
                if (services.get(cont).getName().equals("movement")) {
                    double[] v = b.getDoubleArray("data");
                    services.get(cont).setValue(v);
                }
                    //viene salvato il dato estratti dal messaggio (per tutti gli altri sensori unico valore)
                else {
                    double v = b.getDouble("data");
                    services.get(cont).setValue(v);
                }
                    //aggiorna il timestamp
                services.get(cont).setLastSampleTime(new Timestamp(System.currentTimeMillis()));

                services.get(cont).printValue();
                    //aumentato il contatore per scorrere tutti i servizi da analizzare
                cont++;
                    //viene calcolato il nuovo stato e vengono eseguite le istruzioni
                next = nextState();
                changeState(next);
                executeState();

                break;
        }
    }

    /**
     * Metodo per impacchettare il messaggio da inviare al server
     */
    private String packingMessage() {
        UtenteManager utenteMng = new UtenteManager(MainApplication.getCurrentActivity().getBaseContext());
        Utente user = new Utente();
        if (utenteMng.AnyIsLoggato()) user = utenteMng.findByIsLoggato();

        String mex;
            //arraylist delle chiavi per creare il JSON
        ArrayList<String> keys = new ArrayList<>();
            //arraylist dei valori per creare il JSON
        ArrayList<String> values = new ArrayList<>();

            //create le chiavi per il documento
        keys.add("beacon_address");
        keys.add("user_ID");
        keys.add("timestamp");
        keys.add("temperature");
        keys.add("luxometer");
        keys.add("barometer");
        keys.add("accx");
        keys.add("accy");
        keys.add("accz");

            //aggiunti i valori al documento riferiti ai metadati (beacon selezionato, orario e utente)
        values.add(device.getAddress());
        values.add(user.getID_utente().toString());
        values.add(new Timestamp(System.currentTimeMillis()).toString());

        int c = 0;
            //aggiunti i valori al documento riferiti ai dati estratti dai sensori
        for (int i=0;i<services.size(); i++) {
            for (int j=0; j<services.get(i).getValue().size(); j++) {
                values.add(""+services.get(i).getValue().get(j));
                c++;
            }
        }
            //qualora alcuni valori non siano stati letti, riempe value con "null"
        while(c<6) {
            values.add("null");
            c++;
        }

            //creato il documento json
        mex = MessageBuilder.builder(keys,values,keys.size(),0);
        return mex;
    }
        //broadcast receiver per lo scambio di messaggi
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"ricevuto broadcast: " + intent.getAction());
            messageHandler(intent);
        }
    };

    /**
     * Metodo per impostare i filtri sui messaggi ricevuti
     */
    private void initializeFilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACKNOWLEDGE);
        intentFilter.addAction(DATA_CHANGED);
        intentFilter.addAction(STOP_CONNECTION);
    }

    /**
     * Metodo per selezionare i servizi desiderati
     */
    private void initializeServices() {
        services = new ArrayList<>();
        services.add(new BeaconService("temperature",UUIDTemp,UUIDTempData,UUIDTempConfig));
        services.add(new BeaconService("luxometer",UUIDLuxometer,UUIDLuxometerData,UUIDLuxometerConfig));
        services.add(new BeaconService("barometer",UUIDBarometer,UUIDBarometerData,UUIDBarometerConfig));
        services.add(new BeaconService("movement",UUIDMovement,UUIDMovementData,UUIDMovementConfig));

    }

    /**
     * Metodo per ricercare un servizio da analizzare,
     * fra quelli contenuti nell'arraylist di servizi di interesse per l'applicazione
     */

    private void searchService() {
        boolean b = false;
        int i = 0;
            //aggiorna il servizio da ricercare
        currentService = services.get(cont);
            //controllo sul contatore, per verificare che ancora ci siano servizi non analizzati
        if (cont<services.size()) {
                //fra la lista dei servizi cerca currentService
            while(i<findServices.size() && b==false) {
                    //controlla che il currentService sia fra quelli disponibili
                if (findServices.get(i).getUuid().equals(currentService.getService())) {
                    b = true;
                    Log.i(TAG,"service found");
                }
                i++;
            }
        }

            //viene calcolato il nuovo stato e vengono eseguite le istruzioni
        int next = nextState();
        changeState(next);
        executeState();

    }

    /**
     * Metodo utilizzato quando termina la connessione con il sensore,
     * viene cancellata la registrazione del receiver
     */
    public void close() {
        if (connectionStarted) {
            connectionStarted = false;
                //viene cancellata la registrazione del receiver
            activity.getBaseContext().unregisterReceiver(broadcastReceiver);
        }
    }
        //thread eseguito nel caso in cui il timer arrivi alla fine e si debba uscire dalla connessione
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e("error","si è verificato un problema");
            running = false;
            int next = nextState();
            changeState(next);
            executeState();
        }
    };

}
