package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

public class HomeScreenActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSignup;
    private TextView welcomeText;
    private TextView moreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignup = (Button) findViewById(R.id.btn_signup);

        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        TextView moreText = (TextView) findViewById(R.id.moreText);
        welcomeText.setTextColor(Color.parseColor("#000000"));
        moreText.setTextColor(Color.parseColor("#000000"));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

}
