package is.hi.hopur16.nyttapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class homeActivity extends AppCompatActivity  {

    UserInform userInfo;
    Button searchRideBtn;
    Button addRideBtn;
    Button logoutButton;
    Button myrideBtn;
    TextView textView;
    String mName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mName = preferences.getString("username", "");

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextColor(Color.parseColor("#000000"));

        myrideBtn = (Button) findViewById(R.id.btn_myrides);
        myrideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRideSearcher(mName);
            }
        });

        logoutButton = (Button) findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLog();

        }
        });

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

    public void checkLog() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Boolean isLogged = preferences.contains("username");
        if(isLogged) {
            editor.clear();
            editor.commit();
        }
        Intent intent = new Intent(homeActivity.this, HomeScreenActivity.class);
        startActivity(intent);
    }

    public void parseJSON(String jal) {
        if(jal.length() < 10) {
            Toast.makeText(getApplicationContext(),"Engar auglýsingar uppi frá þér", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, DisplayListView.class);
            intent.putExtra("json_data", jal);
            startActivity(intent);
        }
    }

    public void myRideSearcher(String usname) {
        if (usname.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Ekki skráður inn, BÖGGUR", Toast.LENGTH_LONG).show();
        } else {
            new getJSON().execute("mehrides/?username=" + usname);
        }
    }


    /**
     * read stream klasi
     */
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

    /**
     * AsyncTask klasi sem talar við vefþjónustu
     */
    class getJSON extends AsyncTask<String, String, String> {
        String server_response = null;
        String URL_STRING = "http://nicerideserver.herokuapp.com/";
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            URI uri;
            HttpURLConnection urlConnection = null;
            server_response = null;
            try {
                if(strings[0] != null) {
                    URL_STRING += strings[0];
                }
                uri = new URI(URL_STRING);
                url = uri.toURL();
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                    Log.v("ASNASTAFIr", "String: " + URL_STRING);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseJSON(server_response);
            Log.e("Response", "BOOM" + server_response);

        }
    }
}



