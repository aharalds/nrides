package is.hi.hopur16.nyttapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonParser;

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

public class SignupActivity extends AppCompatActivity {

    User user;
    UserInform userInfo;
    private Vibrator vib;
    Animation animShake;
    private TextInputLayout signupInputLayoutName, signupInputLayoutUsername,
            signupInputLayoutEmail, signupInputLayoutPhone, signupInputLayoutPassword;
    private EditText signupInputName, signupInputUsername, signupInputEmail,
            signupInputPhone, signupInputPassword;
    private Button btnSignup;
    private Button linkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupInputLayoutName = (TextInputLayout) findViewById(R.id.signupInputLayoutName);
        signupInputLayoutUsername = (TextInputLayout) findViewById(R.id.signupInputLayoutUsername);
        signupInputLayoutEmail = (TextInputLayout) findViewById(R.id.signupInputLayoutEmail);
        signupInputLayoutPhone = (TextInputLayout) findViewById(R.id.signupInputLayoutPhone);
        signupInputLayoutPassword = (TextInputLayout) findViewById(R.id.signupInputLayoutPassword);

        signupInputName = (EditText) findViewById(R.id.name);
        signupInputUsername = (EditText) findViewById(R.id.username);
        signupInputEmail = (EditText) findViewById(R.id.email);
        signupInputPhone = (EditText) findViewById(R.id.phone);
        signupInputPassword = (EditText) findViewById(R.id.password);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        linkLogin = (Button) findViewById(R.id.link_login);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Þegar smellt er á hnappinn "Búa til aðgang"
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        // Þegar notandi er þegar með aðgang fer hann á innskráningarsíðuna
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Aðferð sem villutékkar inntakið frá notanda og lætur vita ef upplýsingar vantar eða
     * þær eru ekki rétt skráðar.
     */
    private void submitForm() {

        if (!checkName()) {
            signupInputName.setAnimation(animShake);
            signupInputName.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkUsername()) {
            signupInputUsername.setAnimation(animShake);
            signupInputUsername.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkEmail()) {
            signupInputEmail.setAnimation(animShake);
            signupInputEmail.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkPassword()) {
            signupInputPassword.setAnimation(animShake);
            signupInputPassword.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkPhone()) {
            signupInputPhone.setAnimation(animShake);
            signupInputPhone.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }

        signupInputLayoutName.setErrorEnabled(false);
        signupInputLayoutUsername.setErrorEnabled(false);
        signupInputLayoutEmail.setErrorEnabled(false);
        signupInputLayoutPhone.setErrorEnabled(false);
        signupInputLayoutPassword.setErrorEnabled(false);

        if (checkName() && checkUsername() && checkEmail() && checkPassword() && checkPhone()) {
            createUser();
        }
    }

    // Aðferð sem athugar hvort nafnið sem notandi slær inn sé gilt (ekki tómt)
    private boolean checkName() {
        if (signupInputName.getText().toString().trim().isEmpty()) {
            signupInputLayoutName.setErrorEnabled(true);
            signupInputLayoutName.setError(getString(R.string.err_msg_name));
            signupInputName.setError(getString(R.string.err_msg_required));
            return false;
        }
        signupInputLayoutName.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem athugar hvort notandanafn sé gilt (ekki tómt)
    private boolean checkUsername() {
        if (signupInputUsername.getText().toString().trim().isEmpty()) {
            signupInputLayoutUsername.setErrorEnabled(true);
            signupInputLayoutUsername.setError(getString(R.string.err_msg_username));
            signupInputUsername.setError(getString(R.string.err_msg_required));
            return false;
        }
        signupInputLayoutUsername.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem athugar hvort netfangið sé gilt netfang
    private boolean checkEmail() {
        String email = signupInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {

            signupInputLayoutEmail.setErrorEnabled(true);
            signupInputLayoutEmail.setError(getString(R.string.err_msg_email));
            signupInputEmail.setError(getString(R.string.err_msg_required));
            requestFocus(signupInputEmail);
            return false;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem athugar hvort símanúmerið sé gilt (a.m.k. 7 stafir og ekki tómt)
    private boolean checkPhone() {
        String phone = signupInputPhone.getText().toString().trim();
        if (phone.length() != 7) {
            signupInputLayoutPhone.setErrorEnabled(true);
            signupInputLayoutPhone.setError(getString(R.string.err_msg_phone));
            signupInputPhone.setError(getString(R.string.err_msg_required));
            requestFocus(signupInputPhone);
            return false;
        }
        signupInputLayoutPhone.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem athugar hvort lykilorð notanda sé gilt
    private boolean checkPassword() {
        if (signupInputPassword.getText().toString().trim().isEmpty()) {
            signupInputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(signupInputPassword);
            return false;
        }
        signupInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    // Aðferð sem athugar hvort netfangið sé gilt netfang
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void checkUnique(String string_json) throws JSONException {
        JSONObject toJsonObj = new JSONObject(string_json);
        Boolean success = toJsonObj.getBoolean("success");
        if (!success) {
            Toast.makeText(getApplicationContext(), "Skráning mistókst! Notandanafn, símanúmer eða netfang þegar í notkun.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Nýskráning tókst!", Toast.LENGTH_SHORT).show();
            String uname = toJsonObj.getString("username");
            String name = toJsonObj.getString("name");
            String phone = toJsonObj.getString("phone");
            String email = toJsonObj.getString("email");

            clearPref(uname,name,phone,email);

            Intent intent = new Intent(SignupActivity.this, homeActivity.class);
            startActivity(intent);


        }

    }

    private void clearPref(String uname, String name, String phone, String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Hreinsa Pref
        editor.clear();
        editor.commit();

        // Ýta nýjun upplýsingum inn í pref
        editor.putString("username", uname);
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.apply();
    }

    /**
     * Aðferð sem býr til nýjan notanda
     * @return User
     */
    public void createUser() {
        String username = signupInputUsername.getText().toString();
        String password = signupInputPassword.getText().toString();
        String name = signupInputName.getText().toString();
        String phone = signupInputPhone.getText().toString();
        String email = signupInputEmail.getText().toString();

        JSONObject toPost = new JSONObject();
        try {
            toPost.put("username", username);
            toPost.put("password", password);
            toPost.put("name", name);
            toPost.put("phone", phone);
            toPost.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (toPost.length() > 0) {
            new SignupActivity.SendJsonDataToServer().execute(String.valueOf(toPost));
        }
    }

    /**
     * Klasi sem sendir nýjan notanda í gagnagrunninn
     */
    class SendJsonDataToServer extends AsyncTask<String,String,String> {
        String JsonResponse = null;
        @Override
        protected String doInBackground(String... params) {

            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://nicerideserver.herokuapp.com/register");
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
            try {
                checkUnique(JsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
