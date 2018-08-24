package ids.univpm.breakout.communication;


import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import ids.univpm.breakout.communication.http.GetRequest;
import ids.univpm.breakout.communication.http.PostRequest;
import ids.univpm.breakout.communication.message.MessageBuilder;
import ids.univpm.breakout.communication.message.MessageParser;

public class Server {

    private static String ip = "192.168.1.40"; //TODO da inserire ip
    private static String hostMaster = "Breakout_server"; //= hostname;
    private static SQLiteDatabase db;

    public static boolean handShake() {
        boolean b;
        try {
            String s = new GetRequest().execute(ip,"testconnection").get();
            Log.i("s",s);
            b = Boolean.parseBoolean(s);
        } catch (Exception e) {
            b = false;
        }
        return b;
    }

    private static final ArrayList<String> userProfileKeys = new ArrayList<String>(){{
        add("ID_utente");
        add("username");
        add("email");
        add("password");
        add("nome");
        add("cognome");
        add("ultima_pos");
    }};

    public static boolean autenticazioneUtente(String user, String pw) throws ExecutionException, InterruptedException {
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        name.add("username");
        name.add("password");

        value.add(user);
        value.add(pw);

        String mex = MessageBuilder.builder(name,value,value.size(),0);//dopo la richiesta post io devo far processare il risultato ottenendo un json object e prendere il valore booleano (restituito dal db server)di tale object e metterlo nell if

        boolean flag;

        try{
            String s = new PostRequest().execute(ip,"user/login",mex).get();
            Log.i("s",s);
            flag = Boolean.parseBoolean(s);
        }catch (Exception e){
            flag = false;
        }


        return flag;
    }

    public static void registrazioneUtente(){

    }

    public static HashMap<String, String>[] downloadMappe(){
        ArrayList<String> keys = new ArrayList<>();
        //TODO controllare ordine chiavi confrontandole con con il json inviato dal server
        keys.add("ID");
        keys.add("piano");
        keys.add("immagine");
        keys.add("nome");

        HashMap<String,String>[] mappe = new HashMap[0];
        try {
            mappe = MessageParser.analyzeMessageArray(new GetRequest().execute(hostMaster,"maps").get(),keys,"mappe");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return mappe;
    }

    public static void downloadImgMappa(String img){
        new DownloadFileFromURL().execute(ip, img);
    }

    public static HashMap<String, String>[] downloadBeacon(){
        ArrayList<String> keys = new ArrayList<>();
        //TODO controllare ordine chiavi confrontandole con con il json inviato dal server
        keys.add("ID");
        keys.add("address");
        keys.add("ID_PDI");
        keys.add("coordX");
        keys.add("coordY");
        keys.add("fire");
        keys.add("smoke");
        keys.add("LOS"); //TODO rivedere il discorso LOS
        keys.add("risk");

        HashMap<String,String>[] beacon = new HashMap[0];
        try {
            beacon = MessageParser.analyzeMessageArray(new GetRequest().execute(hostMaster,"beacon").get(),keys,"beacon");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return beacon;
    }

    public static HashMap<String, String>[] downloadTronchi(){
        ArrayList<String> keys = new ArrayList<>();
        //TODO controllare ordine chiavi confrontandole con con il json inviato dal server
        keys.add("ID");
        keys.add("nodo1");
        keys.add("nodo2");
        keys.add("beacon");
        keys.add("lunghezza");

        HashMap<String,String>[] tronchi = new HashMap[0];
        try {
            tronchi = MessageParser.analyzeMessageArray(new GetRequest().execute(hostMaster,"arcs").get(),keys,"tronchi");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return tronchi;
    }

    public static HashMap<String, String>[] downloadNodi(){
        ArrayList<String> keys = new ArrayList<>();
        //TODO controllare ordine chiavi confrontandole con con il json inviato dal server
        keys.add("ID");
        keys.add("ID_piano");
        keys.add("coordX");
        keys.add("coordY");
        keys.add("codice");
        keys.add("larghezza");
        keys.add("lunghezza");
        keys.add("is_pdi");
        keys.add("tipo");

        HashMap<String,String>[] nodi = new HashMap[0];
        try {
            nodi = MessageParser.analyzeMessageArray(new GetRequest().execute(hostMaster,"nodes").get(),keys,"nodi");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return nodi;
    }

    public static HashMap<String, String>[] downloadPiani(){
        ArrayList<String> keys = new ArrayList<>();
        //TODO controllare ordine chiavi confrontandole con con il json inviato dal server
        keys.add("ID");
        keys.add("nome");

        HashMap<String,String>[] piani = new HashMap[0];
        try {
            piani = MessageParser.analyzeMessageArray(new GetRequest().execute(hostMaster,"piani").get(),keys,"piani");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return piani;
    }

    public static void logoutUtente(){

    }

    public static boolean checkModifiche(String data){
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        name.add("data");

        value.add(data);

        String mex = MessageBuilder.builder(name,value,value.size(),0);//dopo la richiesta post io devo far processare il risultato ottenendo un json object e prendere il valore booleano (restituito dal db server)di tale object e metterlo nell if

        boolean flag;

        try{
            String s = new PostRequest().execute(ip,"checkmodifiche",mex).get();
            Log.i("s",s);
            flag = Boolean.parseBoolean(s);
        }catch (Exception e){
            flag = false;
        }


        return flag;
    }

    public static HashMap<String, String>[] downloadModifiche(String data){
        ArrayList<String> keys = new ArrayList<>();
        //TODO controllare ordine chiavi confrontandole con con il json inviato dal server
        keys.add("tabella");
        keys.add("tipo");
        keys.add("ID_oggetto");

        HashMap<String,String>[] modifiche = new HashMap[0];
        try {
            modifiche = MessageParser.analyzeMessageArray(new GetRequest().execute(hostMaster,"modifiche/"+data).get(),keys,"modifiche");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return modifiche;
    }

    //TODO metodo invio posizione utente
    @SuppressLint("WrongConstant")
    public static void sendPosition(String mex) throws ExecutionException, InterruptedException {

    }

    //TODO metodo invio valori beacon
    public static void sendValue(String mex) throws ExecutionException, InterruptedException {

    }
}
