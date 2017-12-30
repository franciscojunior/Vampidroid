package name.vampidroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.about);

        TextView aboutDescription = findViewById(R.id.textAboutDescription);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            aboutDescription.setText(Html.fromHtml(getString(R.string.about_description), Html.FROM_HTML_MODE_LEGACY));
        } else {
            aboutDescription.setText(Html.fromHtml(getString(R.string.about_description)));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
