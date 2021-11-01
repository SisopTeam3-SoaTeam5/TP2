package dreamteam.tp2_grupo5.session;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.views.Login;
import dreamteam.tp2_grupo5.clienteHttp.HttpRequest;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP        //Cierra todas las actividades abiertas
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).finish();
        return true;
    }

    public static void registerEvent(AsyncInterface caller, String evento, String description, Context context) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> values = new HashMap<>();
        header.put("Authorization", "Bearer " + SessionManager.tokenRefresh);
        values.put("env", Constants.prodEnv);
        values.put("type_events", evento);
        values.put("description", description);
        HttpRequest task = new HttpRequest(values, header, caller, context);
        task.execute(Constants.baseUrl + Constants.regEvent, "POST");
    }

    public static void setTokenRefreshAlarm(Context context) {
        if (alarmIntent == null) {
            Intent intent = new Intent("com.token.refresh");
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
    }

    public static void cancelAlarmRefreshToken() {
        alarm.cancel(alarmIntent);
    }
}
