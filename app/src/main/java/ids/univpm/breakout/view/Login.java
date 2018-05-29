package ids.univpm.breakout.view;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import ids.univpm.breakout.R;
import ids.univpm.breakout.controller.Controller;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    public TextView mRegistra;
    public TextView mRecuperaPw;
    public Button mSignInButton;
    public TextView mErroreDati;
    DownloadManager dm;
    long queueid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mRegistra=(TextView) findViewById(R.id.registra);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSignInButton = (Button) findViewById(R.id.signin_button);
        mRecuperaPw=(TextView) findViewById(R.id.recuperapw);
        final TextView mErroreDati = (TextView) findViewById(R.id.errore_dati);
        final AlertDialog.Builder popup_error = new AlertDialog.Builder(Login.this);

        mRegistra.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registra.class));
                    }
        });


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                    DownloadManager.Query req_query = new DownloadManager.Query();
                    req_query.setFilterById(queueid);
                    Cursor c= dm.query(req_query);
                    if(c.moveToFirst()){
                        int columnIndex =c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL==c.getInt(columnIndex)){
                            Toast.makeText(getApplicationContext(), "Download riuscito", Toast.LENGTH_LONG).show();
                        }
                        else {
                            popup_error.setMessage("Si è verificato un errore durante il download");
                            AlertDialog alertDialog = popup_error.create();
                            alertDialog.show();
                        }
                    }
                }
            }
        };


        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));




        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //attemptLogin();
                // Inserire il check delle credenziali, se true, apro navigation1
                Intent i = new Intent();
                i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                startActivity(i);

                startActivity(new Intent(Login.this, Navigation1.class));

                //se false
                //mErroreDati.setVisibility(View.VISIBLE);
                // Scritta "I dati inseriti sono errati"


            }
        });

        mRecuperaPw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Recupera.class));
            }
        });

    }
    public void download_Click(View v){
        dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request= new DownloadManager.Request(Uri.parse("jdbc:derby://localhost:1527/breakout"));
        queueid = dm.enqueue(request);
    }

}

