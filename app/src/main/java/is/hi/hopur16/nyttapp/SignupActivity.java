package is.hi.hopur16.nyttapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hörn on 2.4.2018.
 */

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private Vibrator vib;
    Animation animShake;
    private TextInputLayout signupInputLayoutName, signupInputLayoutEmail, signupInputLayoutPhone, signupInputLayoutPassword;
    private EditText signupInputName, signupInputEmail, signupInputPhone, signupInputPassword;
    private Button btnSignup;
    private Button linkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupInputLayoutName = (TextInputLayout) findViewById(R.id.signupInputLayoutName);
        signupInputLayoutEmail = (TextInputLayout) findViewById(R.id.signupInputLayoutEmail);
        signupInputLayoutPhone = (TextInputLayout) findViewById(R.id.signupInputLayoutPhone);
        signupInputLayoutPassword = (TextInputLayout) findViewById(R.id.signupInputLayoutPassword);

        signupInputName = (EditText) findViewById(R.id.name);
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
        }
        signupInputLayoutName.setErrorEnabled(false);
        signupInputLayoutEmail.setErrorEnabled(false);
        signupInputLayoutPhone.setErrorEnabled(false);
        signupInputLayoutPassword.setErrorEnabled(false);
        if (checkName() && checkEmail() && checkPassword() && checkPhone()) {
            Toast.makeText(getApplicationContext(), "Skráning tókst!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this, homeActivity.class);
            startActivity(intent);
        }
    }

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

    private boolean checkPhone() {
        String phone = signupInputPhone.getText().toString().trim();
        if (phone.isEmpty() || phone.length() < 7) {
            signupInputLayoutPhone.setErrorEnabled(true);
            signupInputLayoutPhone.setError(getString(R.string.err_msg_phone));
            signupInputPhone.setError(getString(R.string.err_msg_required));
            requestFocus(signupInputPhone);
            return false;
        }
        signupInputLayoutPhone.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {
        if (signupInputPassword.getText().toString().trim().isEmpty()) {

            signupInputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(signupInputPassword);
            return false;
        }
        signupInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
