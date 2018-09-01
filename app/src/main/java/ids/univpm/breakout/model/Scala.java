package ids.univpm.breakout.model;

import android.content.Context;

import java.util.ArrayList;

import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.database.Nodi.NodoManager;

/**
 *
 * @author costantino
 */
public class Scala {
    private Integer ID;
    private double larghezza_media;
    private double lunghezza;
    private ArrayList<Nodo> nodi;
    private Integer[] nodi_Integer;
    private Beacon beacon;
    private float costo_totale_normalizzato;

    public Scala() {
    }

    public Integer otherNode (Integer id){
        Integer other;
        Integer[] nodi = this.getNodi_Integer();
        if (id==nodi[0]){
            other=nodi[1];
        }else{
            other=nodi[0];
        }
    return other;
    }

    public Integer[] getNodi_Integer() {
        return nodi_Integer;
    }

    public void setNodi_Integer(Integer[] nodi_Integer) {
        this.nodi_Integer = nodi_Integer;
    }

    public ArrayList<Nodo> getNodi() {
        return nodi;
    }

    public void setNodi(ArrayList<Nodo> nodi) {
        this.nodi = nodi;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public double getLarghezza_media() {
        return larghezza_media;
    }

    public void setLarghezza_media(double larghezza_media) {
        this.larghezza_media = larghezza_media;
    }

    public void setLarghezza_media(Context ctx) {
        setNodi(ctx);
        this.larghezza_media = (nodi.get(0).getLarghezza() + nodi.get(1).getLarghezza()) / 2;
    }

    private void setNodi(Context ctx) {
        NodoManager nodoMng = new NodoManager(ctx);
        nodi.add(nodoMng.findById(nodi_Integer[0]));
        nodi.add(nodoMng.findById(nodi_Integer[1]));
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

    public void setCosto_totale_normalizzato() { //TODO da rivedere

        if (MainApplication.getScanner().getSetup().getState().equals("EMERGENCY")) {
            double LOS = beacon.getInd_NCD() / larghezza_media * lunghezza;
            costo_totale_normalizzato = (float) ((LOS * 0.7) + beacon.getInd_rischio() + beacon.getInd_fumi() + beacon.getInd_fuoco());
        }else{
            costo_totale_normalizzato = (float) lunghezza;
        }
    }
}