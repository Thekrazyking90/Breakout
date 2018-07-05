package ids.univpm.breakout.communication.http;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import ids.univpm.breakout.controller.MainApplication;


/**
 * Classe che implementa una HTTP GET, impacchetta un messaggio passandolo come parametro
 * L'url della risorsa sarà http://[ipserver]:8080/[uri della risorsa]
 */

public class GetRequest extends AsyncTask<String,Void,String> {

    private String json;

    private static final String PORT = "8080";

    private static final String SERVER_ID = "RestfulServerTID";

    private URL url;

    private HttpURLConnection connection;

    /**
     * La richiesta viene effettuata attraverso un oggetto HttpURLConnection che permette di costruire
     * una connessione utilizzando il protocollo HTTP.
     * @param urls
     * @return un stringa che è un documento JSON che rappresenta la risposta del server
     */


    @Override
    protected String doInBackground(String... urls) {


        url = null;
        try {
            url = new URL("http://" + urls[0] + ":" + PORT + "/" + SERVER_ID + "/" + urls[1]);
            Log.i("URL","url: " + url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        }catch (SocketTimeoutException e1){
            Toast.makeText(MainApplication.getCurrentActivity().getApplicationContext(), "Connessione al server scaduta, riavviare l'applicazione", Toast.LENGTH_SHORT).show();
            MainApplication.setOnlineMode(false);
            Log.e("errore","sessione scaduta");
        } catch (IOException e) {
            e.printStackTrace();
        }

        connection.setConnectTimeout(2000);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        try {
            //se la comunicazione è andata a buon fine, il server risponderà con il codice 200, quindi potremmo
            //prendere il messaggio di risposta e restituirlo come output
            if (connection.getResponseCode() == 200) {
                InputStreamReader is = new InputStreamReader(connection.getInputStream());
                BufferedReader read = new BufferedReader(is);
                String s = null;
                StringBuffer sb = new StringBuffer();
                try {
                    while ((s = read.readLine()) != null) {
                        sb.append(s);
                    }
                    read.close();
                    json = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    is.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection!=null) connection.disconnect();
        }

        return json;
    }

}
