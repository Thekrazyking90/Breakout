package ids.univpm.breakout.model;

public class Beacon {

    private Integer ID_beacon;
    private Integer ID_map;
    private Integer ID_floor;
    private String address;
    private float coord_X;
    private float coord_Y;
    private float ind_fuoco;
    private float ind_fumi;
    private float ind_NCD;
    private float ind_rischio;
    private Integer ID_pdi;

    public Beacon() {
    }

    public Integer getID_map() {
        return ID_map;
    }

    public void setID_map(Integer ID_map) {
        this.ID_map = ID_map;
    }

    public Integer getID_floor() {
        return ID_floor;
    }

    public void setID_floor(Integer ID_floor) {
        this.ID_floor = ID_floor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getID_pdi() {
        return ID_pdi;
    }

    public void setID_pdi(Integer ID_pdi) {
        this.ID_pdi = ID_pdi;
    }

    public Integer getID_beacon() {
        return ID_beacon;
    }

    public void setID_beacon(Integer ID_beacon) {
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

    public float getInd_NCD() {
        return ind_NCD;
    }

    public void setInd_NCD(float ind_NCD) {
        this.ind_NCD = ind_NCD;
    }

    public float getInd_rischio() {
        return ind_rischio;
    }

    public void setInd_rischio(float ind_rischio) {
        this.ind_rischio = ind_rischio;
    }
}