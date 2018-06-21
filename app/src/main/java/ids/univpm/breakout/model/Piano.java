package ids.univpm.breakout.model;

import java.util.ArrayList;

/**
 *
 * @author costantino
 */
public class Piano {
    private Integer ID_piano;
    private String quota;
    private ArrayList<Mappa> mappe;
    private Integer[] mappe_Integer;

    public Piano() {
    }

    public Integer[] getMappe_Integer() {
        return mappe_Integer;
    }

    public void setMappe_Integer(Integer[] mappe_Integer) {
        this.mappe_Integer = mappe_Integer;
    }

    public ArrayList<Mappa> getMappe() {
        return mappe;
    }

    public void setMappe(ArrayList<Mappa> mappe) {
        this.mappe = mappe;
    }

    public Integer getID_piano() {
        return ID_piano;
    }

    public void setID_piano(Integer ID_piano) {
        this.ID_piano = ID_piano;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }
}