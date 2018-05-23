package ids.univpm.breakout.model;

import java.util.ArrayList;

/**
 *
 * @author costantino
 */
public class Scala {
    private long ID;
    private double larghezza_media;
    private double lunghezza;
    private ArrayList<Nodo> nodi;
    private Beacon beacon;
    private float costo_totale_normalizzato;

    public Scala() {
    }

    public Nodo otherNode (Nodo node){
        Nodo other = null;
        ArrayList<Nodo> i = this.getNodi();

        for(Nodo f:i){
            if (f.equals(node)){
            } else {
                other = (Nodo) f;
            }
        }
    return other;
    }

    public ArrayList<Nodo> getNodi() {
        return nodi;
    }

    public void setNodi(ArrayList<Nodo> nodi) {
        this.nodi = nodi;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public double getLarghezza_media() {
        return larghezza_media;
    }

    public void setLarghezza_media(double larghezza_media) {
        this.larghezza_media = larghezza_media;
    }

    public double getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(double lunghezza) {
        this.lunghezza = lunghezza;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public float getCosto_totale_normalizzato() {
        return costo_totale_normalizzato;
    }

    public void setCosto_totale_normalizzato(float costo_totale_normalizzato) {
        this.costo_totale_normalizzato = costo_totale_normalizzato;
    }
}