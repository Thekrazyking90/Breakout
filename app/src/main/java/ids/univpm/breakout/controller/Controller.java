package ids.univpm.breakout.controller;

import android.content.Context;

import java.util.ArrayList;

import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.database.Mappa.MappaManager;

public class Controller {

//TODO metodo che controlla se l'utente nel DB locale ha true nel campo is_loggato
    public static boolean checkLog() {
        return false;
    }

//TODO metodo che controlla se sono presenti mappe nel DB locale
    public static boolean checkMappe() {
        return false;
    }

    //TODO revisione del metodo getMappe che prende le mappe dal DB locale
    public static ArrayList<Mappa> getMappe(Context ctx){
        ArrayList<Mappa> mappe = new ArrayList<>();
        //MappaManager mappaMng = new MappaManager(ctx);
        //mappe = mappaMng.findAll();
        return mappe;
    }

    //TODO metodo che controlla se il dispositivo è connesso o meno ad internet
    public static boolean checkConnection() {
        return false;
    }

    /*TODO metodo che effettua l'autenticazione dell'utente:
    - invio richiesta di autenticazione dell'utente al server
    - modifica del campo is_loggato nella tabella dell'utente nel DB locale
     */
    public static boolean autenticazioneUtente(String user, String pass) {
        return false;
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

    //TODO metodo che prende la posizione dell'utente dal DB locale
    public static long getPosizione(String user) {
        return 0;
    }
}
