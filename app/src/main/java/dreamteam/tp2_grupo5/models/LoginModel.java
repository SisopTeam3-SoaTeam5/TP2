package dreamteam.tp2_grupo5.models;

import android.content.SharedPreferences;

import dreamteam.tp2_grupo5.clienteHttp.MetricsInterface;

public class LoginModel implements MetricsInterface {
    SharedPreferences sharedPreferences;

    public LoginModel(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void writePreferences(String key, Integer value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    @Override
    public Integer getPreferences(String key) {
        return sharedPreferences.getInt(key, 0);
    }

}
