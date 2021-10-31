package dreamteam.tp2_grupo5.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.R;
import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.clienteHttp.CoronavirusDataService;
import dreamteam.tp2_grupo5.presenters.HomepagePresenter;
import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.session.TokenRefresh;

public class Homepage extends AppCompatActivity {

    Button covidRankingButton;
    HomepagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomepagePresenter(this);
        presenter.registerEvent();
        presenter.scheduleAlarm();
        setContentView(R.layout.activity_homepage);
        presenter.registerRefreshReceiver();
        covidRankingButton = findViewById(R.id.button2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelAlarm();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            return presenter.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCovidRankingHandler(View view) {
        presenter.getCovidData();
    }

    public void onLoginMetricsHandler(View view) {
        presenter.openMetricsPage("Login");
    }

    public void onShakeMetricsHandler(View view) {
        presenter.openMetricsPage("Shake");
    }

    public void enableButton(boolean b) {
        covidRankingButton.setEnabled(b);
    }

    public void showToast(String msg) {
        Toast.makeText(Homepage.this, msg, Toast.LENGTH_SHORT).show();
    }

}