package ids.univpm.breakout.model;

import java.util.Set;

public class Tronco {
    private long ID_tronco;
    private long ID_mappa;
    private double larghezza_media;
    private double lunghezza;
    private Set<Nodo> nodi;
    private Beacon beacon;
    private float costo_totale_normalizzato;

    public Tronco(){
    };

    public double getLarghezza_media() {
        return larghezza_media;
    }

    public void setLarghezza_media(double larghezza_media) {
        this.larghezza_media = larghezza_media;
    }

    public Set<Nodo> getNodi() {
        return nodi;
    }

    public void setNodi(Set<Nodo> nodi) {
        this.nodi = nodi;
    }

    public float getCosto_totale_normalizzato() {
        return costo_totale_normalizzato;
    }

    public void setCosto_totale_normalizzato(float costo_totale_normalizzato) {
        this.costo_totale_normalizzato = costo_totale_normalizzato;
    }

    public long getID_mappa() {
        return ID_mappa;
    }

    public void setID_mappa(long ID_mappa) {
        this.ID_mappa = ID_mappa;
    }

    public long getID_tronco() {
        return ID_tronco;
    }

    public void setID_tronco(long ID_tronco) {
        this.ID_tronco = ID_tronco;
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
}
