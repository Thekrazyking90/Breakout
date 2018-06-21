package ids.univpm.breakout.model;

public class Mappa {
    private Integer ID_mappa;
    private Integer ID_piano;
//    private Grafo grafo;
    private String nome;
    private String urlImmagine;

    public Mappa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImmagine() {
        return urlImmagine;
    }

    public void setUrlImmagine(String urlImmagine) {
        this.urlImmagine = urlImmagine;
    }

    public Integer getID_mappa() {
        return ID_mappa;
    }

    public void setID_mappa(Integer ID_mappa) {
        this.ID_mappa = ID_mappa;
    }

    public Integer getID_piano() {
        return ID_piano;
    }

    public void setID_piano(Integer ID_piano) {
        this.ID_piano = ID_piano;
    }

}