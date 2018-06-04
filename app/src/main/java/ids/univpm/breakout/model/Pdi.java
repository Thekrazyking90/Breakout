package ids.univpm.breakout.model;

import java.util.ArrayList;

/**
 *
 * @author costantino
 */
public class Pdi extends Nodo {
    private ArrayList<Beacon> beacon;
    private Long[] beacon_long;
    private String tipo;
    private float lunghezza;

    public Pdi() {
    }

    public Long[] getBeacon_long() {
        return beacon_long;
    }

    public void setBeacon_long(Long[] beacon_long) {
        this.beacon_long = beacon_long;
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