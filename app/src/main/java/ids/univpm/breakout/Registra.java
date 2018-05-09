package ids.univpm.breakout;

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

import ids.univpm.breakout.validation.FormControl;

class Registra extends AppCompatActivity {

    public TextView Email;
    public EditText email_txt;
    public TextView textView2;
    public EditText pass_txt1;
    public TextView textView3;
    public EditText pass_txt2;
    public TextView textView4;
    public EditText name_txt;
    public TextView textView5;
    public EditText surname_txt;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registra);
        Email = (TextView) findViewById(R.id.Email);

    }
    // VEDERE FILE INFORMATIONSHANDLER
}


