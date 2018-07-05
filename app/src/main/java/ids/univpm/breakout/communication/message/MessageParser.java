package ids.univpm.breakout.communication.message;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe scompone una stringa, con struttura JSON, restituendo una lista di coppie(chiave,valore)
 */

public class MessageParser {

    public static HashMap<String,String> messageElements;

    public static HashMap<String,String>[] array;

    /**
     * Attraverso le classi JSONObject di Android, siamo in grado di trasformare una stinga in un oggetto che dà
     * la possibilità di scorrerlo e costruire la lista che viene fornita in output.
     * @param s: messaggio da scomporre
     * @param keys lista di chiavi che saranno contenute all'interno del messaggio
     * @return la lista di coppie (k,v) che sono contenute all'interno del messaggio
     * @throws JSONException
     */
    public static HashMap<String,String> analyzeMessage(String s, ArrayList<String> keys) throws JSONException {
        messageElements = new HashMap<>();
        //messageElements.clear();
        Log.i("Messaggio da scomporre",s);
        JSONObject obj = new JSONObject(s);
        for(int k=0;k<keys.size();k++){
            try {
                messageElements.put(keys.get(k),obj.getString(keys.get(k)));
                Log.i("key and value :",keys.get(k)+" "+obj.getString(keys.get(k)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return messageElements;
    }

    /**
     *  Metodo molto simile a quello precendente ma che lavora con JSONArray, cioè array di JSONObject, quindi
     *  si scorre l'array si estraggono gli oggetti e si costruisce l'array di liste che rappresentano i differenti elementi
     *  del JSON.
     * @param s
     * @param keys
     * @param name
     * @return
     * @throws JSONException
     */
    public static HashMap<String,String>[] analyzeMessageArray(String s, ArrayList<String> keys, String name) throws JSONException {
        JSONObject json = new JSONObject(s);
        JSONArray jsonArray = json.getJSONArray(name);
        array = new HashMap[jsonArray.length()];

        JSONObject jsonobject;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonobject = jsonArray.getJSONObject(i);
            messageElements = new HashMap<>();
            messageElements.clear();
            for(int k=0;k<keys.size();k++){
                try {
                    messageElements.put(keys.get(k),jsonobject.getString(keys.get(k)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            array[i] = messageElements;
        }
        return array;
    }
}
