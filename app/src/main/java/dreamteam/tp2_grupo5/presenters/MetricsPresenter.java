package dreamteam.tp2_grupo5.presenters;

import android.content.Context;

import dreamteam.tp2_grupo5.models.PreferencesModel;
import dreamteam.tp2_grupo5.views.MetricsViewer;

public class MetricsPresenter {
    MetricsViewer metricsViewer;
    PreferencesModel metricModel;

    public MetricsPresenter(MetricsViewer metricsViewer, String metric) {
        this.metricsViewer = metricsViewer;
        this.metricModel = new PreferencesModel(metricsViewer.getSharedPreferences(metric, Context.MODE_PRIVATE));
    }

    public void handlerMetrics(String metric){
        switch (metric){
            case "Login":
                int a = metricModel.getPreferences("M");
                int b = metricModel.getPreferences("T");
                break;
            case "Shake":
                int c =  metricModel.getPreferences("AtoD");
                int d =  metricModel.getPreferences("DtoA");
                break;
            default:
        }
    }

}
