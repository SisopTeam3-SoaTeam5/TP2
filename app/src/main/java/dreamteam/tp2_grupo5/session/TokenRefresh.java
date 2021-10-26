package dreamteam.tp2_grupo5.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.Registration;
import dreamteam.tp2_grupo5.clienteHttp.HttpPost;

public class TokenRefresh extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Debug", "recibido");
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + SessionManager.tokenRefresh);
        HttpPost task = new HttpPost(null, header, null);
        task.execute(Constants.baseUrl + Constants.refresh,"PUT");

        SessionManager.setTokenRefreshAlarm(context);
    }

}
