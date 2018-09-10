package ids.univpm.breakout.communication.emergenza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.communication.StateMachine;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.Scala;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;
import ids.univpm.breakout.model.database.Tronchi.TroncoManager;
import ids.univpm.breakout.utility.CamminoMinimo;
import ids.univpm.breakout.utility.Percorso;
import ids.univpm.breakout.view.Navigation1;

public class EmergenzaScanner extends StateMachine {
    private SetupE setup;
    private Handler scanHandler;
    private Activity activity;

    public static boolean emergency = false;

    private Runnable wait = new Runnable() {
        @Override
        public void run() {
            //finita l'attesa richiama i metodi per passare allo stato successivo
            int next = nextState();
            changeState(next);
            executeState();
        }
    };
    private static boolean emergencyServer;

    public EmergenzaScanner(Activity a) {
        this.setup = new SetupE();
        this.activity = a;
        this.scanHandler = new Handler();

        executeState();
    }

    protected int nextState() {
        int next;
        switch(currentState) {
            case(0):
                if (!running) next = 3;
                else if (running && setup.mustAnalyze()) next = 1;
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

    private void waiting() {
        Log.i("WAITING EMERGENCY SCAN","waiting for " + setup.getPeriodBetweenScan()/1000 + " seconds for new Scan ");
        scanHandler.postDelayed(wait,setup.getPeriodBetweenScan());
    }


    protected void executeState() {
        Log.i("State","execute scan state " + getState());
        switch(currentState) {
            //in questo stato viene effettuato lo scan delle emergenze
            case(0):
                checkEmergenze();
                break;
            //in questo stato viene reinizializzato lo Scanner dei Beacon
            case(1):
                //codice cambiamento stato
                setEmergenza();
                break;
            //in questo stato si attende un certo periodo prima di effettuare un altro scan
            case (2):
                waiting();
                break;
            //in questo stato si gestisce lo spegnimento della macchina a stati
            case (3):
                activity.getBaseContext().sendBroadcast(new Intent("TerminatedEmergencyScan"));
                break;
        }
    }

    private void setEmergenza() {
        MainApplication.setEmergency(emergency);
        if(emergency){
            Context ctx = MainApplication.getCurrentActivity().getApplicationContext();
            MainApplication.setScanner(null);
            MainApplication.initializeScanner(MainApplication.getCurrentActivity(), "EMERGENCY");

            Pdi uscita = Controller.findNearestExit(ctx);
            Navigation1.idSelectedPdi = uscita.getID();

            if(Controller.getPosizioneCorrente(ctx) != null){
                BeaconManager beaconManager = new BeaconManager(ctx);
                Beacon beacon = beaconManager.findById(Controller.getPosizioneCorrente(ctx));

                if (beacon.getID_pdi() == null){
                    TroncoManager troncoManager = new TroncoManager(ctx);
                    Scala tronco = troncoManager.findByIdBeacon(beacon.getID_beacon());

                    CamminoMinimo camminoMinimo = new CamminoMinimo(ctx);
                    ArrayList<Integer> percorso = camminoMinimo.Dijkstra_Tronco(Navigation1.idSelectedPdi,tronco.getID());

                    Percorso.setGestionePercorso(true);
                    Percorso.cammino = percorso;

                    Controller.gestisciPercorso();
                }else{
                    CamminoMinimo camminoMinimo = new CamminoMinimo(ctx);
                    ArrayList<Integer> percorso = camminoMinimo.Dijkstra_Tronco(Navigation1.idSelectedPdi,beacon.getID_pdi());

                    Percorso.setGestionePercorso(true);
                    Percorso.cammino = percorso;

                    Controller.gestisciPercorso();
                }

            }
        } else {
            MainApplication.setScanner(null);
            MainApplication.initializeScanner(MainApplication.getCurrentActivity());
        }

        int next = nextState();
        changeState(next);
        executeState();
    }

    private void checkEmergenze() {

      emergencyServer = Server.checkEmergenze();

        if(emergency && !emergencyServer){
            emergency = false;
            Percorso.cammino.clear();
            Controller.gestisciPercorso();
            Percorso.setGestionePercorso(false);
        }else{
            if(emergency){

            }else{
                emergency = emergencyServer;
            }

        }

        int next = nextState();
        changeState(next);
        executeState();
    }

    public void suspendScan() {
        Log.i("SUSPENDEMERGENCYSCAN","suspend scan");
        running = false;

        //se si sta aspettando per un nuovo scan, viene abortito il processo di attesa
        //e si passa allo stato successivo (gestione della chiusura dello scan)
        if(currentState==2) {
            scanHandler.removeCallbacks(wait);
            int next = nextState();
            changeState(next);
            executeState();
        }
    }
}
