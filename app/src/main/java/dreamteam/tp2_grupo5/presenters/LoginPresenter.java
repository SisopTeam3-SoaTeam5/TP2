package dreamteam.tp2_grupo5.presenters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dreamteam.tp2_grupo5.Constants;
import dreamteam.tp2_grupo5.clienteHttp.AsyncInterface;
import dreamteam.tp2_grupo5.clienteHttp.HttpPostStartSession;
import dreamteam.tp2_grupo5.models.LoginModel;
import dreamteam.tp2_grupo5.views.Login;
import dreamteam.tp2_grupo5.views.Registration;

public class LoginPresenter implements AsyncInterface {
    Login login;
    LoginModel loginModel;

    public LoginPresenter(Login login) {
        this.login = login;
        this.loginModel = new LoginModel(login.getSharedPreferences());
    }

    public void goToRegistration(){
        Intent intent = new Intent(login, Registration.class);
        login.startActivity(intent);
    }

    public  void handleLogin(String emailText, String passwordText){
        if(emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            login.showToast(Constants.emailValidationError);
            return;
        }

        if(passwordText.isEmpty() || passwordText.length()<Constants.passwordMinLength) {
           login.showToast(Constants.passwordLengthMsg);
            return;
        }

        Map<String, String> values = new HashMap<>();
        values.put("email", emailText);
        values.put("password", passwordText);

        HttpPostStartSession task = new HttpPostStartSession(values, this, loginModel);
        task.execute(Constants.baseUrl+Constants.login);

    }

    @Override
    public void showToast(String msg) {
        login.showToast(msg);
    }

    @Override
    public void activityTo(Class c){
        Intent intent = new Intent(login, c);
        intent.putExtra("event","Login");
        intent.putExtra("description","A user has logged in to the system");
        login.startActivity(intent);
    }

    @Override
    public void activityToWithPayload(Class c, Serializable s) {
        Intent intent = new Intent(login, c);
        intent.putExtra("payload", s);
        login.startActivity(intent);
    }

    @Override
    public String getEndpoint() {
        return Constants.login;
    }

    @Override
    public void finalize() {
        login.finish();
    }

    @Override
    public boolean getConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void buttonEnabled(boolean b) {
        login.buttonEnabled(b);
    }

    @Override
    public void setTextToVisible() {
        login.setTextToVisible();
    }

    @Override
    public void hideText() {
        login.hideText();
    }

}
