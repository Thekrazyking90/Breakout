package ids.univpm.breakout.communication.http;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Classe che crea un server HTTP sulla porta 8888 che permette di ricevere notifiche dal server,
 * così da creare una doppia comunicazione.
 */

public class GetReceiver extends Thread {
    ServerSocket httpServerSocket;
    static final int HttpServerPORT = 8888;

    @Override
    public void run() {
        Socket socket = null;

        try {
            httpServerSocket = new ServerSocket(HttpServerPORT);

            while(true){
                socket = httpServerSocket.accept();
                HttpReceiverThread httpResponseThread =
                        new HttpReceiverThread(
                                socket);
                httpResponseThread.start();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }
    public boolean status()
    {
        if (httpServerSocket == null)
            return false;
        else
            return true;
    }
    public void closeConnection() throws IOException {
        try {
            httpServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
