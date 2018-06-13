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
    private Long[] nodi_long;
    private Beacon beacon;
    private float costo_totale;

    public Scala() {
    }

    public Long otherNode (Long id){
        Long other = null;
        Long[] nodi = this.getNodi_long();
        if (id==nodi[0]){
            other=nodi[1];
        }else{
            other=nodi[0];
        }
    return other;
    }

    public Long[] getNodi_long() {
        return nodi_long;
    }

    public void setNodi_long(Long[] nodi_long) {
        this.nodi_long = nodi_long;
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

    public float getCosto_totale() {
        return costo_totale;
    }

    public void setCosto_totale(float costo_totale) {
        this.costo_totale = costo_totale;
    }

    public void setCosto_totale() {
        double LOS = larghezza_media * lunghezza / beacon.getInd_NDC();
        costo_totale = (float) (LOS + beacon.getInd_rischio() + beacon.getInd_fumi() + beacon.getInd_fuoco());
    }
}