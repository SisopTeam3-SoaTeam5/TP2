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

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.session.SessionMapper;

public class HttpRequest extends AsyncTask<String, String, String> {

    private final JSONObject postData;
    private Exception exception;
    Map<String, String> headers;
    private int statusCode;

    public HttpRequest(Map<String, String> postData, Map<String, String> headers) {
        this.postData = postData != null ? new JSONObject(postData) : null;
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
    protected String doInBackground(String... params) {
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

    protected void onPostExecute(String response) {
        try {
            super.onPostExecute(response);
            if (exception != null) {
                Log.i("Debug", exception.toString());
                return;
            }
            if (statusCode == HttpURLConnection.HTTP_OK) {  //capaz taria bueno mandar url por parametro y evitar la funcion getEndpoint
                tokenRefreshed(response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tokenRefreshed(String response) {
        Gson gson = new Gson();
        SessionMapper sessionMap = gson.fromJson(response, SessionMapper.class);
        SessionManager.token = sessionMap.token;
        SessionManager.tokenRefresh = sessionMap.tokenRefresh;
        Log.i("Debug", "Token refreshed :D");
    }
}
