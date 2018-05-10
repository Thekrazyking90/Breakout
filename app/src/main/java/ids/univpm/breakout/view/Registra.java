package ids.univpm.breakout.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import ids.univpm.breakout.R;
import ids.univpm.breakout.validation.FormControl;

class Registra extends AppCompatActivity {

    private TextView Username;
    private EditText Email;
    private EditText Password;
    private EditText PassCheck;
    private EditText Nome;
    private EditText Cognome;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registra);
        Email = findViewById(R.id.email_txt);
        Username=findViewById(R.id.username_txt);
        Password=findViewById(R.id.pass_txt1);
        PassCheck=findViewById(R.id.pass_txt2);
        Nome= findViewById(R.id.name_txt);
        Cognome= findViewById(R.id.surname_txt);


    }
    // VEDERE FILE INFORMATIONSHANDLER
}


