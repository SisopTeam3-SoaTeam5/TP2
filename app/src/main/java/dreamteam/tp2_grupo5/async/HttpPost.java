package dreamteam.tp2_grupo5.async;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

import dreamteam.tp2_grupo5.Homepage;
import dreamteam.tp2_grupo5.Registration;

public class HttpPost extends AsyncTask<String, String, String> {

    private JSONObject postData;
    private int statusCode;

    public HttpPost(Map<String, String> postData) {
        this.postData = new JSONObject(postData);
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

            if (statusCode ==  200) {

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                String response = convertInputStreamToString(inputStream);

                System.out.println(response);

            }else {
                System.out.println("Status: "+statusCode);
            }

        }catch (Exception e){
            Log.d(TAG,e.getLocalizedMessage());
        }
        return null;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
