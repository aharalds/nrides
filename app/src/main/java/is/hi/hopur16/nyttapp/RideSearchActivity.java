package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

/**
 * Created by Hörn on 7.3.2018.
 */

public class RideSearchActivity extends AppCompatActivity {

    EditText fromText;
    EditText toText;
    Button leitaBtn;

    GetDataFromServer getData;
    String myUrl;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Tenging yfir í viðmótshluti
        fromText = (EditText) findViewById(R.id.fromText);
        toText = (EditText) findViewById(R.id.toText);
        leitaBtn = (Button) findViewById(R.id.leitaBtn);


        /**
         * Fall sem sendir notanda yfir á niðurstöður þegar smellt er á "Leita" hnappinn
         */
        leitaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideSearchActivity.this, DisplaySearchActivity.class);
                intent.putExtra("searchResult", result);
                startActivity(intent);

                // Sækjum gögnin
                try {
                    myUrl = "http://nicerideserver.herokuapp.com";
                    getData = new GetDataFromServer();
                    result = getData.execute(myUrl).get();
                    Log.i("Skilaboð", result);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * Klasi sem sendir GET request á slóð og nær í gögn
     */
    class GetDataFromServer extends AsyncTask<String, Void, String> {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;

            try {
                // Búum til tengingu og tengjumst slóðinni
                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.connect();

                // Lesum inn gögnin
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                // Ef línan sem við lásum inn er ekki null þá bætum við henni við
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                // Lokum InputStream og Buffered reader
                reader.close();
                streamReader.close();

                // Setjum niðurstöðurnar sem það sem við lásum inn og breytum í streng
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }
}
