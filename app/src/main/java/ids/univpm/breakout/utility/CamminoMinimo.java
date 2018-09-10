package ids.univpm.breakout.utility;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import ids.univpm.breakout.model.Nodo;
import ids.univpm.breakout.model.Scala;
import ids.univpm.breakout.model.database.Nodi.NodoManager;
import ids.univpm.breakout.model.database.Tronchi.TroncoManager;

/**
 *
 * @author costantino
 */
public class CamminoMinimo {

    private Context ctx;

    public CamminoMinimo(Context ctx) {
        this.ctx = ctx;
    }

    /** metodo che prende in input il nodo di destinazione e il PDI di partenza
     * e restituisce il cammino minimo
     *
     * @param destNodeId : ID del nodo di destinazione (Punti Di Interesse o nodo normale)
     * @param startPdiId : ID del punto di interesse di partenza
     * @return : Integer[] contenente tutti gli ID dei tronchi che descrivono il cammino minimo
     */
    public ArrayList<Integer> Dijkstra_PDI (Integer destNodeId, Integer startPdiId){

        HashMap<Integer,Costo_e_Cammino> dijkstraTemp = new HashMap<>(); // insieme temporaneo dei cammini minimi
        HashMap<Integer,Costo_e_Cammino> dijkstraDef = new HashMap<>(); // insieme definitivo dei cammini minimi

        Costo_e_Cammino cost_camm_dest = new Costo_e_Cammino(); //instanzio l'oggetto contenente il costo del cammino minimo e il cammino da associare al nodo di destinazione
        cost_camm_dest.setCosto(0.0f); //Inizializzo il costo del nodo di destinazione (che è ovviamente 0)

        Integer nodoTempId;
        ArrayList<Integer> camminoMinimo;

        dijkstraDef.put(destNodeId, cost_camm_dest); //Inserisco il nodo di destinazione nell'insieme definitivo
        dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, destNodeId); //Inserisco nell'insieme temporaneo i nodi adiacenti alla destinazione

