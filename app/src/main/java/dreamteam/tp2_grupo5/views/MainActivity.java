package dreamteam.tp2_grupo5.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;

import dreamteam.tp2_grupo5.R;
import dreamteam.tp2_grupo5.pattern.PatternListener;
import dreamteam.tp2_grupo5.presenters.MainActivityPresenter;

public class MainActivity extends AppCompatActivity {

    final String patternKey = "012";
    PatternLockView patternLockView;
    TextView batteryText;
    ProgressBar batteryBar;
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainActivityPresenter(this);
        setContentView(R.layout.activity_main);
        batteryText = findViewById(R.id.batteryText);
        batteryBar = findViewById(R.id.batteryBar);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        presenter.setBatteryStatus(batteryStatus);

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
        presenter.getBatteryLevel();
    }

    public void setBatteryBar(int batteryPct) {
        batteryBar.setProgress((int) batteryPct);
    }

    public void setBatteryText(String valueText) {
        batteryText.setText(valueText);

    }
}
