package dreamteam.tp2_grupo5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.clienteHttp.CoronavirusDataService;
import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.session.TokenRefresh;

public class Homepage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        //registro receiver para alarma refresh token
        registerReceiver(new TokenRefresh(),new IntentFilter("com.token.refresh"));
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
                finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCovidRankingHandler(View view){
        CoronavirusDataService task = new CoronavirusDataService();
        task.execute(Constants.virusDataUrl);
        Intent intent = new Intent(this, CovidRanking.class);
        startActivity(intent);
    }
}