package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MetricsViewer extends AppCompatActivity {
    String metric;
    TextView title;
    TextView metricA;
    TextView metricB;
    TextView valueA;
    TextView valueB;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics_viewer);
        Intent intent = getIntent();
        metric = intent.getStringExtra("metric");
        title = findViewById(R.id.textView2);
        metricA = findViewById(R.id.textView3);
        metricB = findViewById(R.id.textView4);
        valueA = findViewById(R.id.textView5);
        valueB = findViewById(R.id.textView6);
        sharedPreferences = getSharedPreferences(metric, Context.MODE_PRIVATE);
        switch (metric){
            case "Login":

                title.setText("Login Metrics");
                metricA.setText("Login from 00:00hs to 11:59hs");
                metricB.setText("Login from 12:00hs to 23:59hs");
                int a =  sharedPreferences.getInt("M", 0);
                int b =  sharedPreferences.getInt("T",0);
                valueA.setText("" + a);
                valueB.setText("" + b);
                break;
            case "Shake":
                title.setText("Shake Metrics");
                metricA.setText("Shake from Ascending to Descending");
                metricB.setText("Shake from Descending to Ascending");
                break;
            default:
                title.setText("Error");
                metricA.setText("Error");
                metricB.setText("Error");

        }
    }
}