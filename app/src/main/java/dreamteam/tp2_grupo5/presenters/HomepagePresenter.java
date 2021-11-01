package dreamteam.tp2_grupo5.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.clienteHttp.CoronavirusDataService;
import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.session.TokenRefresh;
import dreamteam.tp2_grupo5.views.Homepage;
import dreamteam.tp2_grupo5.views.MetricsViewer;

public class HomepagePresenter implements AsyncInterface {

    Homepage activity;

    public HomepagePresenter(Homepage activity) {
        this.activity = activity;
    }

    public void registerEvent() {
        Intent intent = activity.getIntent();
        Bundle params = intent.getExtras();
        SessionManager.registerEvent(this, params.getString("event"), params.getString("description"),activity);
    }

    public void scheduleAlarm() {
        SessionManager.setTokenRefreshAlarm(activity);
    }

    public void cancelAlarm() {
        SessionManager.cancelAlarmRefreshToken();
    }

    public void registerRefreshReceiver() {
        activity.registerReceiver(new TokenRefresh(), new IntentFilter("com.token.refresh"));
    }

    public boolean logout() {
        return SessionManager.logout(activity);
    }

    public void getCovidData() {
        CoronavirusDataService task = new CoronavirusDataService(this, activity);
        task.execute(Constants.virusDataUrl);
    }

    public void openMetricsPage(String metric) {
        Intent intent = new Intent(activity, MetricsViewer.class);
        intent.putExtra("metric", metric);
        activity.startActivity(intent);
    }

    @Override
    public void buttonEnabled(boolean b) {
        activity.enableButton(b);
    }

    @Override
    public void setTextToVisible() {
    }

    @Override
    public void hideText() {
    }

    @Override
    public boolean getConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void finalize() {
        activity.finish();
    }

    @Override
    public void showToast(String msg) {
        activity.showToast(msg);
    }

    @Override
    public void activityTo(Class c) {
        Intent intent = new Intent(activity, c);
        activity.startActivity(intent);
    }

    @Override
    public void activityToWithPayload(Class c, Serializable s) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("payload", s);
        Log.i("Debug","Envia hacia ranking");
        activity.startActivity(intent);
    }

    @Override
    public String getEndpoint() {
        return Constants.homePage;
    }
}
