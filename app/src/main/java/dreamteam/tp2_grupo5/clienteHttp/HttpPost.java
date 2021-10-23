package dreamteam.tp2_grupo5.clienteHttp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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


public class HttpPost extends AsyncTask<String, String, String> {

    private final JSONObject postData;
    private Exception exception;
    private AsyncInterface caller;
    private int statusCode;

    public HttpPost(Map<String, String> postData, Context a) {
        this.postData = new JSONObject(postData);
        this.exception = null;
        this.caller = (AsyncInterface) a;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected String doInBackground(String ...params) {
        try{
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            statusCode = httpURLConnection.getResponseCode();
            String response;

            if (statusCode ==  200 && caller.getEndpoint().equals(Constants.registration)) {
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                response = convertInputStreamToString(inputStream);
            }else {
                response = httpURLConnection.getResponseMessage();
            }
            return response;

        }catch (Exception e){
            exception=e;
            Log.d(TAG,e.getLocalizedMessage());
            return null;
        }

    }

    protected void onPostExecute(String response){
        try{
            super.onPostExecute(response);
            if( exception != null){
                caller.showToast("Error: " + exception.toString());
                return;
            }
            if(statusCode == 200){
                caller.showToast(Constants.welcomeMsg);
                caller.activityTo(Homepage.class);
                return;
            }

            caller.showToast("Error: " + response);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
