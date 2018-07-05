package ids.univpm.breakout.controller;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;

import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.DBHelper;
import ids.univpm.breakout.model.database.Mappa.MappaManager;
import ids.univpm.breakout.model.database.Nodi.NodoManager;
import ids.univpm.breakout.model.database.Utente.UtenteManager;

public class Controller {

    public static int checkLog(Context ctx) {
        ArrayList<Utente> listaUtenti;
        listaUtenti = getUtenti(ctx);
        int flag=0;
        for (Utente i:listaUtenti){
            if (i.getIs_logged()==1){
                flag = i.getIs_logged();
            }
        }

        return flag;
    }

    public static ArrayList<Utente> getUtenti(Context ctx){
        ArrayList<Utente> utenti = new ArrayList<>();
        UtenteManager utentiMng = new UtenteManager(ctx);
        utenti = utentiMng.findAll();
        return utenti;
    }

    public static boolean checkMappe(Context ctx) {
        MappaManager mappaMng = new MappaManager(ctx);
        Cursor crs = mappaMng.query();

        boolean flag=false;

        if (crs.getCount()!=0){
            flag=true;
        }

        return flag;
    }

    public static ArrayList<Mappa> getMappe(Context ctx){
        ArrayList<Mappa> mappe;
        MappaManager mappaMng = new MappaManager(ctx);
        mappe = mappaMng.findAll();
        return mappe;
    }

    public static boolean checkConnection(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /*TODO metodo che effettua l'autenticazione dell'utente:
    - invio richiesta di autenticazione dell'utente al server
    - modifica del campo is_loggato nella tabella dell'utente nel DB locale
     */
    public static boolean verificaAutenticazioneUtente(String user, String pass) {

        Context ctx = MainApplication.getCurrentActivity().getApplicationContext();

        UtenteManager u_manager= new UtenteManager(ctx);

        boolean flag = false;

        try {

            Utente utente_log=u_manager.findByUser(user);

            if(utente_log!=null && utente_log.getPassword().equals(pass)){
                flag = true;
                Server.autenticazioneUtente(user, pass);
                //TODO: per la posizione dell utente?
            }

        } catch (Exception e) {
            //gestire eccezioni
            e.printStackTrace();
        }
        return flag;



    }
    public static void aggiornamentoMappe(){
        if (checkModifiche()){
            //TODO procedura che scarica le modifiche dal server
        }
    }

    //TODO metodo che confronta la data dell'ultima modifica nel DB locale con le modifiche nel server
    public static boolean checkModifiche() {
        return false;
    }

    public static Integer getPosizione(String username, Context ctx) {
        UtenteManager utenteMng = new UtenteManager(ctx);
        Utente utente = utenteMng.findByUser(username);
        Integer idbeacon = null;
        if(utente.getUltima_posizione() != null){
            idbeacon = utente.getUltima_posizione();
        }

        return idbeacon;
    }

    public static ArrayList<Pdi> getPDIs(Context ctx) {
        NodoManager nodoMng = new NodoManager(ctx);
        ArrayList<Pdi> listaPdi;
        listaPdi = nodoMng.findAllPdi();

        return listaPdi;
    }
}
