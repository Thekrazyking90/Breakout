package ids.univpm.breakout.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ids.univpm.breakout.R;

class SelPiano extends AppCompatActivity {

    public ImageView non_connesso;
    public ImageView connesso;
    public TextView connection_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sel_piano);

        //Check connessione: di default disconnesso
        //se non connesso: Server connection: disconnected, simbolo X
        connection_status=(TextView) findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        non_connesso.setVisibility(View.VISIBLE);

        // se connesso: connected: simbolo verde
        connesso= findViewById(R.id.connesso);
        //connesso.setVisibility(View.VISIBLE);
        //non_connesso.setVisibility(View.INVISIBLE);
        //connection_status.setText("Connected");


    }
}
