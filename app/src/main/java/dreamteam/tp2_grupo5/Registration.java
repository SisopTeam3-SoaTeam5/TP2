package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
        if (!commissionText.equals("2900") && !commissionText.equals("3900"))
            showError = addErrorText(showError, "Commission should be 2900 or 3900");

        String nameText = name.getText().toString();
        String lastnameText = lastname.getText().toString();
        String dniText = dni.getText().toString();
        String groupText = group.getText().toString();


        if (nameText.isEmpty() || lastnameText.isEmpty() || dniText.isEmpty() || groupText.isEmpty())
            showError = addErrorText(showError, "Fields should not be empty");



        if (!showError.equals(""))
            Toast.makeText(Registration.this, showError, Toast.LENGTH_SHORT).show();


        Map<String, String> values = new HashMap<>();
        values.put("env", "TEST");
        values.put("name", nameText);
        values.put("lastname", lastnameText);
        values.put("dni", dniText);
        values.put("email", emailText);
        values.put("password", passwordText);
        values.put("commission", commissionText);
        values.put("group", groupText);

        HttpPost task = new HttpPost(values, Registration.this);
        task.execute("http://so-unlam.net.ar/api/api/register");
    }

    private ValidationState validateEmail(String emailText) {

        if (emailText.isEmpty()) {
            return new ValidationState(false, "Email should not be empty");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return new ValidationState(false, "Should be a valid email");
        }

        return new ValidationState(true, "Success");
    }

    private ValidationState validatePassword(String passwordText, EditText repeatedPassword) {
        int minimumLength = 8; // <--Change this to a local env

        if (passwordText.isEmpty() || passwordText.length() < minimumLength) {
            return new ValidationState(false, "Password should be at least 8 char long");
        }

        if (!passwordText.equals(repeatedPassword.getText().toString())) {
            System.out.println(passwordText + "!=" + repeatedPassword.getText().toString());
            return new ValidationState(false, "Doesn't match");
        }

        return new ValidationState(true, "Success");
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
}