package ids.univpm.breakout.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;

import ids.univpm.breakout.R;
import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.DBHelper;
import ids.univpm.breakout.model.database.Mappa.MappaManager;
import ids.univpm.breakout.model.database.Nodi.NodoManager;
import ids.univpm.breakout.model.database.Utente.UtenteManager;
import ids.univpm.breakout.view.Navigation1;

import static android.graphics.Color.RED;

public class Controller {



    public static void displayNotifica(int ID_Notifica, Context context) {
        //TODO: questo metodo va richiamato solo in caso di emergenza, quindi SE comunicato dal server
        // dopo che un dispositivo ha rilevato valori anomali da un Beacon (fuoco)
        // Gli passo l'ID della notifica, che va settato come int ID_Notifica=1 PRIMA di richiamare il
        // metodo displayNotifica, all'interno della activity o della classe desiderata.


        Intent Int_Notifica = new Intent(context, Navigation1.class);
        Int_Notifica.putExtra("ID_Notifica", ID_Notifica);

        //Questo genera la notifica.

        //Il PendingIntent ci consentirà di comunicare con il servizio di notifica caricato su un oggetto
        //NotificationManager. Quest'ultimo, sarà utilizzato per inviare la notifica creata con il
        // Notification.Builder, settando opportuni attributi come il titolo, l'oggetto, l'icona, ecc.
        //Al click sulla notifica verrà avviata l'Activity indicata nella prima Intent del metodo
        //displayNotifica.

        PendingIntent pint = PendingIntent.getActivity(context, 0, Int_Notifica,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder noti = new NotificationCompat.Builder(context,"notify_001");


        noti.setContentIntent(pint)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("EMERGENZA")
                .setColor(RED)
                .setContentText("Evacuare l'edificio!")
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.danger)
                .setPriority(Notification.PRIORITY_MAX);



        //Per fare apparire la notifica, da Oreo in poi serve questa serie di istruzioni
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            nmanager.createNotificationChannel(channel);
        }

        nmanager.notify(ID_Notifica, noti.build());

    }









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

    public static void checkConnection(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting() && Server.handShake())
            MainApplication.setOnlineMode(true);
        else MainApplication.setOnlineMode(false);
    }

    public static boolean verificaAutenticazioneUtente(String user, String pass) {

        Context ctx = MainApplication.getCurrentActivity().getApplicationContext();

        UtenteManager u_manager= new UtenteManager(ctx);
        Utente utente_log=u_manager.findByUser(user);

        ArrayList<Utente> listaUtenti = u_manager.findAll();

        for (Utente u: listaUtenti) {
            u_manager.updateIs_loggato(u, false);
        }

        boolean flag = false;

        if(utente_log!=null && utente_log.getPassword().equals(pass)) {
            flag = true;
            try {
                Server.autenticazioneUtente(user, pass);

                u_manager.updateIs_loggato(utente_log, true);

            } catch (Exception e) {
                //gestire eccezioni
                e.printStackTrace();

                u_manager.updateIs_loggato(utente_log, false);
                flag = false;
            }
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


    //TODO potrebbe essere inutile questo metodo
    public static Integer getPosizioneCorrente(String username, Context ctx) {
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
