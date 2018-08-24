package ids.univpm.breakout.model;

public class Modifica {
    private Integer ID;
    private String data;
    private String tipo;
    private String tabella;
    private Integer ID_ogg_mod;

    public Modifica(){

    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTabella() {
        return tabella;
    }

    public void setTabella(String tabella) {
        this.tabella = tabella;
    }

    public Integer getID_ogg_mod() {
        return ID_ogg_mod;
    }

    public void setID_ogg_mod(Integer ID_ogg_mod) {
        this.ID_ogg_mod = ID_ogg_mod;
    }
}
