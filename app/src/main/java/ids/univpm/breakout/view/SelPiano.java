package ids.univpm.breakout.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ids.univpm.breakout.R;

class SelPiano extends AppCompatActivity {

    public TextView non_connesso_txt;
    public ImageView non_connesso;
    public TextView connesso_txt;
    public ImageView connesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation2bis);

        non_connesso_txt=(TextView) findViewById(R.id.non_connesso_txt);
        non_connesso= findViewById(R.id.non_connesso);
        connesso_txt=(TextView) findViewById(R.id.connesso_txt);
        connesso= findViewById(R.id.connesso);

        //Check connessione
        //se non connesso: Server connection: disconnected, simbolo X
        non_connesso.setVisibility(View.VISIBLE);
        non_connesso_txt.setVisibility(View.VISIBLE);

        // se connesso: connected: simbolo verde
        //connesso.setVisibility(View.VISIBLE);
        //connesso_txt.setVisibility(View.VISIBLE);



    }
}
