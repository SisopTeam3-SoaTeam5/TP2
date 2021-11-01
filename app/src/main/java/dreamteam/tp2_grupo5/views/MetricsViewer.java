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

    TextView title;
    TextView metricA;
    TextView metricB;
    TextView valueA;
    TextView valueB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics_viewer);
        title = findViewById(R.id.textView2);
        metricA = findViewById(R.id.textView3);
        metricB = findViewById(R.id.textView4);
        valueA = findViewById(R.id.textView5);
        valueB = findViewById(R.id.textView6);
        MetricsPresenter metricsPresenter = new MetricsPresenter(MetricsViewer.this);
        metricsPresenter.handlerMetrics();
    }

    public void setUI(String titleText, String metricAText, String metricBText){
        title.setText(titleText);
        metricA.setText(metricAText);
        metricB.setText(metricBText);
    }

    public void setValues(int a, int b){
        valueA.setText(String.format("%d", a));
        valueB.setText(String.format("%d", b));
    }
}