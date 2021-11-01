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
import dreamteam.tp2_grupo5.states.ValidationState;
import dreamteam.tp2_grupo5.views.Registration;

public class RegisterPresenter implements AsyncInterface {
    Registration registration;

    public RegisterPresenter(Registration context) {
        this.registration = context;
    }

    private ValidationState validateEmail(String emailText) {

        if (emailText.isEmpty()) {
            return new ValidationState(false, Constants.emptyEmail);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return new ValidationState(false, Constants.emailValidationError);
        }

        return new ValidationState(true, Constants.successMsg);
    }

    private ValidationState validatePassword(String passwordText, String repeatedPassword) {
        int minimumLength = Constants.passwordMinLength;

        if (passwordText.isEmpty() || passwordText.length() < minimumLength) {
            return new ValidationState(false, Constants.passwordLengthMsg);
        }

        if (!passwordText.equals(repeatedPassword)) {
            return new ValidationState(false, Constants.passwordMatchError);
        }

        return new ValidationState(true, Constants.successMsg);
    }

    private String addErrorText(String err, String msg) {
        if (!err.equals(""))
            err += System.lineSeparator();
        return err + msg;

    }

    public void handleRegistration(String emailText, String passwordText, String repeatedPassword, String commissionText, String nameText, String lastnameText, String dniText, String groupText) {
        String showError = "";
        ValidationState validateEmail = validateEmail(emailText);
        if (!validateEmail.getStatus())
            showError = addErrorText(showError, validateEmail.getMessage());

        ValidationState validatePassword = validatePassword(passwordText, repeatedPassword);
        if (!validatePassword.getStatus())
            showError = addErrorText(showError, validatePassword.getMessage());

        if (!Constants.commissionValues.contains(commissionText))
            showError = addErrorText(showError, Constants.commissionValidationError);

        if (nameText.isEmpty() || lastnameText.isEmpty() || dniText.isEmpty() || groupText.isEmpty())
            showError = addErrorText(showError, Constants.fieldsValidationError);

        if (!showError.equals("")) {
            registration.showToast(showError);
            return;
        }

        Map<String, String> values = new HashMap<>();
        values.put("env", Constants.prodEnv);
        values.put("name", nameText);
        values.put("lastname", lastnameText);
        values.put("dni", dniText);
        values.put("email", emailText);
        values.put("password", passwordText);
        values.put("commission", commissionText);
        values.put("group", groupText);

        HttpPostStartSession task = new HttpPostStartSession(values, this, null);
        task.execute(Constants.baseUrl + Constants.register);

    }

    @Override
    public void showToast(String msg) {
        registration.showToast(msg);
    }

    @Override
    public void activityTo(Class c) {
        Intent intent = new Intent(registration, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP        //Cierra todas las actividades abiertas
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("event", "Register");
        intent.putExtra("description", "New user registered in the system");
        registration.startActivity(intent);
    }

    @Override
    public void activityToWithPayload(Class c, Serializable s) {
        Intent intent = new Intent(registration, c);
        intent.putExtra("payload", s);
        registration.startActivity(intent);
    }

    @Override
    public String getEndpoint() {
        return Constants.register;
    }

    @Override
    public void finalize() {
        registration.finish();
    }

    @Override
    public boolean getConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) registration.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void buttonEnabled(boolean b) {

    }

    @Override
    public void setTextToVisible() {

    }

    @Override
    public void hideText() {

    }
}