        /*
        Finchè l'insieme definitivo non contiene il nodo di partenza, sposta il nodo
        di costo minimo nell'insieme definitivo e inserisce nell'insieme temporaneo
        i nodi ad esso adiacenti, non ancora presenti nè nell'insieme temporaneo nè
        in quello definitivo.
        */
        while(!dijkstraDef.containsKey(startPdiId)){
            nodoTempId = trovaCostoMin(dijkstraTemp);
            dijkstraDef.put(nodoTempId, dijkstraTemp.get(nodoTempId));
            dijkstraTemp.remove(nodoTempId);
            dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, nodoTempId);
        }

        camminoMinimo = dijkstraDef.get(startPdiId).getCammino();
        return camminoMinimo;
    }

    /**metodo che prende in input il nodo di destinazione e il tronco di partenza
     * e restituisce il cammino minimo
     *
     * @param destNodeCode : ID del nodo di destinazione
     * @param startArcId : ID del tronco di partenza
     * @return : Integer[] contenente tutti gli ID dei tronchi che descrivono il cammino minimo
     */
    public ArrayList<Integer> Dijkstra_Tronco (Integer destNodeCode, Integer startArcId){

        HashMap<Integer,Costo_e_Cammino> dijkstraTemp = new HashMap<>(); // insieme temporaneo dei cammini minimi
        HashMap<Integer,Costo_e_Cammino> dijkstraDef = new HashMap<>(); // insieme definitivo dei cammini minimi

        Costo_e_Cammino cost_camm_dest = new Costo_e_Cammino(); //instanzio l'oggetto contenente il costo del cammino minimo e il cammino da associare al nodo di destinazione
        cost_camm_dest.setCosto(0.0f); //Inizializzo il costo del nodo di destinazione (che è ovviamente 0)

        Integer nodoTempId;
        ArrayList<Integer> camminoMinimo;

        dijkstraDef.put(destNodeCode, cost_camm_dest);//Inserisco il nodo di destinazione nell'insieme definitivo
        dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, destNodeCode); //Inserisco nell'insieme temporaneo i nodi adiacenti alla destinazione

        TroncoManager troncoMng = new TroncoManager(ctx);
        Scala startArc = troncoMng.findByIdGeneric(startArcId);

        /*
        Finchè l'insieme definitivo non contiene tutti e due i nodi del tronco di partenza, sposta il nodo
        di costo minimo nell'insieme definitivo e inserisce nell'insieme temporaneo
        i nodi ad esso adiacenti, non ancora presenti nè nell'insieme temporaneo nè
        in quello definitivo.
        */
        while(!dijkstraDef.containsKey(startArc.getNodi_Integer()[0]) || !dijkstraDef.containsKey(startArc.getNodi_Integer()[1])){
            nodoTempId = trovaCostoMin(dijkstraTemp);
            dijkstraDef.put(nodoTempId, dijkstraTemp.get(nodoTempId));
            dijkstraTemp.remove(nodoTempId);
            dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, nodoTempId);
        }

        Integer startNodeId;
        /*
        calcolo il nodo del tronco con costo maggiore e lo considero come nodo di partenza
        */
        if(dijkstraDef.get(startArc.getNodi_Integer()[0]).getCosto() <= dijkstraDef.get(startArc.getNodi_Integer()[1]).getCosto()){
            startNodeId = startArc.getNodi_Integer()[1];
        }else{
            startNodeId = startArc.getNodi_Integer()[0];
        }

        camminoMinimo = dijkstraDef.get(startNodeId).getCammino();
        return camminoMinimo;
    }

    private HashMap<Integer,Costo_e_Cammino> impostaCostoECammino(HashMap<Integer,Costo_e_Cammino> dijkstraTemp, HashMap<Integer,Costo_e_Cammino> dijkstraDef, Integer nodoId){

        NodoManager nodoMng = new NodoManager(ctx);
        TroncoManager troncoMng = new TroncoManager(ctx);

        Nodo nodo = nodoMng.findById(nodoId);
        ArrayList<Scala> stella = new ArrayList<>();

        for (Integer i : nodo.getTronchi_stella_Integer()){
            Scala arc = troncoMng.findByIdGeneric(i);
            stella.add(arc);
        }

        for (Scala i: stella){
            Integer otherNodeId;
            otherNodeId = i.otherNode(nodoId);
            if (!dijkstraTemp.containsKey(otherNodeId) && !dijkstraDef.containsKey(otherNodeId)) {
                ArrayList<Integer> cammino = new ArrayList<>();
                Costo_e_Cammino cost_camm = new Costo_e_Cammino();
                Float costo;

                for (Integer j: dijkstraDef.get(nodoId).getCammino()) {
                    cammino.add(j);
                }


                cammino.add(i.getID());

                costo= i.getCosto_totale_normalizzato()+dijkstraDef.get(nodoId).getCosto();

                cost_camm.setCosto(costo);
                cost_camm.setCammino(cammino);

                dijkstraTemp.put(otherNodeId, cost_camm);
            }
        }
        return dijkstraTemp;
    }

    private Integer trovaCostoMin (HashMap<Integer,Costo_e_Cammino> dijkstra){
        Integer nodo_costo_min = null;

        Entry<Integer, Costo_e_Cammino> min = null;
        for (Entry<Integer, Costo_e_Cammino> entry : dijkstra.entrySet()) {
            if (min == null || min.getValue().getCosto() > entry.getValue().getCosto()) {
                min = entry;
            }
        }
        if(min != null) nodo_costo_min = min.getKey();

        return nodo_costo_min;
    }


}


class Costo_e_Cammino {
    private Float costo;
    private ArrayList<Integer> cammino;

    public Costo_e_Cammino() {
        cammino = new ArrayList<Integer>();
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public ArrayList<Integer> getCammino() {
        return cammino;
    }

    public void setCammino(ArrayList<Integer> cammino) {
        this.cammino = cammino;
    }
}
