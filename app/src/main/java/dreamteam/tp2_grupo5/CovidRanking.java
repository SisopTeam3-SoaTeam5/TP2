package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.comparators.RankingItemComparator;
import dreamteam.tp2_grupo5.models.RankingItem;
import dreamteam.tp2_grupo5.session.SessionManager;

public class CovidRanking extends AppCompatActivity implements SensorEventListener, AsyncInterface {

    RecyclerView recyclerView;
    HashMap<Integer, RankingItem> stats;
    SensorManager sensor;
    boolean desc = true;
    CustomAdapter customAdapter;
    float maxLightValue;
    float previousColor = -1;


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
        //  unregisterSensor();
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
                    SessionManager.registerEvent(this, "Shake", "User re-ordered covid ranking");
                }
            } else if (sensorType == Sensor.TYPE_LIGHT) {
                changeBackgroundColor(values[0]);
            }
        }

    }

    private void changeBackgroundColor(float color) {
        float scaledColor = 255 * color / maxLightValue;
        if (previousColor >= 0) {
            if (previousColor <= 127 && scaledColor > 127)
                SessionManager.registerEvent(this,"High light","The light read by the sensor went from the lower half to the upper half of the sensor");
            else if(scaledColor <= 127 && previousColor > 127)
                SessionManager.registerEvent(this,"Low light","the light read by the sensor went from the upper to the lower half of the sensor");
        }
        previousColor=scaledColor;
        String hexColor = Integer.toHexString((int) scaledColor);
        if (hexColor.length() == 1)
            hexColor = '0'+hexColor;         //transforma, por ejemplo, F en 0F. parseColor requiere 6 caracteres;
        hexColor = "#" + hexColor + hexColor + hexColor;
        Log.i("Debug",hexColor);
        recyclerView.setBackgroundColor(Color.parseColor(hexColor));
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
        int max = values.size() - 1;
        stats.clear();
        if (desc) {
            values.sort(new RankingItemComparator());
            HashMap<Integer, RankingItem> newStats = new HashMap<>();
            Integer i = 0;

            for (RankingItem item : values) {
                newStats.put(i, item);
                i++;
            }

            stats.putAll(newStats);
        } else {

            values.sort(new RankingItemComparator().reversed());
            HashMap<Integer, RankingItem> newStats = new HashMap<>();
            Integer i = 0;

            for (RankingItem item : values) {
                newStats.put(i, item);
                i++;
            }

            stats.putAll(newStats);
        }
        desc = !desc;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void activityTo(Class c) {

    }

    @Override
    public void activityToWithPayload(Class c, Serializable s) {

    }

    @Override
    public String getEndpoint() {
        return null;
    }

    @Override
    public void finalize() {

    }

    @Override
    public boolean getConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

