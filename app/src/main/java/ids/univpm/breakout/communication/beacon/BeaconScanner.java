package ids.univpm.breakout.communication.beacon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ids.univpm.breakout.communication.DataListener;
import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.communication.StateMachine;
import ids.univpm.breakout.communication.message.MessageBuilder;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;
import ids.univpm.breakout.model.database.Utente.UtenteManager;
import ids.univpm.breakout.utility.Percorso;


/**
 Classe utilizzata per gestire lo scan dei beacon in prossimità dell'utente
 */

public class BeaconScanner extends StateMachine implements DataListener {

    //contiene le caratteristiche legate allo scan, in riferimento alla configurazione settata
    private SetupB setup;

    //alcuni possibili messaggi che può ricevere lo scan (vengono utilizzati come parametri per l'intenFilter)
    public static final String SCAN_PHASE_FINISHED = "ScanPhaseFinished";
    public static final String SUSPEND_SCAN = "SuspendScan";
    public static final String EMERGENCY = "EMERGENCY";

    private static final String TAG = "BeaconRESPONSE";

    //adapter per interpretare ciò che viene trovato nello scan
    private LeDeviceListAdapter mLeDeviceListAdapter;
    //activity in cui viene fatto partire lo scan, in modo da poterne ricavare il context
    private Activity activity;

    //rappresenta il beacon più vicino, con cui si deve effettuare il collegamento
    private BluetoothDevice currentBeacon;
    //rappresenta il beacon trovato dallo scan
    private BluetoothDevice selectedBeacon;

    //uuid dei sensortag utilizzati
    private static final String beaconUUID = "0000aa80-0000-1000-8000-00805f9b34fb";

    //maschera di UUID, serve per filtrare i dispositivi bluetooth da analizzare
    private UUID[] uuids;
    //handler utilizzato per lanciare le varie Runnable (start,stop,wait)
    private Handler scanHandler;
    //filtro dei messaggi per il broadcast receiver
    private IntentFilter intentFilter;
    //connessione con un determinato sensortag, per leggere i dati dai sensori
    private BeaconConnection connection;

    //elementi necessari per effettuare lo scan
    private ScanFilter scanFilter;
    private ScanSettings scanSettings;
    private List<ScanFilter> scanFilters;
    //numero massimo di scan senza che venga mandata la posizione al server
    private static final int maxNoUpdate = 5;
    //conta quante volte consecutive non si invia la propria posizione al server
    private int cont;

