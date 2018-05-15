package ids.univpm.breakout.model;

import java.util.Set;

public class Nodo {
    private long ID_nodo;
    private String codice;
    private float larghezza;
    private float coord_X;
    private float coord_Y;
    private Set<Tronco> tronchi_stella;
    private long ID_piano;

    public Nodo() {
    }

    public long getID_piano() {
        return ID_piano;
    }

    public void setID_piano(long ID_piano) {
        this.ID_piano = ID_piano;
    }

    public float getLarghezza() {
        return larghezza;
    }

    public void setLarghezza(float larghezza) {
        this.larghezza = larghezza;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Set<Tronco> getTronchi_stella() {
        return tronchi_stella;
    }

    public void setTronchi_stella(Set<Tronco> tronchi_stella) {
        this.tronchi_stella = tronchi_stella;
    }

    public long getID_nodo() {
        return ID_nodo;
    }

    public void setID_nodo(long ID_nodo) {
        this.ID_nodo = ID_nodo;
    }

    public float getCoord_X() {
        return coord_X;
    }

    public void setCoord_X(float coord_X) {
        this.coord_X = coord_X;
    }

    public float getCoord_Y() {
        return coord_Y;
    }

    public void setCoord_Y(float coord_Y) {
        this.coord_Y = coord_Y;
    }

}
