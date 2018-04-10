package is.hi.hopur16.nyttapp;

import is.hi.hopur16.nyttapp.Ride;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import java.text.DateFormat;

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

import static java.lang.Boolean.valueOf;

public class rideActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Ride ride;

    private Vibrator vib;
    Animation animShake;

    private TextInputLayout fromTxtLayout, toTxtLayout, costTxtLayout, seatsTxtLayout,
    dateTxtLayout, timeTxtLayout;
    private AutoCompleteTextView fromTxt, toTxt;
    private EditText costTxt, seatsTxt, dateTxt, timeTxt;
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

        costTxt = (EditText) findViewById(R.id.costTxt);
        seatsTxt = (EditText) findViewById(R.id.seatsTxt);
        dateTxt = (EditText) findViewById(R.id.dateTxt);
        timeTxt = (EditText) findViewById(R.id.timeTxt);

        fromTxtLayout = findViewById(R.id.fromTxtLayout);
        toTxtLayout = findViewById(R.id.toTxtLayout);
        costTxtLayout = findViewById(R.id.costTxtLayout);
        seatsTxtLayout = findViewById(R.id.seatsTxtLayout);
        dateTxtLayout = findViewById(R.id.dateTxtLayout);
        timeTxtLayout = findViewById(R.id.timeTxtLayout);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Adapter fyrir AutoCompleteView-ið og initializa það
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, places);
        fromTxt.setThreshold(2);
        fromTxt.setAdapter(adapter);
        toTxt.setThreshold(2);
        toTxt.setAdapter(adapter);

        // Veljum dagsetningu með datepicker
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
        if (!checkSeats()) {
            seatsTxt.setAnimation(animShake);
            seatsTxt.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkCost()) {
            costTxt.setAnimation(animShake);
            costTxt.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkDate()) {
            dateTxt.setAnimation(animShake);
            dateTxt.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkTime()) {
            timeTxt.setAnimation(animShake);
            timeTxt.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }

        fromTxtLayout.setErrorEnabled(false);
        toTxtLayout.setErrorEnabled(false);
        seatsTxtLayout.setErrorEnabled(false);
        costTxtLayout.setErrorEnabled(false);
        dateTxtLayout.setErrorEnabled(false);
        timeTxtLayout.setErrorEnabled(false);

        if (checkFrom() && checkTo() && checkSeats() && checkCost() && checkDate() && checkTime()) {
            Ride sendRide = createRide();
            Intent intent = new Intent(rideActivity.this, homeActivity.class);
            //Óþarfi??
            intent.putExtra("newRide", sendRide);
            startActivity(intent);
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

    // Aðferð sem athugar hvort sætafjöldi sé gildur (hvorki tómur né 0)
    private boolean checkSeats() {
        String seats = seatsTxt.getText().toString().trim();
        if (seats.isEmpty() || seats.equals("0")) {
            seatsTxtLayout.setErrorEnabled(true);
            seatsTxtLayout.setError(getString(R.string.err_msg_seats));
            seatsTxt.setError(getString(R.string.err_msg_req));
            requestFocus(seatsTxt);
            return false;
        }
        seatsTxtLayout.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem athugar hvort kostnaður sé gildur (ekki tómur)
    private boolean checkCost() {
        if (costTxt.getText().toString().trim().isEmpty()) {
            costTxtLayout.setErrorEnabled(true);
            costTxtLayout.setError(getString(R.string.err_msg_cost));
            costTxt.setError(getString(R.string.err_msg_req));
            requestFocus(costTxt);
            return false;
        }
        costTxtLayout.setErrorEnabled(false);
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

    // Aðferð sem athugar hvort tímasetning sé gild
    private boolean checkTime() {
        String time = timeTxt.getText().toString().trim();
        if (time.isEmpty()) {
            timeTxtLayout.setError(getString(R.string.err_msg_time));
            timeTxt.setError(getString(R.string.err_msg_req));
            requestFocus(timeTxt);
            return false;
        } else if (time.contains(":")) {
            String[] splitTime = time.split(":");
            int hour = Integer.parseInt(splitTime[0]);
            int min = Integer.parseInt(splitTime[1]);
            if (hour > 24 || min > 59) {
                timeTxtLayout.setError(getString(R.string.err_msg_time));
                timeTxt.setError(getString(R.string.err_msg_req));
                requestFocus(timeTxt);
                return false;
            }
        } else {
            String subString1 = time.substring(0,2);
            String subString2 = time.substring(2);
            int hour = Integer.parseInt(subString1);
            int min = Integer.parseInt(subString2);
            if (hour > 24 || min > 59) {
                timeTxtLayout.setError(getString(R.string.err_msg_time));
                timeTxt.setError(getString(R.string.err_msg_req));
                requestFocus(timeTxt);
                return false;
            }
        }


        timeTxtLayout.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem setur fókus á tiltekið view
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        String username = preferences.getString("username", "NOSTRING");
        String phone = preferences.getString("phone","");
        String email = preferences.getString("email", "");
        fromTxt = (AutoCompleteTextView) findViewById(R.id.fromTxt);
        toTxt = (AutoCompleteTextView) findViewById(R.id.toTxt);
        costTxt = (EditText) findViewById(R.id.costTxt);
        int costInt = Integer.parseInt(String.valueOf(costTxt.getText()));
        seatsTxt = (EditText) findViewById(R.id.seatsTxt);
        int seatsInt = Integer.parseInt(String.valueOf(seatsTxt.getText()));
        dateTxt = (EditText) findViewById(R.id.dateTxt);
        timeTxt = (EditText) findViewById(R.id.timeTxt);
        ride = new Ride(fromTxt.getText().toString(),toTxt.getText().toString(), dateTxt.getText().toString(), timeTxt.getText().toString(),
                seatsInt, costInt, username, phone, email);
        JSONObject toPost = new JSONObject();
        try {
            toPost.put("rideFrom", fromTxt.getText().toString());
            toPost.put("rideTo", toTxt.getText().toString());
            toPost.put("date", dateTxt.getText().toString());
            toPost.put("depTime", timeTxt.getText().toString());
            toPost.put("seatsAvailable", seatsInt);
            toPost.put("cost", costInt);
            toPost.put("userName",username);
            toPost.put("phone", phone);
            toPost.put("email", email);
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



