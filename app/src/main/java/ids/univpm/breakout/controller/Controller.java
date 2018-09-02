package ids.univpm.breakout.controller;

import android.app.AlertDialog;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ids.univpm.breakout.R;
import ids.univpm.breakout.communication.Server;
import ids.univpm.breakout.model.Beacon;
import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.Modifica;
import ids.univpm.breakout.model.Nodo;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.Scala;
import ids.univpm.breakout.model.Tronco;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.Beacon.BeaconManager;
import ids.univpm.breakout.model.database.Mappa.MappaManager;
import ids.univpm.breakout.model.database.Modifiche.ModificheManager;
import ids.univpm.breakout.model.database.Nodi.NodoManager;
import ids.univpm.breakout.model.database.Piano.PianoManager;
import ids.univpm.breakout.model.database.Tronchi.TroncoManager;
import ids.univpm.breakout.model.database.Utente.UtenteManager;
import ids.univpm.breakout.utility.CamminoMinimo;
import ids.univpm.breakout.utility.Percorso;
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
        ArrayList<Utente> utenti;
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

                aggiornamentoMappe();

            } catch (Exception e) {
                //gestire eccezioni
                e.printStackTrace();
                //TODO comunicazione "dati errati"
                u_manager.updateIs_loggato(utente_log, false);
                flag = false;
            }
        }

        return flag;
    }

    public static void downloadAllImgMappa(ArrayList<Mappa> mappe) {
        for (Mappa map: mappe) {
            Server.downloadImgMappa(map.getImmagine());
        }
    }

    public static void aggiornamentoMappe(){
        Context ctx = MainApplication.getCurrentActivity().getApplicationContext();

        ModificheManager modificheMng = new ModificheManager(ctx);
        Modifica mod = modificheMng.findLast();

        if(mod != null){
            if (Server.checkModifiche(mod.getData())){
                HashMap<String,String>[] listaModHash = Server.downloadModifiche(mod.getData());

                MappaManager mappaMng = new MappaManager(ctx);
                BeaconManager beaconMng = new BeaconManager(ctx);
                TroncoManager troncoMng = new TroncoManager(ctx);
                NodoManager nodoMng = new NodoManager(ctx);
                PianoManager pianoMng = new PianoManager(ctx);

                //TODO rivedere nomi tabelle
                for (HashMap<String, String> modifica: listaModHash) {
                    switch (modifica.get("tabella")){
                        case "mappa":{
                            mappaMng.resetTable();
                            saveMappe(ctx);
                            downloadAllImgMappa(mappaMng.findAll());
                        }
                        case "beacon":{
                            beaconMng.resetTable();
                            saveBeacon(ctx);
                        }
                        case "tronco":{
                            troncoMng.resetTable();
                            saveTronchi(ctx);
                        }
                        case "nodo":{
                            nodoMng.resetTable();
                            saveNodi(ctx);
                        }
                        case "piano":{
                            pianoMng.resetTable();
                            savePiani(ctx);
                        }
                    }

                }

                modificheMng.save(null,null,null,null, String.valueOf(System.currentTimeMillis()));
            }
        }else{
            saveDB(ctx);
            modificheMng.save(null,null,null,null, String.valueOf(System.currentTimeMillis()));
        }
    }


    public static void saveDB(Context ctx){
        saveMappe(ctx);
        saveBeacon(ctx);
        saveTronchi(ctx);
        saveNodi(ctx);
        savePiani(ctx);
    }

    public static void saveMappe(Context ctx){
        HashMap<String, String>[] Mappe = Server.downloadMappe();

        MappaManager mappaMng = new MappaManager(ctx);

        for (HashMap<String, String> record: Mappe) {
            Integer ID = Integer.getInteger(record.get("ID"));
            Integer ID_piano = Integer.getInteger(record.get("ID_piano"));
            String img = record.get("immagine");
            String nome = record.get("nome");

            Server.downloadImgMappa(img);

            mappaMng.save(ID, ID_piano, img, nome);
        }
    }

    public static void saveBeacon(Context ctx){
        HashMap<String, String>[] Beacon = Server.downloadBeacon();

        BeaconManager beaconMng = new BeaconManager(ctx);

        for (HashMap<String, String> record: Beacon) {
            Integer ID = Integer.getInteger(record.get("ID"));
            Integer IDMap = Integer.getInteger(record.get("ID_mappa"));
            Integer IDPiano = Integer.getInteger(record.get("ID_piano"));
            String address = record.get("address");
            Integer ID_PDI = Integer.getInteger(record.get("ID_PDI"));
            Float coordx = Float.valueOf(record.get("coordX"));
            Float coordy = Float.valueOf(record.get("coordY"));
            Float fire = Float.valueOf(record.get("fire"));
            Float smoke = Float.valueOf(record.get("smoke"));
            Float ncd = Float.valueOf(record.get("NCD"));
            Float risk = Float.valueOf(record.get("risk"));

            beaconMng.save(ID, IDMap, IDPiano, address, ID_PDI, coordx, coordy, fire, smoke, ncd, risk);
        }
    }

    public static void saveTronchi(Context ctx){
        HashMap<String, String>[] Tronchi = Server.downloadTronchi();

        TroncoManager troncoMng = new TroncoManager(ctx);

        for (HashMap<String, String> record: Tronchi) {
            Integer ID = Integer.getInteger(record.get("ID"));
            Integer IDMap = Integer.getInteger(record.get("ID_mappa"));
            Integer IDPiano = Integer.getInteger(record.get("ID_piano"));
            Integer node1 = Integer.getInteger(record.get("nodo1"));
            Integer node2 = Integer.getInteger(record.get("nodo2"));
            Integer beacon = Integer.getInteger(record.get("beacon"));
            Float length = Float.valueOf(record.get("lunghezza"));

            troncoMng.save(ID,IDMap, IDPiano, node1,node2,beacon,length);
        }
    }

    public static void saveNodi(Context ctx){
        HashMap<String, String>[] Nodi = Server.downloadNodi();

        NodoManager nodoMng = new NodoManager(ctx);

        for (HashMap<String, String> record: Nodi) {
            Integer ID = Integer.getInteger(record.get("ID"));
            Integer ID_mappa = Integer.getInteger(record.get("ID_mappa"));
            Float coordx = Float.valueOf(record.get("coordX"));
            Float coordy = Float.valueOf(record.get("coordY"));
            String code = record.get("codice");
            Float width = Float.valueOf(record.get("larghezza"));
            Float length = Float.valueOf(record.get("lunghezza"));
            Boolean is_pdi = Boolean.parseBoolean(record.get("is_pdi"));
            String type = record.get("tipo");

            nodoMng.save(ID,ID_mappa,coordx,coordy,code,width,length,is_pdi,type);
        }
    }

    public static void savePiani(Context ctx){
        HashMap<String, String>[] Piani = Server.downloadPiani();

        PianoManager pianoMng = new PianoManager(ctx);

        for (HashMap<String, String> record: Piani) {
            Integer ID = Integer.getInteger(record.get("ID"));
            String name = record.get("nome");

            pianoMng.save(ID, name);
        }
    }


    public static Integer getPosizioneCorrente(Context ctx) {
        UtenteManager utenteMng = new UtenteManager(ctx);
        Utente utente = utenteMng.findByIsLoggato();
        Integer idbeacon = null;
        if(utente.getID_beacon() != null){

            idbeacon = utente.getID_beacon();
        }

        return idbeacon;
    }

    public static ArrayList<Pdi> getPDIs(Context ctx) {
        NodoManager nodoMng = new NodoManager(ctx);
        ArrayList<Pdi> listaPdi;
        listaPdi = nodoMng.findAllPdi();

        return listaPdi;
    }

    public static void aggiornaPosizioneUtente(String cod, Context ctx) {
        UtenteManager utenteManager = new UtenteManager(ctx);
        Utente user;

        if (utenteManager.AnyIsLoggato()) {
            BeaconManager beaconManager = new BeaconManager(ctx);
            Beacon beacon = beaconManager.findByAddress(cod);
            user = utenteManager.findByIsLoggato();
            utenteManager.updatePosition(user, beacon.getID_beacon());
        }
    }

    public static Pdi findNearestExit(Context ctx){
        BeaconManager beaconManager = new BeaconManager(ctx);
        Beacon beaconPosizione = beaconManager.findById(Controller.getPosizioneCorrente(ctx));
        Integer idMap = beaconPosizione.getID_map();
        NodoManager nodoManager = new NodoManager(ctx);
        ArrayList<Pdi> listaPdi = nodoManager.findAllPdi();

        ArrayList<Pdi> listaUsciteVicine = new ArrayList<>();
        for (Pdi pdi: listaPdi) {
            if(pdi.getTipo().contains("emergen") && pdi.getID_mappa()==idMap){
                listaUsciteVicine.add(pdi);
            }
        }

        Pdi uscitaVicina = null;

        for (Pdi uscita: listaUsciteVicine) {
            if(uscitaVicina != null){
                Double distUscita;
                Double distUscitaVicina;
                distUscita = Math.sqrt(Math.pow((double) (beaconPosizione.getCoord_X() - uscita.getCoord_X()), (double) 2) +
                        Math.pow((double) (beaconPosizione.getCoord_Y() - uscita.getCoord_Y()), (double) 2));
                distUscitaVicina = Math.sqrt(Math.pow((double) (beaconPosizione.getCoord_X() - uscitaVicina.getCoord_X()), (double) 2) +
                        Math.pow((double) (beaconPosizione.getCoord_Y() - uscitaVicina.getCoord_Y()), (double) 2));
                if(distUscita < distUscitaVicina){
                    uscitaVicina = uscita;
                }
            }else{
                uscitaVicina = uscita;
            }
        }

        return uscitaVicina;
    }


    public static void gestisciPercorso(){
        Context ctx = MainApplication.getCurrentActivity().getApplicationContext();
        BeaconManager beaconManager = new BeaconManager(ctx);
        Beacon beaconPosizione = beaconManager.findById(getPosizioneCorrente(ctx));
        CamminoMinimo camminoMinimo = new CamminoMinimo(ctx);

        if(beaconPosizione.getID_pdi() != null){
            TroncoManager troncoManager = new TroncoManager(ctx);
            ArrayList<Integer> stellaPdi = troncoManager.getArcsByNode_Integer(beaconPosizione.getID_pdi());

            if(stellaPdi.contains(Percorso.cammino.get(0))){
                Percorso.setGestionePercorso(false);
                Percorso.cammino.clear();

                AlertDialog.Builder alertArrivo = new AlertDialog.Builder(MainApplication.getCurrentActivity().getApplicationContext());
                alertArrivo.setTitle("Arrivo");
                alertArrivo.setMessage("Sei arrivato a destinazione");
                AlertDialog alert = alertArrivo.create();
                alert.show();

                Navigation1.idSelectedPdi = null;

            }else{
                Percorso.cammino = camminoMinimo.Dijkstra_PDI(Percorso.cammino.get(0), beaconPosizione.getID_pdi());

                Navigation1.bitmap = Navigation1.disegnoMappa(beaconPosizione.getID_map());
                Navigation1.aggiornaMappa();
            }
        }else{
            TroncoManager troncoManager = new TroncoManager(ctx);
            Tronco arcPosizione = troncoManager.findByIdBeacon(beaconPosizione.getID_beacon());

            if(Percorso.cammino.contains(arcPosizione.getID())){
                if(arcPosizione.getID() != Percorso.cammino.get(Percorso.cammino.size() - 1)){
                    Percorso.cammino = (ArrayList<Integer>) Percorso.cammino.subList(0, Percorso.cammino.indexOf(arcPosizione.getID()) + 1);

                    Navigation1.bitmap = Navigation1.disegnoMappa(beaconPosizione.getID_map());
                    Navigation1.aggiornaMappa();
                }
            }else{
                Percorso.cammino = camminoMinimo.Dijkstra_Tronco(Percorso.cammino.get(0), arcPosizione.getID());

                Navigation1.bitmap = Navigation1.disegnoMappa(beaconPosizione.getID_map());
                Navigation1.aggiornaMappa();
            }
        }

    }
}
