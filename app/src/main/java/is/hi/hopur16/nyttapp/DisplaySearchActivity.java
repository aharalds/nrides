package is.hi.hopur16.nyttapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Hörn on 7.3.2018.
 */


public class DisplaySearchActivity extends AppCompatActivity {

    TextView displaySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        displaySearch = (TextView) findViewById(R.id.searchDisplay);

        /**
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("searchResult");
            displaySearch.setText(value);
        }
         */
    }
}
