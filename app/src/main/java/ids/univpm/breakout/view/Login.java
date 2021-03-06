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
                    intent.putExtra("ID_Mappa", Controller.getIdMappaPosizioneCorrente());

                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Controller.sendNullPosition();
    }

}

