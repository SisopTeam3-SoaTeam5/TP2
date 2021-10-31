package dreamteam.tp2_grupo5;

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

import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.clienteHttp.CoronavirusDataService;
import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.session.TokenRefresh;

public class Homepage extends AppCompatActivity implements AsyncInterface {

    Button covidRankingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle params=intent.getExtras();
        SessionManager.registerEvent(this, params.getString("event"), params.getString("description"));
        SessionManager.setTokenRefreshAlarm(this);
        setContentView(R.layout.activity_homepage);
        //registro receiver para alarma refresh token
        registerReceiver(new TokenRefresh(), new IntentFilter("com.token.refresh"));
        covidRankingButton = findViewById(R.id.button2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SessionManager.cancelAlarmRefreshToken();
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
            if (SessionManager.logout(this))
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCovidRankingHandler(View view) {
        CoronavirusDataService task = new CoronavirusDataService(Homepage.this);
        task.execute(Constants.virusDataUrl);
    }

    public void onLoginMetricsHandler(View view) {
        Intent intent = new Intent(Homepage.this, MetricsViewer.class);
        intent.putExtra("metric", "Login");
        startActivity(intent);
    }

    public void onShakeMetricsHandler(View view) {
        Intent intent = new Intent(Homepage.this, MetricsViewer.class);
        intent.putExtra("metric", "Shake");
        startActivity(intent);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(Homepage.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void activityTo(Class c) {
        Intent intent = new Intent(Homepage.this, c);
        startActivity(intent);
    }

    @Override
    public void activityToWithPayload(Class c, Serializable s) {
        Intent intent = new Intent(Homepage.this, c);
        intent.putExtra("payload", s);
        startActivity(intent);
    }

    @Override
    public String getEndpoint() {
        return Constants.homePage;
    }

    @Override
    public void finalize() {
        finish();
    }

    @Override
    public boolean getConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void buttonEnabled(boolean b) {
        covidRankingButton.setEnabled(b);
    }

}