package ids.univpm.breakout.communication.emergenza;


/**
    Classe utilizzata per gestire le modalità di scan per la ricerca di beacon
 */

public class SetupE {
        //durate della pausa fra due scan consecutivi
    public static final long periodBetweenScan = 10000l;

        //flag che indica se vada effettuata o meno la connessione al beacon
    private boolean analyzeEmergency;

    /**
     * Costruttore il Setup per lo scan, con le impostazioni di default
     */
    public SetupE() {

        analyzeEmergency = true;
    }


    /**
     * Metodo che restituisce la durata della pausa fra due scan per il setup selezionato
     */
    public long getPeriodBetweenScan() {
        return periodBetweenScan;
    }

    /**
     * Metodo che restituisce il flag per sapere se si debba connettere
     */
    public boolean mustAnalyze() {
        return analyzeEmergency;
    }


}