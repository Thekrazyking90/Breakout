package ids.univpm.breakout.model;

public class Mappa {
    private long ID_mappa;
    private long ID_piano;
    private Grafo grafo;
    private String nome;
    private String urlImmagine;

    public Mappa() {
    }

    public long getID_mappa() {
        return ID_mappa;
    }

    public void setID_mappa(long ID_mappa) {
        this.ID_mappa = ID_mappa;
    }

    public long getID_piano() {
        return ID_piano;
    }

    public void setID_piano(long ID_piano) {
        this.ID_piano = ID_piano;
    }

}