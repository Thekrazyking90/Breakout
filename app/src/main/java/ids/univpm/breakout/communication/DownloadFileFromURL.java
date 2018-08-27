package ids.univpm.breakout.communication;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    private static final String PORT = "8080";

    private static final String SERVER_ID = "Breakout_server";

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL("http://" + f_url[0] + ":" + PORT + "/immagini/" + f_url[1]);
            URLConnection connection = url.openConnection();
            connection.connect();


            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            //creazione directory
            File directory = new File(Environment.getExternalStorageDirectory()+ File.separator+"Breakout/ImmaginiMappe");

            if(!directory.isDirectory())
                directory.mkdirs();

            // Output stream
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory()
                    + "/Breakout/ImmaginiMappe/" + f_url[1]);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            Log.e("url", "http://" + f_url[0] + ":" + PORT + "/images/" + f_url[1]);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

}
