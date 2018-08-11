package ids.univpm.breakout.communication;


import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ids.univpm.breakout.communication.http.GetRequest;
import ids.univpm.breakout.communication.http.PostRequest;
import ids.univpm.breakout.communication.message.MessageBuilder;
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Utente;
import ids.univpm.breakout.model.database.DBHelper;
import ids.univpm.breakout.model.database.Utente.UtenteManager;

public class Server {

    private static String ip = ""; //TODO da inserire ip
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

        if (Boolean.parseBoolean(new PostRequest().execute(hostMaster,"user/login",mex).get())){

            flag = true;
        }
        else flag = false;

        return flag;
    }

    void registrazioneUtente(){

    }

    void downloadMappe(){

    }

    void logoutUtente(){

    }

    void downloadModifiche(){

    }

    //TODO metodo invio posizione utente
    @SuppressLint("WrongConstant")
    public static void sendPosition(String mex) throws ExecutionException, InterruptedException {

    }

    //TODO metodo invio valori beacon
    public static void sendValue(String mex) throws ExecutionException, InterruptedException {

    }
}
