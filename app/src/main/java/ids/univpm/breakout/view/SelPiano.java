package ids.univpm.breakout.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
import ids.univpm.breakout.controller.MainApplication;
import ids.univpm.breakout.model.Mappa;
import ids.univpm.breakout.model.database.Mappa.MappaManager;

public class SelPiano extends AppCompatActivity {

    private ImageView non_connesso;
    private ImageView connesso;
    private TextView connection_status;
    private ArrayList<Mappa> listaMappe;
    private LinearLayout layout;
    private ImageButton bottone145, bottone150, bottone155;

    public int convertDpToPixel(int dp){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * (int)((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.setCurrentActivity(this);

        setContentView(R.layout.sel_piano);

        //Check connessione: di default disconnesso
        //se non connesso: Server connection: disconnected, simbolo X
        connection_status= findViewById(R.id.connection_status);
        non_connesso= findViewById(R.id.non_connesso);
        connesso= findViewById(R.id.connesso);

        Controller.checkConnection(SelPiano.this);

        if(MainApplication.getOnlineMode()) {
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

        ImageButton bottone145 =  findViewById(R.id.button145);
        ImageButton bottone150 =  findViewById(R.id.button150);
        ImageButton bottone155 =  findViewById(R.id.button155);


        //bottone145.setOnClickListener();
        setOnClick(bottone145);
        setOnClick(bottone150);
        setOnClick(bottone155);

        if(!listaMappe.isEmpty()){
            for (Mappa i: listaMappe){
                FrameLayout frame =new FrameLayout(this);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );
                layoutParams.setMargins(convertDpToPixel(5),convertDpToPixel(5),convertDpToPixel(5),convertDpToPixel(5));
                frame.setLayoutParams(layoutParams);

                ImageButton btn = new ImageButton(this);
                ViewGroup.LayoutParams btnParams = new ViewGroup.LayoutParams(
                        convertDpToPixel(320),
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                btn.setLayoutParams(btnParams);
                btn.setBackground(getResources().getDrawable(R.drawable.bottone_mappa, getTheme()));
                btn.setPadding(convertDpToPixel(10),convertDpToPixel(10),convertDpToPixel(10),convertDpToPixel(10));
                btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                btn.setImageDrawable(Drawable.createFromPath(Environment.getExternalStorageDirectory()
                        + "/Breakout/ImmaginiMappe/" + i.getImmagine()));
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

    private void setOnClick(final ImageButton btn) {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int btn_id = v.getId();
                Intent intent = new Intent(SelPiano.this, Navigation1.class);

                if (btn_id == R.id.button145) {
                    intent.putExtra("ID_Activity", "From_SelPiano");
                    intent.putExtra("Btn", "145");
                    startActivity(intent);
                } else if (btn_id == R.id.button150) {
                    intent.putExtra("ID_Activity", "From_SelPiano");
                    intent.putExtra("Btn", "150");
                    startActivity(intent);
                } else
                //if (btn_id == R.id.button155)
                {
                    intent.putExtra("ID_Activity", "From_SelPiano");
                    intent.putExtra("Btn", "155");
                    startActivity(intent);
                }
            }
        });
    }


}