package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CovidRanking extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_ranking);
        //GridLayoutManager manager = new GridLayoutManager(this, 3,GridLayoutManager.VERTICAL, false);
        ArrayList<String> fakeData = new ArrayList<String>();
        fakeData.add("A");
        fakeData.add("B");
        fakeData.add("C");
        fakeData.add("D");
        recyclerView = findViewById(R.id.RecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        CustomAdapter customAdapter = new CustomAdapter(fakeData, this);
        recyclerView.setAdapter(customAdapter);
    }
}