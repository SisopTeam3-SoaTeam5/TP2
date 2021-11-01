package dreamteam.tp2_grupo5.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.clienteHttp.HttpRequest;
import dreamteam.tp2_grupo5.presenters.HomepagePresenter;
import dreamteam.tp2_grupo5.views.Homepage;

public class TokenRefresh extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + SessionManager.tokenRefresh);
        HttpRequest task = new HttpRequest(null, header,new HomepagePresenter((Homepage) context),context);
        task.execute(Constants.baseUrl + Constants.refresh,"PUT");

        SessionManager.setTokenRefreshAlarm(context);
    }

}
