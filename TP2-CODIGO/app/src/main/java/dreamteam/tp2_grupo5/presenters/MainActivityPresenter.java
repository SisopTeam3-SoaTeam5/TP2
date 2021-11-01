package dreamteam.tp2_grupo5.presenters;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import dreamteam.tp2_grupo5.views.MainActivity;

public class MainActivityPresenter {

    Intent batteryStatus;
    MainActivity activity;

    public MainActivityPresenter(MainActivity activity) {
        this.activity = activity;
    }

    public void setBatteryStatus() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = activity.registerReceiver(null, ifilter);
        this.batteryStatus = batteryStatus;
    }

    public void getBatteryLevel() {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float) scale;
        String valueText = (int) batteryPct + "%";
        activity.setBatteryBar((int) batteryPct);
        activity.setBatteryText(valueText);
    }
}


