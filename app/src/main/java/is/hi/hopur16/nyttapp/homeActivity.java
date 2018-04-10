package is.hi.hopur16.nyttapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class homeActivity extends AppCompatActivity  {

    UserInform userInfo;
    Button searchRideBtn;
    Button addRideBtn;
    Button logoutButton;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextColor(Color.parseColor("#000000"));

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
}



