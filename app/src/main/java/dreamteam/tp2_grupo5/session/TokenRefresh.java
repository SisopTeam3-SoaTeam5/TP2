package dreamteam.tp2_grupo5.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.clienteHttp.HttpPostStartSesion;
import dreamteam.tp2_grupo5.clienteHttp.HttpRequest;

public class TokenRefresh extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Debug", "recibido");
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + SessionManager.tokenRefresh);
        HttpRequest task = new HttpRequest(null, header);
        task.execute(Constants.baseUrl + Constants.refresh,"PUT");

        SessionManager.setTokenRefreshAlarm(context);
    }

}
