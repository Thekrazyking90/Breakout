package ids.univpm.breakout.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ids.univpm.breakout.R;
import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.Nodi.NodoManager;
import ids.univpm.breakout.model.database.Utente.UtenteManager;
import ids.univpm.breakout.utility.Percorso;

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

        ListView listaRicerca= findViewById(R.id.lista_pdi);
        final ArrayList<Pdi> listaPdi;
        listaPdi = Controller.getPDIs(RicercaPDI.this);

        ArrayList<String> listaPdiStrings= new ArrayList<>();
        for(Pdi i : listaPdi){
            listaPdiStrings.add(i.getTipo());
        }

        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaPdiStrings);
        listaRicerca.setAdapter(adapter);

        listaRicerca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)parent.getItemAtPosition(position);
                NodoManager nodoManager = new NodoManager(RicercaPDI.this);
                Pdi pdi = nodoManager.findByTipo(value);

                Navigation1.idSelectedPdi = pdi.getID();

                Intent intent = new Intent(RicercaPDI.this, Navigation1.class);
                intent.putExtra("ID_Activity", "From_RicercaPDI");
                intent.putExtra("ID_Mappa", pdi.getID_mappa());
                intent.putExtra("ID_PDI", pdi.getID());

                startActivity(intent);
            }
        });

        //Check connessione: di default disconnesso
        //se non connesso: Server connection: disconnected, simbolo X
        connection_status= findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        connesso= findViewById(R.id.connesso);

        Controller.checkConnection(RicercaPDI.this);

        if(MainApplication.getOnlineMode()) {
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
        UtenteManager utenteManager = new UtenteManager(RicercaPDI.this);

        MenuItem mLogButton = menu.findItem(R.id.log_status);

        if(utenteManager.AnyIsLoggato()){
            String username = utenteManager.findByIsLoggato().getUsername();
            mLogButton.setTitle("Logout " + username);
        }else{
            mLogButton.setTitle("Login");
        }

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

        Intent intent = this.getIntent();
        if(intent.hasExtra("value")){
            searchView.setQuery(intent.getExtras().getCharSequence("value"), false);
        }

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
                UtenteManager utenteManager = new UtenteManager(RicercaPDI.this);

                Intent returnBtn;

                if(utenteManager.AnyIsLoggato()){
                    Utente user = utenteManager.findByIsLoggato();
                    utenteManager.updateIs_loggato(user ,false);

                    Server.logoutUtente(user.getUsername());
                    MainApplication.setScanner(null);
                    MainApplication.setEmergencyScanner(null);
                    Navigation1.idSelectedPdi = null;
                    Percorso.setGestionePercorso(false);
                    Percorso.cammino = new ArrayList<>();
                    returnBtn = new Intent(getApplicationContext(), Welcome.class);

                }else{
                    returnBtn = new Intent(getApplicationContext(), Login.class);
                }


                startActivity(returnBtn);
                break;

        }
        return false;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Controller.sendNullPosition();
    }


}
