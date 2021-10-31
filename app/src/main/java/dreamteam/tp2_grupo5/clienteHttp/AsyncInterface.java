package dreamteam.tp2_grupo5.clienteHttp;


import java.io.Serializable;

public interface AsyncInterface {
    void showToast(String msg);
    void activityTo(Class c);
    void activityToWithPayload(Class c, Serializable s);
    String getEndpoint();
    void finalize();
    boolean getConnection();
    void buttonEnabled(boolean b);
    void setTextToVisible();
    void hideText();
}
