package ids.univpm.breakout.utility;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import ids.univpm.breakout.model.Nodo;
import ids.univpm.breakout.model.Pdi;
import ids.univpm.breakout.model.Scala;

/**
 *
 * @author costantino
 */
public class CamminoMinimo {

    /** metodo che prende in input il nodo di destinazione e il PDI di partenza
     * e restituisce il cammino minimo
     *
     * @param dest : nodo di destinazione (Punti Di Interesse o nodo normale)
     * @param partenza : punto di interesse di partenza
     * @return : ArrayList contenente tutti i tronchi che descrivono il cammino minimo
     */
    public static ArrayList<Scala> Dijkstra_PDI (Nodo dest, Pdi partenza){

        HashMap<Nodo,Costo_e_Cammino> dijkstraTemp = new HashMap<>(); // insieme temporaneo dei cammini minimi
        HashMap<Nodo,Costo_e_Cammino> dijkstraDef = new HashMap<>(); // insieme definitivo dei cammini minimi

        Costo_e_Cammino cost_camm_dest = new Costo_e_Cammino(); //instanzio l'oggetto contenente il costo del cammino minimo e il cammino da associare al nodo di destinazione
        cost_camm_dest.setCosto(0.0f); //Inizializzo il costo del nodo di destinazione (che è ovviamente 0)

        Nodo nodoTemp;
        ArrayList<Scala> camminoMinimo;

        dijkstraDef.put(dest, cost_camm_dest); //Inserisco il nodo di destinazione nell'insieme definitivo
        dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, dest); //Inserisco nell'insieme temporaneo i nodi adiacenti alla destinazione

        /*
        Finchè l'insieme definitivo non contiene il nodo di partenza, sposta il nodo
        di costo minimo nell'insieme definitivo e inserisce nell'insieme temporaneo
        i nodi ad esso adiacenti, non ancora presenti nè nell'insieme temporaneo nè
        in quello definitivo.
        */
        while(!dijkstraDef.containsKey(partenza)){
            nodoTemp = trovaCostoMin(dijkstraTemp);
            dijkstraDef.put(nodoTemp, dijkstraTemp.get(nodoTemp));
            dijkstraTemp.remove(nodoTemp);
            dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, nodoTemp);
        }

        camminoMinimo = dijkstraDef.get(partenza).getCammino();
        return camminoMinimo;
    }

    /**metodo che prende in input il nodo di destinazione e il tronco di partenza
     * e restituisce il cammino minimo
     *
     * @param dest : nodo di destinazione (Punti Di Interesse o nodo normale)
     * @param partenza : tronco di partenza (è di tipo Scala perchè Tronco estende Scala)
     * @return : ArrayList contenente tutti i tronchi che descrivono il cammino minimo
     */
    public static ArrayList<Scala> Dijkstra_Tronco (Nodo dest, Scala partenza){

        HashMap<Nodo,Costo_e_Cammino> dijkstraTemp = new HashMap<>(); // insieme temporaneo dei cammini minimi
        HashMap<Nodo,Costo_e_Cammino> dijkstraDef = new HashMap<>(); // insieme definitivo dei cammini minimi

        Costo_e_Cammino cost_camm_dest = new Costo_e_Cammino(); //instanzio l'oggetto contenente il costo del cammino minimo e il cammino da associare al nodo di destinazione
        cost_camm_dest.setCosto(0.0f); //Inizializzo il costo del nodo di destinazione (che è ovviamente 0)

        Nodo nodoTemp;
        ArrayList<Scala> camminoMinimo;

        dijkstraDef.put(dest, cost_camm_dest);//Inserisco il nodo di destinazione nell'insieme definitivo
        dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, dest); //Inserisco nell'insieme temporaneo i nodi adiacenti alla destinazione

        /*
        Finchè l'insieme definitivo non contiene tutti e due i nodi del tronco di partenza, sposta il nodo
        di costo minimo nell'insieme definitivo e inserisce nell'insieme temporaneo
        i nodi ad esso adiacenti, non ancora presenti nè nell'insieme temporaneo nè
        in quello definitivo.
        */
        while(!dijkstraDef.containsKey(partenza.getNodi().get(0)) || !dijkstraDef.containsKey(partenza.getNodi().get(1))){
            nodoTemp = trovaCostoMin(dijkstraTemp);
            dijkstraDef.put(nodoTemp, dijkstraTemp.get(nodoTemp));
            dijkstraTemp.remove(nodoTemp);
            dijkstraTemp = impostaCostoECammino(dijkstraTemp, dijkstraDef, nodoTemp);
        }

        Nodo nodoPartenza;
        /*
        calcolo il nodo del tronco con costo maggiore e lo considero come nodo di partenza
        */
        if(dijkstraDef.get(partenza.getNodi().get(0)).getCosto() <= dijkstraDef.get(partenza.getNodi().get(1)).getCosto()){
            nodoPartenza = partenza.getNodi().get(1);
        }else{
            nodoPartenza = partenza.getNodi().get(0);
        }

        camminoMinimo = dijkstraDef.get(nodoPartenza).getCammino();
        return camminoMinimo;
    }

    private static HashMap<Nodo,Costo_e_Cammino> impostaCostoECammino(HashMap<Nodo,Costo_e_Cammino> dijkstraTemp, HashMap<Nodo,Costo_e_Cammino> dijkstraDef, Nodo nodo){

        for (Scala i: nodo.getTronchi_stella()){
            Nodo other;
            other = i.otherNode(nodo);
            if (!dijkstraTemp.containsKey(other) && !dijkstraDef.containsKey(other)) {
                ArrayList<Scala> cammino;
                Costo_e_Cammino cost_camm = new Costo_e_Cammino();
                Float costo;

                cammino = dijkstraDef.get(nodo).getCammino();
                cammino.add(i);

                costo= i.getCosto_totale_normalizzato()+dijkstraDef.get(nodo).getCosto();

                cost_camm.setCosto(costo);
                cost_camm.setCammino(cammino);

                dijkstraTemp.put(other, cost_camm);
            }
        }
        return dijkstraTemp;
    }

    private static Nodo trovaCostoMin (HashMap<Nodo,Costo_e_Cammino> dijkstra){
        Nodo nodo_costo_min = new Nodo();

        Entry<Nodo, Costo_e_Cammino> min = null;
        for (Entry<Nodo, Costo_e_Cammino> entry : dijkstra.entrySet()) {
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
    ArrayList<Scala> cammino;

    public Costo_e_Cammino() {
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public ArrayList<Scala> getCammino() {
        return cammino;
    }

    public void setCammino(ArrayList<Scala> cammino) {
        this.cammino = cammino;
    }
}