package ids.univpm.breakout.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ids.univpm.breakout.R;
import ids.univpm.breakout.controller.Controller;
import ids.univpm.breakout.model.Mappa;

public class SelPiano extends AppCompatActivity {

    private ImageView non_connesso;
    private ImageView connesso;
    private TextView connection_status;
    private ArrayList<Mappa> listaMappe;
    private LinearLayout layout;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sel_piano);

        //Check connessione: di default disconnesso
        //se non connesso: Server connection: disconnected, simbolo X
        connection_status= findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        connesso= findViewById(R.id.connesso);

        if(Controller.checkConnection(SelPiano.this)) {
            non_connesso.setVisibility(View.INVISIBLE);
            connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Connected");
        }else{
            connesso.setVisibility(View.INVISIBLE);
            non_connesso.setVisibility(View.VISIBLE);
            connection_status.setText("Disconnected");
        }

        listaMappe = Controller.getMappe(getApplicationContext());
        layout = findViewById(R.id.buttons_layout);
        if(!listaMappe.isEmpty()){
            for (Mappa i: listaMappe){
                FrameLayout frame =new FrameLayout(this);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );
                layoutParams.setMargins(5,5,5,5);
                frame.setLayoutParams(layoutParams);

                ImageButton btn = new ImageButton(this);
                ViewGroup.LayoutParams btnParams = new ViewGroup.LayoutParams(
                        320,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                btn.setLayoutParams(btnParams);
                btn.setBackground(Drawable.createFromPath("@drawable/bottone_mappa"));
                btn.setPadding(10,10,10,10);
                btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                btn.setImageDrawable(Drawable.createFromPath(i.getUrlImmagine()));
                btn.setFocusable(true);

                TextView nome = new TextView(this);
                ViewGroup.LayoutParams nomeParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                nome.setGravity(Gravity.CENTER);
                nome.setLayoutParams(nomeParams);
                nome.setText(i.getNome());
                nome.setTextSize(50);
                nome.setTypeface(Typeface.DEFAULT_BOLD);

                final Integer idmap = i.getID_mappa();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelPiano.this, Navigation1.class);
                        intent.putExtra("id_mappa", idmap);
                        startActivity(intent);
                    }
                });
                frame.addView(btn);
                frame.addView(nome);

                layout.addView(frame);
            }
        }


    }
}