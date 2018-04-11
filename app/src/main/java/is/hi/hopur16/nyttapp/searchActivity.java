package is.hi.hopur16.nyttapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
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
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import android.app.DatePickerDialog;

/**
 * Created by atliharaldsson on 12/03/2018.
 */

public class searchActivity extends AppCompatActivity {

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%ÁáÍíðÐþÞéÉÖÆæöúÚ";
    Ride[] rides;
    private Vibrator vib;
    Animation animShake;
    private AutoCompleteTextView fromTxt, toTxt;
    private TextInputLayout fromTxtLayout, toTxtLayout, dateTxtLayout;
    TextView dateTxt;
    Button sendaBtn;
    String jsonString;
    String[] places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_ride);
        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        // úr strings.xlm í string array í activity-i
        Resources res = getResources();
        places = res.getStringArray(R.array.places);
        toTxt = (AutoCompleteTextView) findViewById(R.id.toTxt);
        fromTxt = (AutoCompleteTextView) findViewById(R.id.fromTxt);
        dateTxt = (TextView) findViewById(R.id.dateTxt);

        fromTxtLayout = findViewById(R.id.fromTxtLayout);
        toTxtLayout = findViewById(R.id.toTxtLayout);
        dateTxtLayout = findViewById(R.id.dateTxtLayout);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Adapter fyrir AutoCompleteView-ið og initializa það
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, places);
        fromTxt.setThreshold(2);
        fromTxt.setAdapter(adapter);
        toTxt.setThreshold(2);
        toTxt.setAdapter(adapter);

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
                submitForm();
            }
        });
    }

    // Aðferð sem athugar hvort input frá notanda sé gilt
    public void submitForm() {

        if (!checkFrom()) {
            fromTxt.setAnimation(animShake);
            fromTxt.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkTo()) {
            toTxt.setAnimation(animShake);
            toTxt.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkDate()) {
            dateTxt.setAnimation(animShake);
            dateTxt.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }

        fromTxtLayout.setErrorEnabled(false);
        toTxtLayout.setErrorEnabled(false);
        dateTxtLayout.setErrorEnabled(false);

        if (checkFrom() && checkTo() && checkDate()) {
            try {
                searchForRides();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    // Aðferð sem athugar hvort brottfararstaður sem notandi slær inn sé tókur
    private boolean checkFrom() {
        if (fromTxt.getText().toString().trim().isEmpty()) {
            fromTxtLayout.setErrorEnabled(true);
            fromTxtLayout.setError(getString(R.string.err_msg_from));
            fromTxt.setError(getString(R.string.err_msg_req));
            requestFocus(fromTxt);
            return false;
        }
        fromTxtLayout.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem athugar hvort áfangastaður sé gildur (ekki tómur)
    private boolean checkTo() {
        if (toTxt.getText().toString().trim().isEmpty()) {
            toTxtLayout.setErrorEnabled(true);
            toTxtLayout.setError(getString(R.string.err_msg_to));
            toTxt.setError(getString(R.string.err_msg_req));
            requestFocus(toTxt);
            return false;
        }
        toTxtLayout.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem athugar hvort dagsetning sé gild (ekki tóm)
    private boolean checkDate() {
        if (dateTxt.getText().toString().trim().isEmpty()) {
            dateTxtLayout.setError(getString(R.string.err_msg_date));
            dateTxt.setError(getString(R.string.err_msg_req));
            requestFocus(dateTxt);
            return false;
        }
        dateTxtLayout.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem setur fókus á tiltekið view
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        if(jal.length() < 10) {
            Toast.makeText(getApplicationContext(),"Engin för fundust", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(searchActivity.this, DisplayListView.class);
            intent.putExtra("json_data", jal);
            startActivity(intent);
        }
    }

    public void searchForRides() throws UnsupportedEncodingException {
        fromTxt = (AutoCompleteTextView) findViewById(R.id.fromTxt);
        toTxt = (AutoCompleteTextView) findViewById(R.id.toTxt);
        dateTxt = (TextView) findViewById(R.id.dateTxt);
        String dateEncoded = URLEncoder.encode(dateTxt.getText().toString(), "UTF-8");
        String fromEncoded = URLEncoder.encode(fromTxt.getText().toString(), "UTF-8");
        String toEncoded = URLEncoder.encode(toTxt.getText().toString(), "UTF-8");
        Log.e("Cata", "searchForRides: " + fromTxt.getText().toString() + " : " + fromEncoded + " : " + toTxt.getText().toString() );
        if(Arrays.asList(places).contains(fromTxt.getText().toString()) && Arrays.asList(places).contains(toTxt.getText().toString())) {
            //findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            new getJSON().execute("rides?rideFrom=" + fromEncoded + "&rideTo=" + toEncoded + "&date=" + dateEncoded);
        } else {
            Toast.makeText(getApplicationContext(),"Óþekktur brottfara- eða komustaður" ,Toast.LENGTH_LONG).show();
        }
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
                URL_STRING = Uri.encode(URL_STRING,ALLOWED_URI_CHARS);
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