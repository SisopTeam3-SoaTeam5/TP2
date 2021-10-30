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
import java.util.Map;

import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.session.SessionMapper;

public class HttpRequest extends AsyncTask<String, String, String> {

    private final JSONObject postData;
    private Exception exception;
    Map<String, String> headers;
    private int statusCode;
    private boolean isConnected;
    private final AsyncInterface caller;

    public HttpRequest(Map<String, String> postData, Map<String, String> headers, Context caller) {
        this.postData = postData != null ? new JSONObject(postData) : null;
        this.caller = (AsyncInterface) caller;
        this.exception = null;
        this.headers = headers;
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
                if (headers != null)
                    for (String key : headers.keySet()) {
                        httpURLConnection.setRequestProperty(key, headers.get(key));
                    }
                httpURLConnection.setRequestMethod(params[1]);

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                if (postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                    writer.close();
                }

                httpURLConnection.connect();
                statusCode = httpURLConnection.getResponseCode();
                String response;

                if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
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
        System.out.println(response);
        if (isConnected) {
            try {
                super.onPostExecute(response);
                if (exception != null) {
                    Log.i("Debug", exception.toString());
                    return;
                }
                if (postData == null) {   //si postData es null se refrescó el token
                    if (statusCode == HttpURLConnection.HTTP_OK)
                        tokenRefreshed(response);                                       //de lo contrario se registró evento
                } else  if(statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED){
                        Log.i("Debug",response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            caller.showToast("Session expired" + System.lineSeparator() +
                    "No internet connection");
            SessionManager.logout((Context)caller);
        }
    }

    private void tokenRefreshed(String response) {
        SessionManager.registerEvent((Context) caller,"Token refreshed", "The user token was refreshed");
        Gson gson = new Gson();
        SessionMapper sessionMap = gson.fromJson(response, SessionMapper.class);
        SessionManager.token = sessionMap.token;
        SessionManager.tokenRefresh = sessionMap.tokenRefresh;
        Log.i("Debug", "Token refreshed :D");
    }
}
