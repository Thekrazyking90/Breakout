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
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Nodo;
import ids.univpm.breakout.model.Pdi;

public class RicercaPDI extends AppCompatActivity {

    private ImageView non_connesso;
    private ImageView connesso;
    private TextView connection_status;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.setCurrentActivity(this);

        setContentView(R.layout.ricerca_pdi);

        ListView lv= findViewById(R.id.lista_pdi);
        ArrayList<Pdi> listaPdi;
        listaPdi = Controller.getPDIs(RicercaPDI.this);

        ArrayList<String> listaPdiStrings= new ArrayList<>();
        for(Pdi i : listaPdi){
            listaPdiStrings.add(i.getTipo());
        }

        adapter=new ArrayAdapter<>(RicercaPDI.this, android.R.layout.simple_list_item_1, listaPdiStrings);
        lv.setAdapter(adapter);

        //Check connessione: di default disconnesso
        //se non connesso: Server connection: disconnected, simbolo X
        connection_status= findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        connesso= findViewById(R.id.connesso);

        if(Controller.checkConnection(RicercaPDI.this)) {
            non_connesso.setVisibility(View.INVISIBLE);
            connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Connected");
        }else{
            connesso.setVisibility(View.INVISIBLE);
            non_connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Disconnected");
        }

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
