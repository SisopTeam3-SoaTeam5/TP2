package dreamteam.tp2_grupo5.pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.Login;

public class PatternListener implements PatternLockViewListener {
    PatternLockView patternLockView;
    String key;
    Context context;
    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        String finalPattern = PatternLockUtils.patternToString(patternLockView, pattern);
        if(finalPattern.equals(key)){
            Toast.makeText(context, Constants.correctPattern, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        }else{
            Toast.makeText(context, Constants.wrongPattern, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCleared() {

    }

    public void setPatternLockView(PatternLockView patternLockView) {
        this.patternLockView = patternLockView;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
