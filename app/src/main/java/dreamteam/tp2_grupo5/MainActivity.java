package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;

import dreamteam.tp2_grupo5.pattern.PatternListener;

public class MainActivity extends AppCompatActivity {

    final String patternKey = "012";
    PatternLockView patternLockView;
    TextView batteryText;
    ProgressBar batteryBar;
    Intent batteryStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryText = findViewById(R.id.batteryText);
        batteryBar = findViewById(R.id.batteryBar);


        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = registerReceiver(null, ifilter);

        patternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        PatternListener patternListener = new PatternListener();
        patternListener.setPatternLockView(patternLockView);
        patternListener.setKey(patternKey);
        patternListener.setContext(MainActivity.this);
        patternLockView.addPatternLockListener(patternListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float) scale;
        String valueText=(int) batteryPct + "%";
        batteryBar.setProgress((int) batteryPct);
        batteryText.setText(valueText);
        Log.i("Debug", batteryPct + "");

    }
}