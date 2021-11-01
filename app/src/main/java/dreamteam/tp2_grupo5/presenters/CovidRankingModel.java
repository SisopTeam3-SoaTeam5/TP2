package dreamteam.tp2_grupo5.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.comparators.RankingItemComparator;
import dreamteam.tp2_grupo5.models.RankingItem;
import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.views.CovidRanking;
import dreamteam.tp2_grupo5.CustomAdapter;

public class CovidRankingModel implements SensorEventListener, AsyncInterface {

    CovidRanking activity;
    HashMap<Integer, RankingItem> stats;
    CustomAdapter customAdapter;
    SensorManager sensor;
    SharedPreferences sharedPreferences;
    final float maxLightValue = 50;
    float previousColor = -1;
    boolean desc = true;


    public CovidRankingModel(CovidRanking activity) {
        this.activity = activity;
        sensor = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        registerSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensor(Sensor.TYPE_LIGHT);
        sharedPreferences = activity.getSharedPreferences("Shake", Context.MODE_PRIVATE);
    }

    public void setStats() {
        Intent intent = activity.getIntent();
        stats = (HashMap<Integer, RankingItem>) intent.getSerializableExtra("payload");
        customAdapter = new CustomAdapter(stats, activity);
        activity.setAdapter(customAdapter);
    }

    private void registerSensor(int sens) {
        boolean done;
        Sensor selectedSensor = sensor.getDefaultSensor(sens);
        done = sensor.registerListener(this, selectedSensor, SensorManager.SENSOR_DELAY_NORMAL);

        String sensName;
        if (sens == Sensor.TYPE_ACCELEROMETER)
            sensName = "Acelerómetro";
        else {
            sensName = "Sensor de Luz";
        }
        if (!done)
            activity.showToast("tristelme no tenes este sensor: " + sensName);
        Log.i("sensor", "register");
    }

    public void unregisterSensor() {
        sensor.unregisterListener(this);
        Log.i("sensor", "unregister");
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
                    SessionManager.registerEvent(this, "Shake", "User re-ordered covid ranking", activity);
                }
            } else if (sensorType == Sensor.TYPE_LIGHT) {
                changeBackgroundColor(values[0]);
            }
        }

    }

    private void changeBackgroundColor(float color) {
        registerLightEvent(color);
        previousColor = color;
        float scaledColor = 255 * color / maxLightValue;
        if (scaledColor > 255)
            scaledColor = 255;
        String hexColor = Integer.toHexString((int) scaledColor);
        if (hexColor.length() == 1)
            hexColor = '0' + hexColor;         //transforma, por ejemplo, F en 0F. parseColor requiere 6 caracteres;
        hexColor = "#" + hexColor + hexColor + hexColor;
        activity.changeColor(Color.parseColor(hexColor));
    }

    private void registerLightEvent(float color) {
        float mitad = maxLightValue / 2;
        if (previousColor >= 0) {
            if (previousColor <= mitad && color > mitad)
                SessionManager.registerEvent(this, "High light", "The light read by the sensor went from the lower half to the upper half of the sensor",activity);
            else if (color <= mitad && previousColor > mitad)
                SessionManager.registerEvent(this, "Low light", "the light read by the sensor went from the upper to the lower half of the sensor",activity);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
            writePreferences("DtoA", value);
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
            writePreferences("AtoD", value);

            stats.putAll(newStats);
        }
        desc = !desc;
    }

    private void writePreferences(String key, Integer value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private Integer getPreferences(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    @Override
    public void showToast(String msg) {
        activity.showToast(msg);
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
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void buttonEnabled(boolean b) {

    }

    @Override
    public void setTextToVisible() {

    }

    @Override
    public void hideText() {

    }

}