package ids.univpm.breakout.view;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ids.univpm.breakout.R;

public class Welcome extends AppCompatActivity {

    public Button bottoneMappe;
    public Button bottoneAccedi;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        bottoneMappe = (Button) findViewById(R.id.mappeoffline);

        //Premendo mappe offline, faccio un check: se non è connesso al server, appare il toast
        // oppure se è connesso al server MA non si è mai autenticato quindi non ha le mappe

        bottoneMappe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //inserire check x connessione al server, se non è connesso --> toast
                Toast.makeText(getApplicationContext(), "Accedi per scaricare le mappe", Toast.LENGTH_LONG).show();

                //Se si era gia connesso in precedenza, allora passo a navigation 1, ma in modalità offline
                // startActivity(new Intent(Login.this, Navigation1.class));

            }
        });

        bottoneAccedi = (Button) findViewById(R.id.accedi);
        //Premendo accedi, passo alla schermata di login
        bottoneAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome.this, Login.class));
            }
        });
    }

}
