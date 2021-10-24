package dreamteam.tp2_grupo5.clienteHttp;

import android.content.Context;

public interface AsyncInterface {
    void showToast(String msg);
    void activityTo(Class c);
    String getEndpoint();
}
