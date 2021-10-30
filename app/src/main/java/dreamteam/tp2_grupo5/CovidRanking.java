package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import dreamteam.tp2_grupo5.comparators.RankingItemComparator;
import dreamteam.tp2_grupo5.models.RankingItem;

public class CovidRanking extends AppCompatActivity implements SensorEventListener {

    RecyclerView recyclerView;
    HashMap<Integer, RankingItem> stats;
    SensorManager sensor;
    boolean desc = true;
    CustomAdapter customAdapter;
    float maxLightValue;
    SharedPreferences sharedPreferences;

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
        recyclerView.setAdapter(customAdapter);
        sharedPreferences = getSharedPreferences("Shake", Context.MODE_PRIVATE);
        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onStop() {
        unregisterSensor();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //Considerar usar una bandera en vez de onPause y onResume
    @Override
    protected void onResume() {
        // registerSensor();
        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {

            int sensorType = event.sensor.getType();

            float[] values = event.values;

            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                if ((Math.abs(values[0]) > Constants.ACC || Math.abs(values[1]) > Constants.ACC || Math.abs(values[2]) > Constants.ACC)) {
                    Log.i("sensor", "running");
                    reSortRanking();
                    Log.i("stats", "re ordenando");
                    customAdapter.notifyDataSetChanged();
                }
            } else if (sensorType == Sensor.TYPE_LIGHT) {
                Log.i("sensor", values[0] + "");
                changeBackgroundColor(values[0]);
            }
        }

    }

    private void changeBackgroundColor(float color) {
        float scaledColor = 255 * color / maxLightValue;
        String hexColor = Integer.toHexString((int) scaledColor);
        if (hexColor.length() == 1)
            hexColor += hexColor;         //transforma, por ejemplo, F en FF. parseColor requiere 6 caracteres;
        hexColor = "#" + hexColor + hexColor + hexColor;
        recyclerView.setBackgroundColor(Color.parseColor(hexColor));
        customAdapter.changeTextColor(255 - (int) scaledColor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void registerSensor(int sens) {
        boolean done;
        Sensor selectedSensor = sensor.getDefaultSensor(sens);
        done = sensor.registerListener(this, selectedSensor, SensorManager.SENSOR_DELAY_NORMAL);

        String sensName;
        if (sens == Sensor.TYPE_ACCELEROMETER)
            sensName = "ACELERÓMETRO";
        else {
            maxLightValue = selectedSensor.getMaximumRange();
            sensName = "Sensor de Luz";
        }
        if (!done)
            Toast.makeText(this, "tristelme no tenes este sensor: " + sensName, Toast.LENGTH_SHORT).show();


        Log.i("sensor", "register");
    }

    private void unregisterSensor() {
        sensor.unregisterListener(this);
        Log.i("sensor", "unregister");
    }

    private void reSortRanking() {

        ArrayList<RankingItem> values = new ArrayList<>(stats.values());
        stats.clear();
        if (desc) {
            values.sort(new RankingItemComparator());
            HashMap<Integer, RankingItem> newStats = new HashMap<>();
            Integer i = 0;

            for (RankingItem item : values) {
                newStats.put(i, item);
                i++;
            }
            Integer value = getPreferences("DtoA");
            value++;
            writePreferences("DtoA",value);
            stats.putAll(newStats);
        } else {

            values.sort(new RankingItemComparator().reversed());
            HashMap<Integer, RankingItem> newStats = new HashMap<>();
            Integer i = 0;

            for (RankingItem item : values) {
                newStats.put(i, item);
                i++;
            }

            Integer value = getPreferences("AtoD");
            value++;
            writePreferences("AtoD",value);

            stats.putAll(newStats);
        }
        desc = !desc;
    }

    private void writePreferences(String key, Integer value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    private Integer getPreferences(String key) {
        return sharedPreferences.getInt(key, 0);
    }

}

