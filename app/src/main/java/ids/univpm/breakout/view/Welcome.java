package ids.univpm.breakout.view;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ids.univpm.breakout.R;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.DBHelper;
import ids.univpm.breakout.model.database.Utente.UtenteManager;
import ids.univpm.breakout.model.database.Utente.UtenteStrings;

public class Welcome extends AppCompatActivity {

    private Button bottoneMappe;
    private Button bottoneAccedi;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!Controller.checkMappe(Welcome.this)) {
            UtenteManager utenteManager = new UtenteManager(Welcome.this);
            utenteManager.save(null,"guest@guest.com","guest","guest","guest","guest",0);
        }

            MainApplication.setCurrentActivity(this);

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        setContentView(R.layout.activity_welcome);
        bottoneMappe = findViewById(R.id.mappeoffline);


        bottoneMappe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.setOnlineMode(false);
                if(Controller.checkMappe(Welcome.this)){
                    startActivity(new Intent(Welcome.this, SelPiano.class));
                }else {
                    Toast.makeText(getApplicationContext(), "Accedi per scaricare le mappe", Toast.LENGTH_LONG).show();
                }

            }
        });

        bottoneAccedi = findViewById(R.id.accedi);
        //Premendo accedi, passo alla schermata di login
        bottoneAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Controller.checkLog(Welcome.this)==1){
                    UtenteManager utenteMng = new UtenteManager(Welcome.this);
                    Utente user = utenteMng.findByIsLoggato();

                    if (Controller.verificaAutenticazioneUtente(user.getUsername(), user.getPassword())){
                        Intent intent = new Intent(Welcome.this, Navigation1.class);
                        intent.putExtra("ID_Activity", "From_Welcome");
                        intent.putExtra("ID_Mappa", Controller.getIdMappaPosizioneCorrente());

                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Errore nell'autenticazione, riprova", Toast.LENGTH_LONG).show();
                    }
                }else{
                    startActivity(new Intent(Welcome.this, Login.class));
                }
            }
        });


        //CODICE DI TEST
        //Server.downloadImgMappa("8755576_xl.jpg");

        //CODICE DI TEST
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Controller.sendNullPosition();
    }

}
