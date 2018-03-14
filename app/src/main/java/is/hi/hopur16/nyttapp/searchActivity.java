package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.internal.gmsg.HttpClient;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by atliharaldsson on 12/03/2018.
 */

public class searchActivity extends AppCompatActivity {

    Ride[] rides;
    TextView fromTxt;
    TextView toTxt;
    TextView dateTxt;
    Button sendaBtn;
    String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_ride);

        sendaBtn = (Button) findViewById(R.id.sendaBtn);
        sendaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForRides();
                //parseJSON();
                //Intent intent = new Intent(searchActivity.this, homeActivity.class);
                //intent.putExtra("rideList", rides);
                //startActivity(intent);


            }
        });

    }
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


    public void parseJSON(String jal) {
        if(jal == null) {
            Toast.makeText(getApplicationContext(),"Engin för fundin", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(searchActivity.this, DisplayListView.class);
            intent.putExtra("json_data", jal);
            startActivity(intent);
        }


    }

    public void searchForRides() {
        fromTxt = (TextView) findViewById(R.id.fromTxt);
        toTxt = (TextView) findViewById(R.id.toTxt);
        if(fromTxt.getText().toString().isEmpty() && fromTxt.getText().toString().isEmpty()) {
            new getJSON().execute("http://nicerideserver.herokuapp.com/");
        } else {
        new getJSON().execute("http://nicerideserver.herokuapp.com/rides?rideFrom=" + fromTxt.getText().toString() + "&rideTo=" + toTxt.getText().toString());
        }

    }

    /**
     * AsyncTask klasi sem talar við vefþjónustu
     */
    class getJSON extends AsyncTask<String, String, String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseJSON(server_response);
            //jsonString = server_response;
            //Log.v("CatalogClient" , server_response);
            //Gson gson = new Gson();
            //rides = gson.fromJson(server_response, Ride[].class);
            Log.e("Response", "BOOM" + server_response);

        }
    }
}