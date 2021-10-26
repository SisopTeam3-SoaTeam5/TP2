package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.clienteHttp.HttpPostStartSesion;

public class Login extends AppCompatActivity implements AsyncInterface {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
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

        HttpPostStartSesion task = new HttpPostStartSesion(values, Login.this);
        task.execute(Constants.baseUrl+Constants.login);
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void activityTo(Class c){
        Intent intent = new Intent(Login.this, c);
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
}