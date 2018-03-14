package is.hi.hopur16.nyttapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list_view);
        rideAdapter = new RideAdapter(this,R.layout.row_layout);
        listView = findViewById(R.id.listview);
        listView.setAdapter(rideAdapter);
        json_string = getIntent().getExtras().getString("json_data");
        try {
            String ridefrom,rideto,date,deptime;
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

                    Ride ride = new Ride(ridefrom, rideto, date, deptime,seatsavailable,cost);
                    rideAdapter.add(ride);
                    count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
