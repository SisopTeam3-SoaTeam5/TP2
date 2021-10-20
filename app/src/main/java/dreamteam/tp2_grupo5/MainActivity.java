package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andrognito.patternlockview.PatternLockView;

import dreamteam.tp2_grupo5.pattern.PatternListener;

public class MainActivity extends AppCompatActivity {

    String patternKey = "012";
    PatternLockView patternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        patternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        PatternListener patternListener = new PatternListener();
        patternListener.setPatternLockView(patternLockView);
        patternListener.setKey(patternKey);
        patternListener.setContext(MainActivity.this);
        patternLockView.addPatternLockListener(patternListener);
    }
}