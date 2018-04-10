package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RideInfoPopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_info_popup);

        Intent intent = getIntent();
        Ride ride = (Ride)intent.getSerializableExtra("info");

        // Meira eða minn info í popup?
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView seats = (TextView) findViewById(R.id.seats);
        TextView depTime = (TextView) findViewById(R.id.dep);
        cost.setText("Kostnaður hvers farþega: " + String.valueOf(ride.getCost()));
        seats.setText("Laus sæti: " + String.valueOf(ride.getSeatsavailable()));
        depTime.setText("Brottför " + ride.getDate() + " klukkan " + ride.getDeptime());
    }
}
