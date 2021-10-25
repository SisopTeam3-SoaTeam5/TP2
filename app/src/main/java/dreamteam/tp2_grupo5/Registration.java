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
import dreamteam.tp2_grupo5.clienteHttp.HttpPost;
import dreamteam.tp2_grupo5.states.ValidationState;

public class Registration extends AppCompatActivity implements AsyncInterface {

    EditText email;
    EditText password;
    EditText repeatedPassword;
    EditText name;
    EditText lastname;
    EditText dni;
    EditText commission;
    EditText group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        repeatedPassword = findViewById(R.id.editTextTextPassword2);
        name = findViewById(R.id.editTextTextPersonName);
        lastname = findViewById(R.id.editTextTextPersonName3);
        dni = findViewById(R.id.editTextNumber);
        commission = findViewById(R.id.editTextNumber2);
        group = findViewById(R.id.editTextNumber3);

    }

    public void registerHandler(View view) {

        String emailText = email.getText().toString();
        String showError = "";
        ValidationState validateEmail = validateEmail(emailText);
        if (!validateEmail.getStatus())
            showError = addErrorText(showError, validateEmail.getMessage());

        String passwordText = password.getText().toString();
        ValidationState validatePassword = validatePassword(passwordText, repeatedPassword);
        if (!validatePassword.getStatus())
            showError = addErrorText(showError, validatePassword.getMessage());

        String commissionText = commission.getText().toString();
        if (!Constants.commissionValues.contains(commissionText))
            showError = addErrorText(showError, Constants.commissionValidationError);

        String nameText = name.getText().toString();
        String lastnameText = lastname.getText().toString();
        String dniText = dni.getText().toString();
        String groupText = group.getText().toString();


        if (nameText.isEmpty() || lastnameText.isEmpty() || dniText.isEmpty() || groupText.isEmpty())
            showError = addErrorText(showError, Constants.fieldsValidationError);



        if (!showError.equals("")) {
            Toast.makeText(Registration.this, showError, Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, String> values = new HashMap<>();
        values.put("env", Constants.testEnv);
        values.put("name", nameText);
        values.put("lastname", lastnameText);
        values.put("dni", dniText);
        values.put("email", emailText);
        values.put("password", passwordText);
        values.put("commission", commissionText);
        values.put("group", groupText);

        HttpPost task = new HttpPost(values, Registration.this);
        task.execute(Constants.baseUrl + Constants.register);
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

    private ValidationState validatePassword(String passwordText, EditText repeatedPassword) {
        int minimumLength = Constants.passwordMinLength;

        if (passwordText.isEmpty() || passwordText.length() < minimumLength) {
            return new ValidationState(false, Constants.passwordLengthMsg);
        }

        if (!passwordText.equals(repeatedPassword.getText().toString())) {
            return new ValidationState(false, Constants.passwordMatchError);
        }

        return new ValidationState(true, Constants.successMsg);
    }

    private String addErrorText(String err, String msg) {
        if (!err.equals(""))
            err += System.lineSeparator();
        return err + msg;

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(Registration.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void activityTo(Class c){
        Intent intent = new Intent(Registration.this, c);
        startActivity(intent);
    }

    @Override
    public String getEndpoint(){
        return Constants.register;
    }

    @Override
    public void finalize() {
        finish();
    }
}