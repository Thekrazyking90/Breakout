package ids.univpm.breakout.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ids.univpm.breakout.R;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.Nodo;
import ids.univpm.breakout.model.Scala;
import ids.univpm.breakout.model.Tronco;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;
import ids.univpm.breakout.model.database.Mappa.MappaManager;
import ids.univpm.breakout.model.database.Nodi.NodoManager;
import ids.univpm.breakout.model.database.Tronchi.TroncoManager;

/**
 * A login screen that offers login via username/password.
 */
public class Login extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private TextView mRegistra;
    private Button mSignInButton;
    private TextView mErroreDati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.setCurrentActivity(this);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mRegistra= findViewById(R.id.registra);
        mUsernameView =  findViewById(R.id.username);
        mPasswordView =  findViewById(R.id.password);
        mSignInButton =  findViewById(R.id.signin_button);
        mErroreDati = findViewById(R.id.errore_dati);

        mRegistra.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registra.class));
                    }
        });

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mUsernameView.getText().toString();
                String pass = mPasswordView.getText().toString();
                if(Controller.verificaAutenticazioneUtente(user, pass)){
                    Intent intent = new Intent(Login.this, Navigation1.class);
                    intent.putExtra("ID_Activity", "From_Login");
                    intent.putExtra("ID_Mappa", getIdMappaPosizioneCorrente());

                    startActivity(intent);
                }else{
                    mErroreDati.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    public Integer getIdMappaPosizioneCorrente(){
        Integer ID_map;

        BeaconManager beaconManager = new BeaconManager(Login.this);
        if(Controller.getPosizioneCorrente(Login.this) != null){
            Beacon beacon_current_position = beaconManager.findById(Controller.getPosizioneCorrente(Login.this));
            if(beacon_current_position.getID_pdi() == null) {
                TroncoManager troncoManager = new TroncoManager(Login.this);
                Scala tronco = troncoManager.findByIdBeacon(beacon_current_position.getID_beacon());

                NodoManager nodoManager = new NodoManager(Login.this);
                Nodo nodo = nodoManager.findById(tronco.getNodi_Integer()[0]);

                ID_map = nodo.getID_mappa();
            }else{
                NodoManager nodoManager = new NodoManager(Login.this);
                Nodo nodo = nodoManager.findById(beacon_current_position.getID_pdi());

                ID_map = nodo.getID_mappa();
            }
        }else{
            ID_map = null;
        }

        return ID_map;


    }
}

