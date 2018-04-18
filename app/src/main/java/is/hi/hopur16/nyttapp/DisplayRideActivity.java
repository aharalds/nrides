package is.hi.hopur16.nyttapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

public class DisplayRideActivity extends AppCompatActivity  {

    Ride[] rides;
    TextView fromTxt;
    TextView toTxt;
    TextView costTxt;
    TextView seatsTxt;
    TextView dateTxt;
    TextView timeTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ride);

        Intent i = getIntent();
        Ride ride = (Ride)i.getSerializableExtra("newRide");


        timeTxt = (TextView) findViewById(R.id.timeTxt);
        dateTxt = (TextView) findViewById(R.id.dateTxt);
        seatsTxt = (TextView) findViewById(R.id.seatsTxt);
        costTxt = (TextView) findViewById(R.id.costTxt);
        toTxt = (TextView) findViewById(R.id.toTxt);
        fromTxt = (TextView) findViewById(R.id.fromTxt);

        fromTxt.setText("Frá: " + ride.getRidefrom());
        toTxt.setText("Til: " + ride.getRideto());
        costTxt.setText("Kostnaður: " + String.valueOf(ride.getCost()) + " kr.");
        seatsTxt.setText("Laus sæti: " + String.valueOf(ride.getSeatsavailable()));
        dateTxt.setText(ride.getDate());
        timeTxt.setText(ride.getDeptime());
    }
}


