package ids.univpm.breakout.communication.message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Classe che costruisce dei messaggi con struttura json, prendendo come parametro la lista di coppia (chiave,valore)
 */

public class MessageBuilder {

    private static JSONObject jsonObject;
    private static JSONArray jsonArray;

    /**
     * Questa classe costruisce un messaggio che dovra' essere inviato al server prendendo dei valori,
     * costruendo il JSON, restituendo poi una stringa.
     *
     * @param keys   arraylist di chiavi
     * @param values arraylist di valori
     * @param elements indica il numero di elementi (quando arrays ==0, esso coincide con la lunghezza delle liste)
     * @param arrays indica il numero di oggetti che devo creare (se è == i la lunghezza delle liste sara' pari a i*elements)
     * @return ritorna una stringa che e' il messaggio json
     */
    public static String builder(ArrayList<String> keys, ArrayList<String> values, int elements, int arrays){
        if(arrays==0) {
            //se deco costruire un messaggio con un unico oggetto

            if (!keys.isEmpty() && !values.isEmpty() && keys != null && values != null) {
                //se nessuno dei due è vuoto possiamo costruire il messaggio
                jsonObject = new JSONObject();
                for (int i = 0; i < elements; i++) {

                    try {
                        jsonObject.put(keys.get(i), values.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return jsonObject.toString();
            }
                //se  sono vuote o nulle una delle due liste o entrambe
                return null;
        }else {
            //se devo costruire un array
            jsonArray = new JSONArray();
            for(int k = 0;k<arrays;k++){
                if (!keys.isEmpty() && !values.isEmpty() && keys != null && values != null) {
                    //se nessuno dei due è vuoto possiamo costruire il messaggio
                    jsonObject = new JSONObject();
                    for (int i = 0; i < elements; i++) {

                        try {
                            jsonObject.put(keys.get(i+k*elements), values.get(i+k*elements));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    jsonArray.put(jsonObject);

                }
                //se  sono vuote o nulle una delle due liste o entrambe
                return null;
            }

        }
        return jsonArray.toString();
    }
}
