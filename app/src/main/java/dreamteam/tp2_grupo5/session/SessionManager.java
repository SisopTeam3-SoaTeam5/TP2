package dreamteam.tp2_grupo5.session;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;

import dreamteam.tp2_grupo5.Login;

public class SessionManager implements Serializable {

    public static String token;
    public static String tokenRefresh;
    private static AlarmManager alarm;
    private static PendingIntent alarmIntent;

    public SessionManager(String token, String tokenRefresh) {
        SessionManager.token = token;
        SessionManager.tokenRefresh = tokenRefresh;
    }

    public static boolean isLoggedIn() {
        return !token.isEmpty();
    }

    public static boolean logout(Context context) {
        token = "";
        tokenRefresh = "";
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        return true;
    }


    public static void setTokenRefreshAlarm(Context context) {
        if (alarmIntent == null) {
            Intent intent = new Intent("com.token.refresh");
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        Log.i("Debug", "Alarma Registada");
    }

    public static void cancelAlarmRefreshToken() {
        alarm.cancel(alarmIntent);
    }
}
