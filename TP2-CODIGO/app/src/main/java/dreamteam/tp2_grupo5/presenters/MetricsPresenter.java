package dreamteam.tp2_grupo5.presenters;

import android.content.Context;
import android.content.Intent;

import dreamteam.tp2_grupo5.models.PreferencesModel;
import dreamteam.tp2_grupo5.views.MetricsViewer;

public class MetricsPresenter {
    MetricsViewer metricsViewer;
    PreferencesModel metricModel;
    String metric;
    public MetricsPresenter(MetricsViewer metricsViewer) {
        this.metricsViewer = metricsViewer;
        Intent intent = metricsViewer.getIntent();
        this.metric = intent.getStringExtra("metric");
        this.metricModel = new PreferencesModel(metricsViewer.getSharedPreferences(metric, Context.MODE_PRIVATE));
    }

    public void handlerMetrics(){

        switch (metric){
            case "Login":
                metricsViewer.setUI("Login Metrics", "Login from 00:00hs to 11:59hs","Login from 12:00hs to 23:59hs");
                int a = metricModel.getPreferences("M");
                int b = metricModel.getPreferences("T");
                metricsViewer.setValues(a,b);
                break;
            case "Shake":
                metricsViewer.setUI("Shake Metrics","Shake from Ascending to Descending","Shake from Descending to Ascending");
                int c =  metricModel.getPreferences("AtoD");
                int d =  metricModel.getPreferences("DtoA");
                metricsViewer.setValues(c,d);
                break;
            default:
                metricsViewer.setUI("Error","Error","Error");
        }
    }

}
