package ids.univpm.breakout.model;

public class Utente {
    private Integer ID_utente;
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private Integer ID_beacon;
    private int is_logged;

    public Utente() {
    }

    public Integer getID_utente() {
        return ID_utente;
    }

    public void setID_utente(Integer ID_utente) {
        this.ID_utente = ID_utente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Integer getID_beacon() {
        return ID_beacon;
    }

    public void setID_beacon(Integer ID_beacon) {
        this.ID_beacon = ID_beacon;
    }

    public int getIs_logged() {
        return is_logged;
    }

    public void setIs_logged(int is_logged) {
        this.is_logged = is_logged;
    }
}