package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import dreamteam.tp2_grupo5.models.RankingItem;

public class CovidRanking extends AppCompatActivity implements SensorEventListener {
    RecyclerView recyclerView;
    HashMap<Integer, RankingItem> stats;
    private SensorManager sensor;
    private final static float ACC = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_ranking);
        Intent intent = getIntent();
        stats = (HashMap<Integer, RankingItem>) intent.getSerializableExtra("payload");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.RecycleView);
        recyclerView.setLayoutManager(manager);
        CustomAdapter customAdapter = new CustomAdapter(stats, this);
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
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        float[] values = event.values;

        if (sensorType == Sensor.TYPE_ACCELEROMETER)
        {
            if ((Math.abs(values[0]) > ACC || Math.abs(values[1]) > ACC || Math.abs(values[2]) > ACC))
            {
                Log.i("sensor", "running");
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

}