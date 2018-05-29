package ids.univpm.breakout.controller;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.database.Mappa.MappaManager;

import static android.content.Context.DOWNLOAD_SERVICE;


public class Controller extends AppCompatActivity {
    //controllo scaricamento mappe?
    //controllo primo accesso
    //Download mappe dal server
    // Se già presenti, fare check se uguali

    public void controlloMappeServer(View v){

        //  CONTROLLA SE LE MAPPE SONO O NO SUL DISPOSITIVO. SE NO LE SCARICA
        // IMPLEMENTARE ULTERIORE COMANDO DI AGGIORNAMENTO: VEDE SE LE MAPPE SONO OBSOLETE
        // quando l'utente preme ENTRA scarica le mappe -> nella classe Login è aggiunta l'azione

        try {
            URL url = new URL(" <jdbc:derby://localhost:1527/breakout> ");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } //Serve la connessione o no? nel tutorial non c'è...
    }




}

