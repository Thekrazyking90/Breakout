package ids.univpm.breakout.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ids.univpm.breakout.R;

class RicercaPDI extends AppCompatActivity {

    public ImageView non_connesso;
    public ImageView connesso;
    public TextView connection_status;
    ArrayAdapter<String> adapter;
    private Menu menu;


    // inserire check per la connessione al server --> sotto icona rossa oppure verde x connesso
    // far si che appaia la mappa del piano in cui mi trovo, in base al beacon a cui sono connesso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ricerca_pdi);
        ListView lv=(ListView) findViewById(R.id.lista_pdi);
        ArrayList<String> listaPdi = new ArrayList<>();
        listaPdi.addAll(Arrays.asList(getResources().getStringArray(R.array.lista_pdi)));

        adapter=new ArrayAdapter<String>(RicercaPDI.this, android.R.layout.simple_list_item_1, listaPdi);
        lv.setAdapter(adapter);

        //Check connessione: di default disconnesso
        //se non connesso: Server connection: disconnected, simbolo X
        connection_status=(TextView) findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        non_connesso.setVisibility(View.VISIBLE);

        // se connesso: connected: simbolo verde
        connesso= findViewById(R.id.connesso);
        //connesso.setVisibility(View.VISIBLE);
        //non_connesso.setVisibility(View.INVISIBLE);
        //connection_status.setText("Connected");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.navigation_menu,menu);
        //Hide options for PDI
        MenuItem pdi_menu= menu.findItem(R.id.pdi);
        pdi_menu.setVisible(false);
        //Se non loggato, cambia titolo in login nel menu(!)

        MenuItem item = menu.findItem(R.id.ricerca);
        SearchView searchView= (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });

        return true;
        //return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.SelPiano:

                //Apertura nuova activity per scelta del piano
                startActivity(new Intent(RicercaPDI.this, SelPiano.class));

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
