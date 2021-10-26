package dreamteam.tp2_grupo5.clienteHttp;

import static android.content.ContentValues.TAG;

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
import dreamteam.tp2_grupo5.Homepage;
import dreamteam.tp2_grupo5.session.SessionManager;
import dreamteam.tp2_grupo5.session.SessionMapper;


public class HttpPostStartSesion extends AsyncTask<String, String, String> {

    private final JSONObject postData;
    private Exception exception;
    private final AsyncInterface caller;
    private int statusCode;

    public HttpPostStartSesion(Map<String, String> postData, Context a) {
        Log.i("Debug", "Init");
        this.postData = new JSONObject(postData);
        this.exception = null;
        this.caller = (AsyncInterface) a;
        Log.i("Debug", "End");
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
            Log.i("Debug", "arranca");
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
            Log.i("Debug", response);
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
                caller.showToast("Error: " + exception.toString());
                return;
            }
            if (statusCode == HttpURLConnection.HTTP_OK) {  //capaz taria bueno mandar url por parametro y evitar la funcion getEndpoint
                handleLoginAndRegistration(response);
                if (caller.getEndpoint().equals(Constants.register))
                    caller.showToast(Constants.successRegister);
                else if (caller.getEndpoint().equals(Constants.login))
                    caller.showToast(Constants.successLoggin);
                return;
            }
            caller.showToast("Error: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLoginAndRegistration(String response) {
        Gson gson = new Gson();
        SessionMapper sessionMap = gson.fromJson(response, SessionMapper.class);
        SessionManager.token = sessionMap.token;
        SessionManager.tokenRefresh = sessionMap.tokenRefresh;
        SessionManager.setTokenRefreshAlarm((Context) caller);
        System.out.println("created session: " + SessionManager.token);
        caller.activityTo(Homepage.class);
        caller.finalize();
    }


}
