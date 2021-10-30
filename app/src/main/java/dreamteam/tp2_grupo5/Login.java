package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.clienteHttp.HttpPostStartSession;
import dreamteam.tp2_grupo5.clienteHttp.MetricsInterface;

public class Login extends AppCompatActivity implements AsyncInterface, MetricsInterface {

    EditText email;
    EditText password;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        sharedPreferences = getSharedPreferences("Login",Context.MODE_PRIVATE);
    }


    public void onRegistration(View view){
        Intent intent = new Intent(Login.this, Registration.class);
        startActivity(intent);
    }

    public void loginHandler(View view){

        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if(emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            Toast.makeText(Login.this, Constants.emailValidationError, Toast.LENGTH_SHORT).show();
            return;
        }

        if(passwordText.isEmpty() || passwordText.length()<Constants.passwordMinLength) {
            Toast.makeText(Login.this, Constants.passwordLengthMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> values = new HashMap<>();
        values.put("email", emailText);
        values.put("password", passwordText);

        HttpPostStartSession task = new HttpPostStartSession(values, Login.this);
        task.execute(Constants.baseUrl+Constants.login);
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void activityTo(Class c){
        Intent intent = new Intent(Login.this, c);
        intent.putExtra("event","Login");
        intent.putExtra("description","A user has logged in to the system");
        startActivity(intent);
    }

    @Override
    public void activityToWithPayload(Class c, Serializable s) {
        Intent intent = new Intent(Login.this, c);
        intent.putExtra("payload", s);
        startActivity(intent);
    }

    @Override
    public String getEndpoint() {
        return Constants.login;
    }

    @Override
    public void finalize() {
        finish();
    }

    @Override
    public boolean getConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public void writePreferences(String key, Integer value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    @Override
    public Integer getPreferences(String key) {
        return sharedPreferences.getInt(key, 0);
    }
}