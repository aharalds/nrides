package is.hi.hopur16.nyttapp;

import is.hi.hopur16.nyttapp.Ride;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

public class rideActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Ride ride;

    EditText dateTxt;
    EditText fromTxt;
    EditText toTxt;
    EditText costTxt;
    EditText seatsTxt;
    EditText timeTxt;
    Button sendaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        dateTxt = (EditText) findViewById(R.id.dateTxt);
        dateTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(), "date picker");
                }
            }
        });

        sendaBtn = (Button) findViewById(R.id.sendaBtn);
        sendaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rideActivity.this, DisplayRideActivity.class);
                Ride sendRide = createRide();
                intent.putExtra("newRide", sendRide);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        dateTxt.setText(currentDate);

    }

    public Ride createRide() {
        fromTxt = (EditText) findViewById(R.id.fromTxt);
        toTxt = (EditText) findViewById(R.id.toTxt);
        costTxt = (EditText) findViewById(R.id.costTxt);
        int costInt = Integer.parseInt(String.valueOf(costTxt.getText()));
        seatsTxt = (EditText) findViewById(R.id.seatsTxt);
        int seatsInt = Integer.parseInt(String.valueOf(seatsTxt.getText()));
        dateTxt = (EditText) findViewById(R.id.seatsTxt);
        timeTxt = (EditText) findViewById(R.id.timeTxt);
        ride = new Ride(fromTxt.getText().toString(),toTxt.getText().toString(), dateTxt.getText().toString(), timeTxt.getText().toString(), seatsInt, costInt);
        return ride;
    }
}
