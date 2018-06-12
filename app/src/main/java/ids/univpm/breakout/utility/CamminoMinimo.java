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
     * @return : Long[] contenente tutti gli ID dei tronchi che descrivono il cammino minimo
     */
    public Long[] Dijkstra_PDI (Long destNodeId, Long startPdiId){

        HashMap<Long,Costo_e_Cammino> dijkstraTemp = new HashMap<>(); // insieme temporaneo dei cammini minimi
        HashMap<Long,Costo_e_Cammino> dijkstraDef = new HashMap<>(); // insieme definitivo dei cammini minimi

        Costo_e_Cammino cost_camm_dest = new Costo_e_Cammino(); //instanzio l'oggetto contenente il costo del cammino minimo e il cammino da associare al nodo di destinazione
        cost_camm_dest.setCosto(0.0f); //Inizializzo il costo del nodo di destinazione (che è ovviamente 0)

        Long nodoTempId;
        Long[] camminoMinimo;

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
     * @return : Long[] contenente tutti gli ID dei tronchi che descrivono il cammino minimo
     */
    public Long[] Dijkstra_Tronco (Long destNodeCode, Long startArcId){

        HashMap<Long,Costo_e_Cammino> dijkstraTemp = new HashMap<>(); // insieme temporaneo dei cammini minimi
        HashMap<Long,Costo_e_Cammino> dijkstraDef = new HashMap<>(); // insieme definitivo dei cammini minimi

        Costo_e_Cammino cost_camm_dest = new Costo_e_Cammino(); //instanzio l'oggetto contenente il costo del cammino minimo e il cammino da associare al nodo di destinazione
        cost_camm_dest.setCosto(0.0f); //Inizializzo il costo del nodo di destinazione (che è ovviamente 0)

        Long nodoTempId;
        Long[] camminoMinimo;

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
        while(!dijkstraDef.containsKey(startArc.getNodi_long()[0]) || !dijkstraDef.containsKey(startArc.getNodi_long()[1])){
            nodoTempId = trovaCostoMin(dijkstraTemp);
            dijkstraDef.put(nodoTempId, dijkstraTemp.get(nodoTempId));
            dijkstraTemp.remove(nodoTempId);
            dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, nodoTempId);
        }

        Long startNodeId;
        /*
        calcolo il nodo del tronco con costo maggiore e lo considero come nodo di partenza
        */
        if(dijkstraDef.get(startArc.getNodi_long()[0]).getCosto() <= dijkstraDef.get(startArc.getNodi_long()[1]).getCosto()){
            startNodeId = startArc.getNodi_long()[1];
        }else{
            startNodeId = startArc.getNodi_long()[0];
        }

        camminoMinimo = dijkstraDef.get(startNodeId).getCammino();
        return camminoMinimo;
    }

    private HashMap<Long,Costo_e_Cammino> impostaCostoECammino(HashMap<Long,Costo_e_Cammino> dijkstraTemp, HashMap<Long,Costo_e_Cammino> dijkstraDef, Long nodoId){

        NodoManager nodoMng = new NodoManager(ctx);
        TroncoManager troncoMng = new TroncoManager(ctx);

        Nodo nodo = nodoMng.findById(nodoId);
        ArrayList<Scala> stella = new ArrayList<>();

        for (Long i : nodo.getTronchi_stella_long()){
            Scala arc = troncoMng.findByIdGeneric(i);
            stella.add(arc);
        }

        for (Scala i: stella){
            Long otherNodeId;
            otherNodeId = i.otherNode(nodoId);
            if (!dijkstraTemp.containsKey(otherNodeId) && !dijkstraDef.containsKey(otherNodeId)) {
                Long[] cammino;
                Costo_e_Cammino cost_camm = new Costo_e_Cammino();
                Float costo;

                cammino = dijkstraDef.get(nodoId).getCammino();

                cammino[cammino.length] = i.getID(); //TODO verificare se è giusto

                costo= i.getCosto_totale_normalizzato()+dijkstraDef.get(nodoId).getCosto();

                cost_camm.setCosto(costo);
                cost_camm.setCammino(cammino);

                dijkstraTemp.put(otherNodeId, cost_camm);
            }
        }
        return dijkstraTemp;
    }

    private Long trovaCostoMin (HashMap<Long,Costo_e_Cammino> dijkstra){
        Long nodo_costo_min = null;

        Entry<Long, Costo_e_Cammino> min = null;
        for (Entry<Long, Costo_e_Cammino> entry : dijkstra.entrySet()) {
            if (min == null || min.getValue().getCosto() > entry.getValue().getCosto()) {
                min = entry;
            }
        }
        if(min != null) nodo_costo_min = min.getKey();

        return nodo_costo_min;
    }


}


class Costo_e_Cammino {
    Float costo;
    Long[] cammino;

    public Costo_e_Cammino() {
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public Long[] getCammino() {
        return cammino;
    }

    public void setCammino(Long[] cammino) {
        this.cammino = cammino;
    }
}