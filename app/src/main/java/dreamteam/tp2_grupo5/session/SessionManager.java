package dreamteam.tp2_grupo5.session;


import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

import dreamteam.tp2_grupo5.Login;

public class SessionManager implements Serializable {

    public static String token;
    public static String tokenRefresh;

    public SessionManager(String token, String tokenRefresh) {
        SessionManager.token = token;
        SessionManager.tokenRefresh =tokenRefresh;
    }

    public static boolean isLoggedIn(){
        return !token.isEmpty();
    }

    public static void logout(Context context){
        token="";
        tokenRefresh="";
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
    }


    //hacer el metodo cron aca
}
