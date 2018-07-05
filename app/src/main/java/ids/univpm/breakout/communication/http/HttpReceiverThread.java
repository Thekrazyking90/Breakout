package ids.univpm.breakout.communication.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import ids.univpm.breakout.communication.DataListener;

/**
 * Created by picci on 09/05/2017.
 */

public class HttpReceiverThread extends Thread implements DataListener {

    private Socket socket;
    private String notifies;
    private final ArrayList<String> notifyKeys = new ArrayList<String>(){{
        add("id");
        add("cod_cat");
        add("floor");
        add("room");
    }};

    HttpReceiverThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader is;
        PrintWriter os;
        String line;

        //vado a iscrivere tale classe alla struttura dati Notification come subscriber
        // Data.getNotification().addDataListener(this);


        try {
            //Quando arriva la richesta da parte del server di connessione http per inviare le notifiche,
            //l'applicazione apre un canale in ingresso e inizia a leggere la stringa che rappresenta
            //le notifiche scritte sotto forma di documento JSON
            InputStreamReader lettore = new InputStreamReader(socket.getInputStream());
            is = new BufferedReader(lettore);
            line = is.readLine();
            StringBuilder raw = new StringBuilder();
            raw.append("" + line);
            boolean isPost = line.startsWith("POST");
            int contentLength = 0;
            String request;


            while (!(line = is.readLine()).equals("")) {
                raw.append('\n' + line);
                if (isPost) {
                    final String contentHeader = "Content-Length: ";
                    if (line.startsWith(contentHeader)) {
                        contentLength = Integer.parseInt(line.substring(contentHeader.length()));
                    }
                }
            }
            StringBuilder body = new StringBuilder();
            if (isPost) {
                int c = 0;
                for (int i = 0; i < contentLength; i++) {
                    c = is.read();
                    if(c>20)
                        body.append( (char) c);
                }
            }
            Log.i("POST: ", body.toString());
            notifies = body.toString();

            //quando la lettura è terminata viene richiamata la update della struttura dati
            //notification che va ad avvertire tutti i subscriber che la struttura dati è stata
            // modificata, all'evento update sulla struttura verranno chiamati tutti i metodi retrive dei
            // sottoscrittori che aggiornanno la loro versione
            update();

            os = new PrintWriter(socket.getOutputStream(), true);

            String response =
                    "<html><head></head>" +
                            "<body>" +
                            "<h1>" + "INVIO RIUSCITO" + "</h1>" +
                            "</body></html>";

            os.print("HTTP/1.0 200" + "\r\n");
            os.print("Content type: text/html" + "\r\n");
            os.print("Content length: " + response.length() + "\r\n");
            os.print("\r\n");
            os.print(response + "\r\n");
            os.flush();
            socket.close();


        } catch (IOException e) {

            e.printStackTrace();
        }

        return;
    }

    /**
     * Il metodo descritto è un'implementazione del medesimo metodo dell'interfaccia DataListener.
     * Presa la stringa, risultato dell'invio della notifica da parte del server, la si va a scomporre,
     * mediante un metodo della classe MessageParser che estrarrà tutte le notifiche, esse verranno costruite
     * e inserite all'interno di un arrayList, poi memorizzato dentro la struttura dati condivisa.
     *
     */
    @Override
    public void update() {
       /* HashMap<String,String>[] not;
        try {
            not = MessageParser.analyzeMessageArray(notifies,notifyKeys,"notifications");
            ArrayList<Notify> n = new ArrayList<>();
            for(int i = 0;i<not.length;i++){
                n.add(new Notify(Integer.parseInt(not[i].get("id")),
                                 Integer.parseInt(not[i].get("cod_cat")),
                                 not[i].get("floor"),
                                 not[i].get("room")));
            }
            //controllo se ci sono notifiche se è vero vado in modalita emergenza, in caso contrario uscirò da tale modalità
            Log.e("Lunghezza not ",not.length+"");
            if(not.length==0){
                if (MainApplication.getEmergency()) MainApplication.setEmergency(false);
                if (!MainApplication.getVisible()) MainApplication.deleteNotification();
                Data.getNotification().getNotifies().clear();
            }
            else if (not.length>0 && !MainApplication.getEmergency()){
                if(MainApplication.getVisible()) {
                    MainApplication.setEmergency(true);
                }
                else {
                    MainApplication.launchNotification();
                }
                Data.getNotification().setNotifies(n);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void retrive() {

    }
}

