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
import ids.univpm.breakout.communication.http.PutRequest;
import ids.univpm.breakout.communication.message.MessageBuilder;
import ids.univpm.breakout.communication.message.MessageParser;

public class Server {

    private static String ip = "192.168.1.65"; //ip del server
    private static String hostMaster = "Breakout_server"; //= hostname;
    private static SQLiteDatabase db;

    public static boolean handShake() {
        boolean b;
        try {
            String s = new GetRequest().execute(ip,"resources/utility/testconnection").get();
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
            String s = new PostRequest().execute(ip,"resources/user/login",mex).get();
            Log.i("s",s);
            flag = Boolean.parseBoolean(s);
        }catch (Exception e){
            flag = false;
        }


        return flag;
    }

    public static boolean registrazioneUtente(String username,
                                           String password,
                                           String email,
                                           String nome,
                                           String cognome){
        boolean flag;

        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        name.add("username");
        name.add("password");
        name.add("email");
        name.add("nome");
        name.add("cognome");

        value.add(username);
        value.add(password);
        value.add(email);
        value.add(nome);
        value.add(cognome);

        String mex = MessageBuilder.builder(name,value,value.size(),0);

        try{
            String s = new PostRequest().execute(ip,"resources/utility/registraUser",mex).get();
            Log.i("s",s);
            flag = Boolean.parseBoolean(s);
        }catch (Exception e){
            flag = false;
        }

        return flag;
    }

    public static HashMap<String, String>[] downloadMappe(){
        ArrayList<String> keys = new ArrayList<>();

        keys.add("ID_mappa");
        keys.add("ID_piano");
        keys.add("immagine");
        keys.add("nome");

        HashMap<String,String>[] mappe = null;
        try {
            mappe = MessageParser.analyzeMessageArray(new GetRequest().execute(ip,"resources/maps/getmaps").get(),keys);
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

        keys.add("ID_beacon");
        keys.add("address");
        keys.add("ID_pdi");
        keys.add("coord_X");
        keys.add("coord_Y");
        keys.add("ind_fuoco");
        keys.add("ind_fumi");
        keys.add("ind_NCD");
        keys.add("ind_rischio");
        keys.add("ID_mappa");
        keys.add("ID_piano");


        HashMap<String,String>[] beacon = null;
        try {
            beacon = MessageParser.analyzeMessageArray(new GetRequest().execute(ip,"resources/maps/beacon").get(),keys);
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

        keys.add("ID");
        //keys.add("nodo1");
        //keys.add("nodo2");
        keys.add("nodi_int");
        keys.add("ID_beacon");
        keys.add("lunghezza");
        keys.add("ID_mappa");
        keys.add("ID_piano");

        HashMap<String,String>[] tronchi = null;
        try {
            tronchi = MessageParser.analyzeMessageArray(new GetRequest().execute(ip,"resources/maps/arcs").get(),keys);
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

        keys.add("ID");
        keys.add("ID_mappa");
        keys.add("coord_X");
        keys.add("coord_Y");
        keys.add("codice");
        keys.add("larghezza");
        keys.add("lunghezza");
        keys.add("isPdi");
        keys.add("tipo");

        HashMap<String,String>[] nodi = null;
        try {
            nodi = MessageParser.analyzeMessageArray(new GetRequest().execute(ip,"resources/maps/nodes").get(),keys);
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

        keys.add("ID_piano");
        keys.add("quota");

        HashMap<String,String>[] piani = null;
        try {
            piani = MessageParser.analyzeMessageArray(new GetRequest().execute(ip,"resources/maps/floors").get(),keys);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return piani;
    }

    public static boolean logoutUtente(String user){
    	ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        name.add("username");

        value.add(user);

        String mex = MessageBuilder.builder(name,value,value.size(),0);

        boolean flag;

        try{
            String s = new PostRequest().execute(ip,"resources/user/logout",mex).get();
            Log.i("s",s);
            flag = Boolean.parseBoolean(s);
        }catch (Exception e){
            flag = false;
        }


        return flag;
        //TODO modificare chi utilizza questo metodo
    }

    public static boolean checkModifiche(String data){
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        name.add("data");

        value.add(data);

        String mex = MessageBuilder.builder(name,value,value.size(),0);

        boolean flag;

        try{
            String s = new PostRequest().execute(ip,"resources/utility/checkmodifiche",mex).get();
            Log.i("s",s);
            flag = Boolean.parseBoolean(s);
        }catch (Exception e){
            flag = false;
        }


        return flag;
    }

    public static HashMap<String, String>[] downloadModifiche(String data){
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        name.add("data");

        value.add(data);
        
        String mex = MessageBuilder.builder(name,value,value.size(),0);

        keys.add("tabella");
        keys.add("tipo");
        keys.add("ID_oggetto");

        HashMap<String,String>[] modifiche = null;
        try {
            modifiche = MessageParser.analyzeMessageArray(new PostRequest().execute(ip,"resources/utility/getmodifiche", mex).get(), keys);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return modifiche;
    }

    public static void sendPosition(String mex) throws ExecutionException, InterruptedException {
        new PutRequest().execute(ip,"resources/utility/setposition",mex).get();
    }

    public static void sendValue(String mex) throws ExecutionException, InterruptedException {
        new PutRequest().execute(ip,"resources/utility/beaconvalues",mex).get();
    }

    //metodo per controllare se lo username scritto sulla pagina di registrazione è già in uso
    public static boolean checkUsername(String username) {
        boolean flag;

        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        name.add("username");

        value.add(username);

        String mex = MessageBuilder.builder(name,value,value.size(),0);

        try{
            String s = new PostRequest().execute(ip,"resources/utility/checkusername",mex).get();
            Log.i("s",s);
            flag = Boolean.parseBoolean(s);
        }catch (Exception e){
            flag = false;
        }

        return flag;
    }

    public static boolean checkEmergenze() {
        boolean b;
        try {
            String s = new GetRequest().execute(ip,"resources/utility/emergency").get();
            Log.i("s",s);
            b = Boolean.parseBoolean(s);
        } catch (Exception e) {
            b = false;
        }
        return b;
    }
}
