package dreamteam.tp2_grupo5.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import dreamteam.tp2_grupo5.R;
import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.presenters.CovidRankingModel;

public class CovidRanking extends AppCompatActivity {

    RecyclerView recyclerView;
    CovidRankingModel presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Debug","llega al ranking");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_ranking);
        presenter = new CovidRankingModel(this);
        recyclerView = findViewById(R.id.RecycleView);
        presenter.setStats();
    }

    public void setAdapter(CustomAdapter customAdapter) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(customAdapter);
    }

    public void changeColor(int color) {
        recyclerView.setBackgroundColor(color);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        presenter.unregisterSensor();
        super.onStop();
    }
}

