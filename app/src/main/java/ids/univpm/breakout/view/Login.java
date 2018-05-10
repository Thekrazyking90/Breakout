package ids.univpm.breakout.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ids.univpm.breakout.R;

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

        mRegistra.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // System.out.println("CHECK "+Login.this+"   "+ Registra.class);
                startActivity(new Intent(Login.this, Registra.class));
                    }
        });


        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //attemptLogin();
                startActivity(new Intent(Login.this, Navigation1.class));
            }
        });

        mRecuperaPw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Recupera.class));
            }
        });

    }

}

