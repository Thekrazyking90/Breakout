package ids.univpm.breakout.view;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ids.univpm.breakout.R;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;

public class Welcome extends AppCompatActivity {

    private Button bottoneMappe;
    private Button bottoneAccedi;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.setCurrentActivity(this);

        setContentView(R.layout.activity_welcome);
        bottoneMappe = (Button) findViewById(R.id.mappeoffline);

        //Premendo mappe offline, faccio un check: se non è connesso al server, appare il toast
        // oppure se è connesso al server MA non si è mai autenticato quindi non ha le mappe

        bottoneMappe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    startActivity(new Intent(Welcome.this, Navigation1.class));
                }else{
                    startActivity(new Intent(Welcome.this, Login.class));
                }
            }
        });
    }

}
