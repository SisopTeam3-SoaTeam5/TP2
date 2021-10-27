package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import dreamteam.tp2_grupo5.comparators.RankingItemComparator;
import dreamteam.tp2_grupo5.models.RankingItem;

public class CovidRanking extends AppCompatActivity implements SensorEventListener {

    RecyclerView recyclerView;
    HashMap<Integer, RankingItem> stats;
    SensorManager sensor;
    ProgressDialog nDialog;
    boolean desc = true;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_ranking);
        Intent intent = getIntent();
        stats = (HashMap<Integer, RankingItem>) intent.getSerializableExtra("payload");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.RecycleView);
        recyclerView.setLayoutManager(manager);
        customAdapter = new CustomAdapter(stats, this);
//        customAdapter.registerAdapterDataObserver(new DataSetObserver(){
//            @Override
//            public void onChanged(){
//                super.onChanged();
//            }
//        });
        recyclerView.setAdapter(customAdapter);
        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerSenser();
    }

    @Override
    protected void onStop()
    {
        unregisterSenser();
        super.onStop();
    }

    @Override
    protected void onPause() {
      //  unregisterSenser();
        super.onPause();
    }
                            //Considerar usar una bandera en vez de onPause y onResume
    @Override
    protected void onResume() {
       // registerSenser();
        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        float[] values = event.values;

        if (sensorType == Sensor.TYPE_ACCELEROMETER)
        {
            if ((Math.abs(values[0]) > Constants.ACC || Math.abs(values[1]) > Constants.ACC || Math.abs(values[2]) > Constants.ACC))
            {
                Log.i("sensor", "running");
                reSortRanking();
                Log.i("stats","re ordenando");
                customAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void registerSenser()
    {
        boolean done;
        done = sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        if (!done)
        {
            Toast.makeText(this, "tristelme no tenes este sensor", Toast.LENGTH_SHORT).show();
        }

        Log.i("sensor", "register");
    }

    private void unregisterSenser()
    {
        sensor.unregisterListener(this);
        Log.i("sensor", "unregister");
    }

    private void reSortRanking(){
        ArrayList<RankingItem> values = new ArrayList<>(stats.values());
        int max = values.size()-1;
        stats.clear();
        if(desc){
            values.sort(new RankingItemComparator());
                HashMap<Integer,RankingItem> newStats = new HashMap<>();
                Integer i = 0;

                for (RankingItem item : values) {
                    newStats.put(i,item);
                    i++;
                }

                stats.putAll(newStats);
            }else {

            values.sort(new RankingItemComparator().reversed());
                HashMap<Integer,RankingItem> newStats = new HashMap<>();
                Integer i = 0;

                for (RankingItem item : values) {
                    newStats.put(i,item);
                    i++;
                }

                stats.putAll(newStats);
            }
            desc = !desc;
        }
    }

