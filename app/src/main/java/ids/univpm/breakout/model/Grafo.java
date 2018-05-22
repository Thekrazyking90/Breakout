package ids.univpm.breakout.model;

import java.util.ArrayList;

/**
 *
 * @author costantino
 */
class Grafo {
    private ArrayList<Nodo> nodi;
    private ArrayList<Scala> tronchi;

    public Grafo (){
    }

    public ArrayList<Nodo> getNodi() {
        return nodi;
    }

    public void setNodi(ArrayList<Nodo> nodi) {
        this.nodi = nodi;
    }

    public ArrayList<Scala> getTronchi() {
        return tronchi;
    }

    public void setTronchi(ArrayList<Scala> tronchi) {
        this.tronchi = tronchi;
    }
}
