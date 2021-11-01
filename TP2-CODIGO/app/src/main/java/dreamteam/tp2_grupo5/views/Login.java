package dreamteam.tp2_grupo5.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dreamteam.tp2_grupo5.R;
import dreamteam.tp2_grupo5.presenters.LoginPresenter;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    SharedPreferences sharedPreferences;
    TextView loading;
    Button loginButton;
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loading = findViewById(R.id.textView7);
        loading.setVisibility(View.GONE);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.button);
        loginPresenter = new LoginPresenter(Login.this);
    }

    public void onRegistration(View view){
        loginPresenter.goToRegistration();
    }

    public void showToast(String msg) {
        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void buttonEnabled(boolean b){
        loginButton.setEnabled(b);
    }

    public void setTextToVisible(){
        loading.setVisibility(View.VISIBLE);
    }

    public void hideText(){
        loading.setVisibility(View.GONE);
    }

    public void loginHandler(View view){
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        loginPresenter.handleLogin(emailText,passwordText);
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

}