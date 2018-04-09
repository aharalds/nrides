package is.hi.hopur16.nyttapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Hörn on 2.4.2018.
 */

public class LoginActivity extends AppCompatActivity {

    private Vibrator vib;
    Animation animShake;
    private TextInputLayout loginInputLayoutUsername, loginInputLayoutPassword;
    private EditText loginInputUsername, loginInputPassword;
    private Button btnLogin;
    private Button linkSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginInputLayoutUsername = (TextInputLayout) findViewById(R.id.loginInputLayoutUsername);
        loginInputLayoutPassword = (TextInputLayout) findViewById(R.id.loginInputLayoutPassword);

        loginInputUsername = (EditText) findViewById(R.id.loginUsername);
        loginInputPassword = (EditText) findViewById(R.id.loginPW);
        btnLogin = (Button) findViewById(R.id.btn_login);
        linkSignup = (Button) findViewById(R.id.link_signup);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Þegar smellt er á hnappinn "Búa til aðgang"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        // Þegar notandi er ekki með aðgang fer hann yfir á síðuna til þess
        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Aðferð sem villutékkar inntakið frá notanda og lætur vita ef upplýsingar vantar eða
     * þær eru ekki rétt skráðar.
     */
    private void logIn() {

        if (!checkUsername()) {
            loginInputUsername.setAnimation(animShake);
            loginInputUsername.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkPassword()) {
            loginInputPassword.setAnimation(animShake);
            loginInputPassword.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        loginInputLayoutUsername.setErrorEnabled(false);
        loginInputLayoutPassword.setErrorEnabled(false);
        if (checkUsername() && checkPassword()) {
            sendUser();
        }
    }

    /**
     * Aðferð sem athugar hvort notandanafn sé gilt (ekki tómt)
     * @return
     */
    private boolean checkUsername() {
        String email = loginInputUsername.getText().toString().trim();
        if (email.isEmpty()) {

            loginInputLayoutUsername.setErrorEnabled(true);
            loginInputLayoutUsername.setError(getString(R.string.err_msg_email));
            loginInputUsername.setError(getString(R.string.err_msg_required));
            requestFocus(loginInputUsername);
            return false;
        }
        loginInputLayoutUsername.setErrorEnabled(false);
        return true;
    }

    /**
     * Aðferð sem athugar hvort lykilorð sé gilt (ekki tómt)
     * @return
     */
    private boolean checkPassword() {
        if (loginInputPassword.getText().toString().trim().isEmpty()) {

            loginInputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(loginInputPassword);
            return false;
        }
        loginInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem setur fókus á ákveðið view
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Aðferð sem sendir notandanafn og lykilorð á server
     */
    public void sendUser() {
        String username = loginInputUsername.getText().toString();
        String password = loginInputPassword.getText().toString();

        JSONObject toPost = new JSONObject();
        try {
            toPost.put("username", username);
            toPost.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (toPost.length() > 0) {
            new LoginActivity.SendJsonDataToServer().execute(String.valueOf(toPost));
        }
    }

    /**
     * Aðferð sem tekur við svari frá server (true/false) og ýmist gefur villumeldingu
     * eða sendir notanda áfram (ef innskráning tókst)
     * @param response
     */
    public void checkUser(String response) {
        try {
            JSONObject json = new JSONObject(response);
            Boolean success = json.getBoolean("success");

            if (success == false) {
                Toast.makeText(getApplicationContext(), "Innskráning mistókst! Notandanafn eða lykilorð ekki til.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Innskráning tókst!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, homeActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Klasi sem sendir login upplýsingar í gagnagrunn
     */
    class SendJsonDataToServer extends AsyncTask<String,String,String> {
        String JsonResponse = null;
        @Override
        protected String doInBackground(String... params) {

            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://nicerideserver.herokuapp.com/login");
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
            Log.e("Response", " " + JsonResponse);
            checkUser(JsonResponse);
        }

    }

}