package ids.univpm.breakout.model;

import java.util.ArrayList;

/**
 *
 * @author costantino
 */
public class Pdi extends Nodo {
    private ArrayList<Beacon> beacon;
    private String tipo;
    private String descrizione;
    private float lunghezza;

    public Pdi() {
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public float getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(float lunghezza) {
        this.lunghezza = lunghezza;
    }

}