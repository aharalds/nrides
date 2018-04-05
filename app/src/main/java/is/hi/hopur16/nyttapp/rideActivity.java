package is.hi.hopur16.nyttapp;

import is.hi.hopur16.nyttapp.Ride;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;

public class rideActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Ride ride;

    EditText dateTxt;
    AutoCompleteTextView fromTxt;
    AutoCompleteTextView toTxt;
    EditText costTxt;
    EditText seatsTxt;
    EditText timeTxt;
    Button sendaBtn;
    String[] places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        Resources res = getResources();
        places = res.getStringArray(R.array.places);
        toTxt = (AutoCompleteTextView) findViewById(R.id.toTxt);
        fromTxt = (AutoCompleteTextView) findViewById(R.id.fromTxt);

        // Adapter fyrir AutoCompleteView-ið og initializa það
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, places);
        fromTxt.setThreshold(2);
        fromTxt.setAdapter(adapter);
        toTxt.setThreshold(2);
        toTxt.setAdapter(adapter);
        dateTxt = (EditText) findViewById(R.id.dateTxt);
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
                Intent intent = new Intent(rideActivity.this, homeActivity.class);
                Ride sendRide = createRide();
               //Óþarfi??
                intent.putExtra("newRide", sendRide);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        dateTxt.setText(currentDate);

    }

    public Ride createRide() {
        fromTxt = (AutoCompleteTextView) findViewById(R.id.fromTxt);
        toTxt = (AutoCompleteTextView) findViewById(R.id.toTxt);
        costTxt = (EditText) findViewById(R.id.costTxt);
        int costInt = Integer.parseInt(String.valueOf(costTxt.getText()));
        seatsTxt = (EditText) findViewById(R.id.seatsTxt);
        int seatsInt = Integer.parseInt(String.valueOf(seatsTxt.getText()));
        dateTxt = (EditText) findViewById(R.id.dateTxt);
        timeTxt = (EditText) findViewById(R.id.timeTxt);
        ride = new Ride(fromTxt.getText().toString(),toTxt.getText().toString(), dateTxt.getText().toString(), timeTxt.getText().toString(), seatsInt, costInt);
        JSONObject toPost = new JSONObject();
        try {
            toPost.put("rideFrom", fromTxt.getText().toString());
            toPost.put("rideTo", toTxt.getText().toString());
            toPost.put("date", dateTxt.getText().toString());
            toPost.put("depTime", timeTxt.getText().toString());
            toPost.put("seatsAvailable", seatsInt);
            toPost.put("cost", costInt);
            System.out.println(toPost.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (toPost.length() > 0) {
            new SendJsonDataToServer().execute(String.valueOf(toPost));
        }
        return ride;
    }

    class SendJsonDataToServer extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://nicerideserver.herokuapp.com");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    return null;
                }
                JsonResponse = buffer.toString();
                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),"Auglýsing er uppi!" ,Toast.LENGTH_LONG).show();
        }

    }
}



