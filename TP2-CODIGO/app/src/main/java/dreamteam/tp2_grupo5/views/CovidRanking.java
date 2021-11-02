package dreamteam.tp2_grupo5.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import dreamteam.tp2_grupo5.R;
import dreamteam.tp2_grupo5.presenters.CovidRankingPresenter;
import dreamteam.tp2_grupo5.CustomAdapter;

public class CovidRanking extends AppCompatActivity {

    RecyclerView recyclerView;
    CovidRankingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Debug","llega al ranking");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_ranking);
        presenter = new CovidRankingPresenter(this);
        recyclerView = findViewById(R.id.RecycleView);
        presenter.setStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.listenSensors();
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
        super.onStop();
        presenter.unregisterSensor();
    }
}

