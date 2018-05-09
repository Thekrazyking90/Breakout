package ids.univpm.breakout;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {

    public Button bottoneMappe;
    public Button bottoneAccedi;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        bottoneMappe = (Button) findViewById(R.id.mappeoffline);//find button by Id
        bottoneMappe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Accedi per scaricare le mappe", Toast.LENGTH_LONG).show();
            }
        });
        bottoneAccedi = (Button) findViewById(R.id.accedi);//find button by Id
        bottoneAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome.this, Login.class));
            }
        });
    }

}
