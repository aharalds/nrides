package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class RideEditPopup extends AppCompatActivity {

    Button btnCommit;
    Button btnDelete;
    homeActivity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ride_change_popup);

        Intent intent = getIntent();
        final Ride ride = (Ride)intent.getSerializableExtra("info");

        btnCommit = (Button) findViewById(R.id.btnCommit);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonDelete(ride.getId());
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView driver = (TextView) findViewById(R.id.driver);
        driver.setText("Breyta Auglýsingu");
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        String seats = String.valueOf(ride.getSeatsavailable());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        if (seats != null) {
            int spinnerPosition = adapter.getPosition(seats);
            dropdown.setSelection(spinnerPosition);
        }
    }

    public void jsonDelete(int id) {
        new DeleteData().execute(String.valueOf(id));
    }

    public void checkSuccess(String jsonString) throws JSONException {
        if (jsonString == "false") {
            Toast.makeText(getApplicationContext(),"Villa kom upp, ekki hægt að eyða" ,Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, homeActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Tókst! Auglýsingu eytt" ,Toast.LENGTH_LONG).show();
        }
    }

    class DeleteData extends AsyncTask<String,String,String> {
        String jsonR = "false";

        @Override
        protected String doInBackground(String... params) {
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://nicerideserver.herokuapp.com/" + JsonDATA);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("charset", "utf-8");
                Log.e("TAG" , " :" + urlConnection.getResponseCode());
                if (urlConnection.getResponseCode() == 201) {
                    jsonR = "true";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("MSG", " BRE: " + jsonR);
            try {
                checkSuccess(jsonR);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
