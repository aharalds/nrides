package is.hi.hopur16.nyttapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class homeActivity extends AppCompatActivity  {

    Button searchRideBtn;
    Button addRideBtn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextColor(Color.parseColor("#000000"));

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
}