    /**
     * Costruisce l'oggetto per lo scan (con il setup di default)
     * @param a, activity in cui viene creato lo scan
     */
    public BeaconScanner(Activity a) {
        super();

        activity = a;
        //inizializza il contenitore
        setup = new SetupB();
        //mette in funzione la state machine
        running = true;
        //inizializzati i componenti del bluetooth
        mLeDeviceListAdapter = new LeDeviceListAdapter();
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//        if(!controlBluetooth()) activateBluetooth(a);

        cont = 0;

        //insieme di UUID riconosciuti dallo scan e relativa inizializzazione
        uuids = new UUID[1];
        uuids[0] = UUID.fromString(beaconUUID);
        //inizializzati gli elementi per lo scan
        scanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(beaconUUID))).build();

        scanFilters = new ArrayList<>();
        scanFilters.add(scanFilter);
        scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();

        //inizializzazione del filtro per i messaggi e registrazione del broadcast receiver
        initializeFilter();
        //viene registrato il receiver, in modo che possa ricevere messaggi e possa leggere
        //quelli la cui intestazione si trova nell'intentFilter
        activity.getBaseContext().registerReceiver(broadcastReceiver,intentFilter);
        //viene inizializzato l'handler
        scanHandler = new Handler();
        //viene eseguito lo scan corrente
        executeState();
    }

    /**
     * Costruisce l'oggetto per lo scan
     * @param a, activity in cui viene creato lo scan
     * @param set, indica l'identificativo del Setup con cui viene costruito lo scan
     */
    public BeaconScanner(Activity a, String set) {
        super();
        activity = a;
        //inizializza il contenitore
        setup = new SetupB(set);
        //mette in funzione la state machine
        running = true;
        //inizializzati i componenti del bluetooth
        mLeDeviceListAdapter = new LeDeviceListAdapter();
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //viene controllato che il bluetooth sia accesso, nel caso in cui ciò non sia vero
        //viene mostrata l'opzione per attivarlo
//        if(!controlBluetooth()) activateBluetooth(a);

        cont = 0;

        //insieme di UUID riconosciuti dallo scan e relativa inizializzazione
        uuids = new UUID[1];
        uuids[0] = UUID.fromString(beaconUUID);

        scanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(beaconUUID))).build();

        scanFilters = new ArrayList<>();
        scanFilters.add(scanFilter);
        scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();

        //inizializzazione del filtro per i messaggi e registrazione del broadcast receiver
        initializeFilter();
        //viene registrato il receiver, in modo che possa ricevere messaggi e possa leggere
        //quelli la cui intestazione si trova nell'intentFilter
        activity.getBaseContext().registerReceiver(broadcastReceiver,intentFilter);
        //viene inizializzato l'handler
        scanHandler = new Handler();
        //viene eseguito lo scan corrente
        executeState();
    }

    /**
     * Metodo per costruire il filtro per i messaggi che può ricevere il broadcastReceiver
     */
    //inseriti i filtri per i messaggi ricevuti
    private void initializeFilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(SCAN_PHASE_FINISHED);
        intentFilter.addAction(SUSPEND_SCAN);
        intentFilter.addAction(EMERGENCY);
    }

    /**
     * Metodo per gestire la ricezione dei messaggi
     * @param intent, indica il messaggio ricevuto
     */
    private void messageHandler(Intent intent) {
        switch (intent.getAction()) {
            //ricevuto quando è terminata la connessione al beacon
            case (SCAN_PHASE_FINISHED):
                connection = null;
                //finita l'esecuzione dello stato richiama
                int next = nextState();
                changeState(next);
                executeState();
                break;
            //ricevuto quando deve essere sospeso lo scan
            case (SUSPEND_SCAN):
                suspendScan();
                break;
            default:
                break;
        }
    }
    /**
     * Metodo che gestisce le istruzioni relative alla sospesione dello scan, diverse in base allo stato in cui si trova
     * la macchina a stati
     */

    public void suspendScan() {
        Log.i("SUSPENDSCAN","suspend scan");
        running = false;

        //nel caso in cui si stiano leggendo i dati dai sensori, viene interrotta la procedura
        //fermando la macchina a stati che se ne occupa
        if(currentState==1) {
            activity.getBaseContext().sendBroadcast(new Intent(BeaconConnection.STOP_CONNECTION));
        }
        //se si sta aspettando per un nuovo scan, viene abortito il processo di attesa
        //e si passa allo stato successivo (gestione della chiusura dello scan)
        else if(currentState==2) {
            scanHandler.removeCallbacks(wait);
            int next = nextState();
            changeState(next);
            executeState();
        }
    }

    //il broadcast receiver deputato alla ricezione dei messaggi
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"ricevuto broadcast: " + intent.getAction());
            messageHandler(intent);
        }
    };

    /**
     * Metodo che restituisce il beacon più vicino, dall'ultimo scan
     * @return beacon più vicino rispetto all'utente
     */
    public BluetoothDevice getSelectedBeacon() {
        return selectedBeacon;
    }

    /**
     * Metodo che restituisce l'ultimo beacon rilevato
     * @return ultimo beacon con cui ci si è collegati
     */
    public BluetoothDevice getCurrentBeacon() {
        return currentBeacon;
    }

    /**
     * Metodo che restituisce il setup con cui sta lavorando lo scan
     * @return setup con cui sta lavorando lo scan
     */

    public BeaconConnection getConnection() { return connection; }

    public SetupB getSetup() {
        return setup;
    }

    /**
     * Metodo per iniziare lo scan che ricerca i sensortag presenti nel raggio d'azione dell'utente
     */

    private void discoverBLEDevices() {
        //parte il thread deputato allo scan dei bluetooth LE
        startScan.run();

        Log.e("BLE_Scanner", "DiscoverBLE, in " + setup.getState() + " condition");
    }


    protected int nextState() {
        int next;
        switch(currentState) {
            case(0):
                if (!running) next = 3;
                else if (running && mLeDeviceListAdapter.getCurrentBeacon()!=null && setup.  mustAnalyze()) next = 1;
                else next = 2;
                break;
            case(1):
                if (!running) next = 3;
                else next = 2;
                break;
            case(2):
                if (!running) next = 3;
                else next = 0;
                break;
            default:
                next = 3;
                break;
        }
        return next;
    }

    /**
     * Metodo che costruisce il messaggio contenente la posizione dell'utente da inviare al server
     */
    public String packingMessage() {
        UtenteManager utenteMng = new UtenteManager(MainApplication.getCurrentActivity().getBaseContext());
        BeaconManager beaconMng = new BeaconManager(MainApplication.getCurrentActivity().getBaseContext());
        Utente user = new Utente();
        Beacon beacon = beaconMng.findByAddress(currentBeacon.getAddress());
        if (utenteMng.AnyIsLoggato()) user = utenteMng.findByIsLoggato();

        String mex;
        //arraylist delle chiavi per creare il JSON
        ArrayList<String> keys = new ArrayList<>();
        //arraylist dei valori per creare il JSON
        ArrayList<String> values = new ArrayList<>();
        //create le chiavi per il documento
        keys.add("ID_beacon");
        keys.add("username");

        //aggiunti i valori al documento riferiti ai metadati (beacon selezionato, indirizzo ip)
        values.add(beacon.getID_beacon().toString());
        values.add(user.getUsername());


        mex = MessageBuilder.builder(keys,values,keys.size(),0);
        Log.i("mex",mex);

        return mex;
    }

    /**
     * Metodo per attendere il tempo necessario fra due scan consecutivi
     *
     */
    private void waiting() {
        Log.i("WAITING","waiting for " + setup.getPeriodBetweenScan()/1000 + " seconds for new Scan ");
        scanHandler.postDelayed(wait,setup.getPeriodBetweenScan());
    }


    protected void executeState() {
        Log.i("State","execute scan state " + getState());
        switch(currentState) {
            //in questo stato viene effettuato lo scan dei beacon
            case(0):
                discoverBLEDevices();
                break;
            //in questo stato viene effettuata la connessione al beacon
            case(1):
                connection = new BeaconConnection(activity, currentBeacon);
                connection.start();
                break;
            //in questo stato si attende un certo periodo prima di effettuare un altro scan
            case (2):
                if (currentBeacon==null) closeConnection();
                waiting();
                break;
            //in questo stato si gestisce lo spegnimento della macchina a stati
            case (3):
                activity.getBaseContext().sendBroadcast(new Intent("TerminatedScan"));
                break;
        }
    }

    /**
     * Metodo per chiudere la connessione, una volta terminata
     */

    public void closeConnection() {
        connection = null;
    }

    //thread che si occupa di far partire lo scan in cerca dei beacon
    private Runnable startScan = new Runnable() {
        @Override
        public void run() {
            //cancella la lista di sensortag precedentemente trovati
            mLeDeviceListAdapter.clear();
            mLeDeviceListAdapter.setCurrentBeacon(null);

            Log.e(TAG, "Start Scan");
            //parte effettivamente la ricerca dei sensortag
            if (MainApplication.getmBluetoothAdapter() != null) {
                try {
                    MainApplication.getmBluetoothAdapter().getBluetoothLeScanner()
                            .startScan(scanFilters, scanSettings, mScanCallback);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("bluetooth error","accendi il bluetooth");
                }

            }

            //attende per la durata dello scan e poi lancia la runnable per stopparlo
            scanHandler.postDelayed(stopScan, setup.getScanPeriod());

        }
    };

    //thread per mettere in pausa lo scan ed eventualmente elaborare i dati
    private Runnable stopScan = new Runnable() {
        @Override
        public void run() {

            Log.e(TAG, "Stop Scan");
            try {
                MainApplication.getmBluetoothAdapter().getBluetoothLeScanner()
                        .stopScan(mScanCallback);
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e("bluetooth error","accendi il bluetooth");
            }

            Log.i(TAG,"numero: " + mLeDeviceListAdapter.getCount());

            //trova il beacon più vicino

            selectedBeacon = mLeDeviceListAdapter.selectedDevice();

            if(selectedBeacon!=null){
                if (currentBeacon==null || !currentBeacon.getAddress().equals(mLeDeviceListAdapter.getCurrentBeacon().getAddress())) {
                    currentBeacon = mLeDeviceListAdapter.getCurrentBeacon();
                    cont = 0;
                    update();
                }
                //nel caso per n cicli non venga aggiornato
                else {
                    cont++;
                    if(cont>=maxNoUpdate) {
                        currentBeacon = selectedBeacon;
                        update();
                        cont = 0;
                    }

                }
            }

            //finita l'esecuzione dello stato richiama
            int next = nextState();
            changeState(next);
            executeState();


        }
    };

    //thread per gestire l'attesa fra due scan consecutivi
    private Runnable wait = new Runnable() {
        @Override
        public void run() {
            //finita l'attesa richiama i metodi per passare allo stato successivo
            int next = nextState();
            changeState(next);
            executeState();
        }
    };


    /**
     * Metodo che cancella la registrazione del broadcast receiver
     */
    public void closeScan() {
        if(broadcastReceiver!=null) activity.getBaseContext().unregisterReceiver(broadcastReceiver);
    }

    //callback utilizzata per trovare dispositivi nel raggio d'azione
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device,rssi);
                        }
                    });
                }

            };


    //callback utilizzata per trovare dispositivi nel raggio d'azione
    private ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("result", result.toString());
            BluetoothDevice btDevice = result.getDevice();
            mLeDeviceListAdapter.addDevice(btDevice,result.getRssi());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            System.out.println("BLE// onBatchScanResults");
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            System.out.println("BLE// onScanFailed");
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }

    };

    /**
     * Metodo per aggiornare la posizione dell'utente e per notificarla al server qualora collegato
     */
    @Override
    public void update() {
        String mex = packingMessage();
        //viene aggiornato il beacon a cui si è collegato l'utente
        String cod = currentBeacon.getAddress();

        Log.e("get","BeaconAddress: " + cod);


        Controller.aggiornaPosizioneUtente(cod, MainApplication.getCurrentActivity().getApplicationContext());

        if(Percorso.isGestionePercorso()){
            Controller.gestisciPercorso();
        }
        //TODO aggiungere(se possibile) condizione per inviare la posizione solo quando cambia
        if(MainApplication.getOnlineMode()) {
            try {
                //inviato un messaggio con la posizione dell'utente
                Server.sendPosition(mex);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void retrive() {

    }
}
