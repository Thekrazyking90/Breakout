package ids.univpm.breakout.communication.Emergenza;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.communication.StateMachine;
import ids.univpm.breakout.controller.MainApplication;

public class EmergenzaScanner extends StateMachine {
    private SetupE setup;
    private Handler scanHandler;
    private Activity activity;

    private boolean emergency = false;

    private Runnable wait = new Runnable() {
        @Override
        public void run() {
            //finita l'attesa richiama i metodi per passare allo stato successivo
            int next = nextState();
            changeState(next);
            executeState();
        }
    };

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
            MainApplication.initializeScanner(MainApplication.getCurrentActivity(), "EMERGENCY");
        } else {
            MainApplication.initializeScanner(MainApplication.getCurrentActivity());
        }

        int next = nextState();
        changeState(next);
        executeState();
    }

    private void checkEmergenze() {
        emergency = Server.checkEmergenze();

        int next = nextState();
        changeState(next);
        executeState();
    }
}
