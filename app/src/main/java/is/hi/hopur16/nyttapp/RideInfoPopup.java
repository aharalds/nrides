package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RideInfoPopup extends AppCompatActivity {

    ImageButton callButton;
    ImageButton smsButton;
    ImageButton emailButton;
    String SMS_MESSAGE_STOCK = "";
    String EMAIL_MESSAGE_STOCK = "";
    homeActivity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ride_info_popup);


        Intent intent = getIntent();
        final Ride ride = (Ride)intent.getSerializableExtra("info");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String uName = preferences.getString("name", "");

        SMS_MESSAGE_STOCK = "Góðan daginn, " + ride.getUsername() + "\n\n Má ég fá far frá " + ride.getRidefrom() + " til " + ride.getRideto()
                + " auglýst á Nicerides? \n Brottför skráð " + ride.getDate() + " kl " + ride.getDeptime() + "\n\n m.b. kv " + uName;

        EMAIL_MESSAGE_STOCK = "Góðann daginn, " + ride.getUsername() + "\n\n Þú settir inn eftirfarandi auglýsingu: \n\n" + "Frá: " + ride.getRidefrom()
                + "Til: " + ride.getRideto() + "\n Dags: " + ride.getDate() + "\n Kl: " + ride.getCost() + "\n Kostn.:" + ride.getCost()
                + "\n Sæti: " + ride.getSeatsavailable() + "\n\n Áttu laus sæti fyrir: ***1*** \n\n m.b.kv. " + uName;

        // Meira eða minn info í popup?
        /*
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView seats = (TextView) findViewById(R.id.seats);
        TextView depTime = (TextView) findViewById(R.id.dep);
        */

        TextView driver = (TextView) findViewById(R.id.driver);
        driver.setText("Auglýsandi: " + ride.getUsername());

        /*
        cost.setText("Kostnaður: " + String.valueOf(ride.getCost()) + " per farðþegi");
        seats.setText("Hafðu samband");

        depTime.setText("Brottför " + ride.getDate() + " klukkan " + ride.getDeptime());
        */

        callButton = (ImageButton) findViewById(R.id.btn_phone);
        smsButton = (ImageButton) findViewById(R.id.btn_sms);
        emailButton = (ImageButton) findViewById(R.id.btn_email);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + ride.getPhone()));
                startActivity(callIntent);
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ride.getPhone()));
                intent.putExtra("sms_body", SMS_MESSAGE_STOCK);
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("mailto:" + ride.getEmail()));
                //sendIntent.setType("message/rfc822");
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { ride.getEmail()});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,"NiceRides: Beiðni um far");
                sendIntent.putExtra(Intent.EXTRA_TEXT, EMAIL_MESSAGE_STOCK);
                try {
                    startActivity(Intent.createChooser(sendIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(RideInfoPopup.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
