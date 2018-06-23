package ids.univpm.breakout.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ids.univpm.breakout.R;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;

public class Navigation1 extends AppCompatActivity {

    private TextView connection_status;
    private ImageView non_connesso;
    private ImageView connesso;

    // inserire check per la connessione al server --> sotto icona rossa oppure verde x connesso
    // far si che appaia la mappa del piano in cui mi trovo, in base al beacon a cui sono connesso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation1);

        MainApplication.start(this);

        connection_status= findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        connesso= findViewById(R.id.connesso);

        if(Controller.checkConnection(Navigation1.this)) {
            non_connesso.setVisibility(View.INVISIBLE);
            connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Connected");
        }else{
            connesso.setVisibility(View.INVISIBLE);
            non_connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Disconnected");
        }

//TODO caricamento mappa
        if(getIntent() != null){
            //get id mappa from intent
        }else{

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        //Se non loggato, cambia titolo in login nel menu(!)

        MenuItem item = menu.findItem(R.id.ricerca);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                startActivity(new Intent(Navigation1.this, RicercaPDI.class));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.SelPiano:

                //Apertura nuova activity per scelta del piano
                startActivity(new Intent(Navigation1.this, SelPiano.class));

                break;

            case R.id.pdi:
                startActivity(new Intent(Navigation1.this, RicercaPDI.class));
                break;


            case R.id.log_status:

                //Inserire codice per LogOut se deve fare logout
                Intent returnBtn = new Intent(getApplicationContext(), Welcome.class);

                startActivity(returnBtn);
                break;

        }
        return false;
        //return super.onOptionsItemSelected(item);
    }
}

