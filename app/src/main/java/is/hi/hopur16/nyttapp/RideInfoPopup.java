package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RideInfoPopup extends AppCompatActivity {

    ImageButton callButton;
    ImageButton smsButton;
    ImageButton emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ride_info_popup);


        Intent intent = getIntent();
        final Ride ride = (Ride)intent.getSerializableExtra("info");

        // Meira eða minn info í popup?
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView seats = (TextView) findViewById(R.id.seats);
        TextView depTime = (TextView) findViewById(R.id.dep);
        TextView driver = (TextView) findViewById(R.id.driver);
        driver.setText("Sett inn af " + ride.getUsername());
        cost.setText("Kostnaður per: " + String.valueOf(ride.getEmail()) + ", Laus sæti: " + String.valueOf(ride.getSeatsavailable()));
        //seats.setText("Laus sæti: " + String.valueOf(ride.getSeatsavailable()));
        depTime.setText("Brottför " + ride.getDate() + " klukkan " + ride.getDeptime());

        callButton = (ImageButton) findViewById(R.id.btn_phone);
        smsButton = (ImageButton) findViewById(R.id.btn_sms);
        emailButton = (ImageButton) findViewById(R.id.btn_email);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + ride.getPhone()));
                startActivity(callIntent);
            }
        });







    }
}
