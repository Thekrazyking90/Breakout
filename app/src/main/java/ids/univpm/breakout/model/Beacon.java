package ids.univpm.breakout.model;

public class Beacon {

    private long ID_beacon;
    private float coord_X;
    private float coord_Y;
    private float ind_fuoco;
    private float ind_fumi;
    private float ind_LOS;
    private float ind_rischio;
    private long ID_pdi;

    public Beacon() {
    }

    public long getID_pdi() {
        return ID_pdi;
    }

    public void setID_pdi(long ID_pdi) {
        this.ID_pdi = ID_pdi;
    }

    public long getID_beacon() {
        return ID_beacon;
    }

    public void setID_beacon(long ID_beacon) {
        this.ID_beacon = ID_beacon;
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

    public float getInd_fuoco() {
        return ind_fuoco;
    }

    public void setInd_fuoco(float ind_fuoco) {
        this.ind_fuoco = ind_fuoco;
    }

    public float getInd_fumi() {
        return ind_fumi;
    }

    public void setInd_fumi(float ind_fumi) {
        this.ind_fumi = ind_fumi;
    }

    public float getInd_LOS() {
        return ind_LOS;
    }

    public void setInd_LOS(float ind_LOS) {
        this.ind_LOS = ind_LOS;
    }

    public float getInd_rischio() {
        return ind_rischio;
    }

    public void setInd_rischio(float ind_rischio) {
        this.ind_rischio = ind_rischio;
    }
}