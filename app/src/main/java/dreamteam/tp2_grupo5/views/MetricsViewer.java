package dreamteam.tp2_grupo5.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import dreamteam.tp2_grupo5.R;
import dreamteam.tp2_grupo5.presenters.MetricsPresenter;

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
        MetricsPresenter metricsPresenter = new MetricsPresenter(MetricsViewer.this, metric);
    }

    public void setUI(String titleText, String metricAText, String metricBText){
        title.setText(titleText); //"Login Metrics" "Shake Metrics" "Error"
        metricA.setText(metricAText);//"Login from 00:00hs to 11:59hs" "Shake from Ascending to Descending"
        metricB.setText(metricBText); //"Login from 12:00hs to 23:59hs" "Shake from Descending to Ascending"
    }

    public void setValues(int a, int b){
        valueA.setText(String.format("%d", a));
        valueB.setText(String.format("%d", b));
    }
}