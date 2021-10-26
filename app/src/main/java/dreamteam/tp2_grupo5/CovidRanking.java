package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

import dreamteam.tp2_grupo5.models.RankingItem;

public class CovidRanking extends AppCompatActivity  {
    RecyclerView recyclerView;
    HashMap<Integer, RankingItem> stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_ranking);
        Intent intent = getIntent();
        stats = (HashMap<Integer, RankingItem>) intent.getSerializableExtra("payload");
        System.out.println("stats: "+ stats);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.RecycleView);
        recyclerView.setLayoutManager(manager);
        CustomAdapter customAdapter = new CustomAdapter(stats, this);
        recyclerView.setAdapter(customAdapter);
    }

}