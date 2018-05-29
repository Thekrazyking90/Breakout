package ids.univpm.breakout.model;

public class Utente {
    private long ID_utente;
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private String ultima_posizione;
    private boolean is_logged;

    public Utente() {
    }

    public long getID_utente() {
        return ID_utente;
    }

    public void setID_utente(long ID_utente) {
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

    public String getUltima_posizione() {
        return ultima_posizione;
    }

    public void setUltima_posizione(String ultima_posizione) {
        this.ultima_posizione = ultima_posizione;
    }

    public boolean isIs_logged() {
        return is_logged;
    }

    public void setIs_logged(boolean is_logged) {
        this.is_logged = is_logged;
    }
}