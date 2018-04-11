package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayListView extends AppCompatActivity {

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    RideAdapter rideAdapter;
    ListView listView;
    Ride[] rides = new Ride[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list_view);
        rideAdapter = new RideAdapter(this,R.layout.row_layout);
        listView = findViewById(R.id.listview);
        listView.setAdapter(rideAdapter);
        json_string = getIntent().getExtras().getString("json_data");
        try {
            String ridefrom,rideto,date,deptime,username,email,phone;
            int cost,seatsavailable;
            jsonArray = new JSONArray(json_string);
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                    ridefrom = JO.getString("ridefrom");
                    rideto = JO.getString("rideto");
                    date = JO.getString("date");
                    deptime = JO.getString("deptime");
                    cost = JO.getInt("cost");
                    seatsavailable = JO.getInt("seatsavailable");
                    username = JO.getString("username");
                    phone = JO.getString("phone");
                    email = JO.getString("email");


                    Ride ride = new Ride(ridefrom, rideto, date, deptime,seatsavailable,cost, username, phone, email);
                    rideAdapter.add(ride);
                    rides[count] = ride;
                    count++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RideInfoPopup.class);
                intent.putExtra("info", rides[position]);
                startActivity(intent);

            }
        });


    }
}
