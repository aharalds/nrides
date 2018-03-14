package is.hi.hopur16.nyttapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;

public class homeActivity extends AppCompatActivity  {


    Button searchRideBtn;
    Button addRideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        searchRideBtn = (Button) findViewById(R.id.searchRideBtn);
        addRideBtn = (Button) findViewById(R.id.addRideBtn);
        searchRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, searchActivity.class);
                startActivity(intent);
            }
        });

        addRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, rideActivity.class);
                startActivity(intent);
            }
        });
    }
}



