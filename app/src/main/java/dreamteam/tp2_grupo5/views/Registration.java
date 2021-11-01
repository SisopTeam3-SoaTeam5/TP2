package dreamteam.tp2_grupo5.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import dreamteam.tp2_grupo5.R;
import dreamteam.tp2_grupo5.presenters.RegisterPresenter;

public class Registration extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText repeatedPassword;
    EditText name;
    EditText lastname;
    EditText dni;
    EditText commission;
    EditText group;

    RegisterPresenter startSessionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        startSessionPresenter = new RegisterPresenter(this);
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
        String passwordText = password.getText().toString();
        String repeatedPasswordText = repeatedPassword.getText().toString();
        String commissionText = commission.getText().toString();
        String nameText = name.getText().toString();
        String lastnameText = lastname.getText().toString();
        String dniText = dni.getText().toString();
        String groupText = group.getText().toString();
        startSessionPresenter.handleRegistration(emailText, passwordText, repeatedPasswordText, commissionText, nameText, lastnameText, dniText, groupText);
    }

    public void showToast(String msg) {
        Toast.makeText(Registration.this, msg, Toast.LENGTH_SHORT).show();
    }

}