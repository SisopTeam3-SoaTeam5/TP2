package dreamteam.tp2_grupo5.clienteHttp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.Homepage;
import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.session.SessionMapper;


public class HttpPostStartSession extends AsyncTask<String, String, String> {

    private final JSONObject postData;
    private Exception exception;
    private final AsyncInterface caller;
    private final MetricsInterface metrics;
    private int statusCode;
    boolean isConnected;
    Calendar calendar;

    public HttpPostStartSession(Map<String, String> postData, Context a) {
        this.postData = new JSONObject(postData);
        this.exception = null;
        this.caller = (AsyncInterface) a;
        if (caller.getEndpoint().equals(Constants.login))
            this.metrics = (MetricsInterface) a;
        else
            metrics = null;
        this.calendar = Calendar.getInstance();
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPreExecute() {
        isConnected = caller.getConnection();
    }

    @Override
    protected String doInBackground(String... params) {
        if (isConnected) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
                writer.close();

                httpURLConnection.connect();
                statusCode = httpURLConnection.getResponseCode();
                String response;

                if (statusCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    response = convertInputStreamToString(inputStream);
                } else {
                    response = httpURLConnection.getResponseMessage();
                }
                httpURLConnection.disconnect();
                return response;
            } catch (Exception e) {
                exception = e;
                Log.i("Debug", e.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }


    protected void onPostExecute(String response) {
        if (isConnected) {
            try {
                super.onPostExecute(response);
                if (exception != null) {
                    caller.showToast("Error: " + exception.toString());
                    return;
                }
                if (statusCode == HttpURLConnection.HTTP_OK) {  //capaz taria bueno mandar url por parametro y evitar la funcion getEndpoint
                    handleLoginAndRegistration(response);
                    if (caller.getEndpoint().equals(Constants.register)) {
                        caller.showToast(Constants.successRegister);
                    } else if (caller.getEndpoint().equals(Constants.login)) {
                        caller.showToast(Constants.successLoggin);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        String key = "";
                        if (hour < 12) {
                            key = "M";
                        } else {
                            key = "T";
                        }
                        Integer value = metrics.getPreferences(key);
                        if (value == 0) {
                            value = 1;
                        } else {
                            value++;
                        }
                        metrics.writePreferences(key, value);
                    }
                    return;
                }
                caller.showToast("Error: " + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            caller.showToast("No internet connection" + System.lineSeparator() +
                    "Try again later");
        }
    }

    private void handleLoginAndRegistration(String response) {
        Gson gson = new Gson();
        SessionMapper sessionMap = gson.fromJson(response, SessionMapper.class);
        SessionManager.token = sessionMap.token;
        SessionManager.tokenRefresh = sessionMap.tokenRefresh;
        System.out.println("created session: " + SessionManager.token + "------------------------" + SessionManager.tokenRefresh);
        caller.activityTo(Homepage.class);
        caller.finalize();
    }


}
