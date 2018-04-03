package is.hi.hopur16.nyttapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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
            Toast.makeText(getApplicationContext(), "Innskráning tókst!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, homeActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Aðferð sem athugar hvort notandanafn sé til í gagnagrunni (og ekki tómt input frá notanda)
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
     * Aðferð sem athugar hvort lykilorð passi við notandanafn í gagnagrunni (og sé ekki tómt input
     * frá notanda)
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


}
