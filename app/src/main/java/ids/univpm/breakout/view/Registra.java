package ids.univpm.breakout.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ids.univpm.breakout.R;
import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.database.Utente.UtenteManager;

public class Registra extends AppCompatActivity {

    private EditText Username;
    private EditText Email;
    private EditText Password;
    private EditText PassCheck;
    private EditText Nome;
    private EditText Cognome;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.setCurrentActivity(this);

        setContentView(R.layout.registra);
        Email = findViewById(R.id.email_txt);
        Username=findViewById(R.id.username_txt);
        Password=findViewById(R.id.pass_txt1);
        PassCheck=findViewById(R.id.pass_txt2);
        Nome= findViewById(R.id.name_txt);
        Cognome= findViewById(R.id.surname_txt);

        Button mInvia = findViewById(R.id.profile_button);

        mInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Username.getText().toString().equals("") &&
                        !Password.getText().toString().equals("") &&
                        !Email.getText().toString().equals("") &&
                        !Nome.getText().toString().equals("") &&
                        !Cognome.getText().toString().equals("") &&
                        !PassCheck.getText().toString().equals(""))
                {

                    if(!Password.getText().toString().equals(PassCheck.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Le password non corrispondono", Toast.LENGTH_LONG).show();
                    }else if(Server.handShake()){
                        if(Server.checkUsername(Username.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Username già in uso", Toast.LENGTH_LONG).show();
                        }else{
                            if(Server.registrazioneUtente(Username.getText().toString(),
                                    Password.getText().toString(),
                                    Email.getText().toString(),
                                    Nome.getText().toString(),
                                    Cognome.getText().toString())){
                                Toast.makeText(getApplicationContext(), "Registrazione avvenuta con successo", Toast.LENGTH_LONG).show();

                                UtenteManager utenteManager = new UtenteManager(Registra.this);
                                utenteManager.save(null,
                                        Email.getText().toString(),
                                        Nome.getText().toString(),
                                        Password.getText().toString(),
                                        Cognome.getText().toString(),
                                        Username.getText().toString(),
                                        0);

                                if(Controller.verificaAutenticazioneUtente(Username.getText().toString(), Password.getText().toString())){
                                    Intent intent = new Intent(Registra.this, Navigation1.class);
                                    intent.putExtra("ID_Activity", "From_Registra");
                                    intent.putExtra("ID_Mappa", Controller.getIdMappaPosizioneCorrente());
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Accesso fallito, riprovare più tardi", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Registrazione fallita", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Non sei connesso alla rete, connettiti o riprova più tardi", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Completa i campi obbligatori", Toast.LENGTH_LONG).show();
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


