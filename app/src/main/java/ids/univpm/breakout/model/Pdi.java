package ids.univpm.breakout.model;

import java.util.ArrayList;

/**
 *
 * @author costantino
 */
public class Pdi extends Nodo {
    private ArrayList<Beacon> beacon;
    private Integer[] beacon_Integer;
    private String tipo;
    private float lunghezza;

    public Pdi() {
    }

    public Integer[] getBeacon_Integer() {
        return beacon_Integer;
    }

    public void setBeacon_Integer(Integer[] beacon_Integer) {
        this.beacon_Integer = beacon_Integer;
    }

    public ArrayList<Beacon> getBeacon() {
        return beacon;
    }

    public void setBeacon(ArrayList<Beacon> beacon) {
        this.beacon = beacon;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(float lunghezza) {
        this.lunghezza = lunghezza;
    }

}