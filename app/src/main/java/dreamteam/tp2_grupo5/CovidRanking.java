package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_ranking);
        Intent intent = getIntent();
        stats = (HashMap<Integer, RankingItem>) intent.getSerializableExtra("payload");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.RecycleView);
        recyclerView.setLayoutManager(manager);
        setAdapter();
        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerSenser();
        setProgressBar();
    }

    @Override
    protected void onStop()
    {
        unregisterSenser();
        super.onStop();
    }

    @Override
    protected void onPause() {
        unregisterSenser();
        super.onPause();
    }
                            //Considerar usar una bandera en vez de onPause y onResume
    @Override
    protected void onResume() {
        registerSenser();
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
                nDialog.show();
                setAdapter();
                nDialog.dismiss();
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

    private void setAdapter(){
        if( stats != null ){
            reSortRanking();
        }
        CustomAdapter customAdapter = new CustomAdapter(stats, this);
        recyclerView.setAdapter(customAdapter);
    }

    private void setProgressBar(){
        nDialog = new ProgressDialog(CovidRanking.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Get Data");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
    }

    private void reSortRanking(){
        ArrayList<RankingItem> values = new ArrayList<>(stats.values());
        if(desc){
            values.sort(new RankingItemComparator());
                HashMap<Integer,RankingItem> newStats = new HashMap<>();
                Integer i = values.size() - 1;

                for (RankingItem item : values) {
                    newStats.put(i,item);
                    i--;
                }

                stats = newStats;
            }else {

            values.sort(new RankingItemComparator().reversed());
                HashMap<Integer,RankingItem> newStats = new HashMap<>();
                Integer i = 0;

                for (RankingItem item : values) {
                    newStats.put(i,item);
                    i++;
                }

                stats = newStats;

            }
            desc = !desc;
        }
    }

