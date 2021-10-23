package dreamteam.tp2_grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import dreamteam.tp2_grupo5.states.ValidationState;

public class Registration extends AppCompatActivity {

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

    public void registerHandler(View view){
        boolean ok = true;
        ValidationState validateEmail = validateEmail(email);
        if(!validateEmail.getStatus()){
            ok = false;
            Toast.makeText(Registration.this, validateEmail.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ValidationState validatePassword = validatePassword(password, repeatedPassword);

        if(!validatePassword.getStatus()){
            ok= false;
            Toast.makeText(Registration.this, validatePassword.getMessage() , Toast.LENGTH_SHORT).show(); //Chequear como mostrar ambos toast si suceden al mismo tiempo.
        }

        String commissionText = commission.getText().toString();

        if(!commissionText.equals("2900") && !commissionText.equals("3900")){
            ok = false;
            Toast.makeText(Registration.this, "Commission should be 2900 oe 3900", Toast.LENGTH_SHORT).show();
        }

        if(ok) {
            Toast.makeText(Registration.this, "Welcome!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Registration.this, Login.class);
            startActivity(intent);
        }
    }

    private ValidationState validateEmail(EditText email){

        String emailText = email.getText().toString();

        if(emailText.isEmpty()){
            return new ValidationState(false, "Email should not be empty");
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return new ValidationState(false, "Should be a valid email");
        }

        return new ValidationState(true,"Success");
    }

    private ValidationState validatePassword(EditText password, EditText repeatedPassword){
        int minimumLength = 8;
        String passwordText = password.getText().toString();

        if(passwordText.isEmpty() || passwordText.length() < minimumLength ){
            return new ValidationState(false, "Password should be at least 8 char long");
        }

        if(!passwordText.equals(repeatedPassword.getText().toString())) {
            System.out.println(passwordText + "!=" + repeatedPassword.getText().toString());
            return new ValidationState(false,"Doesn't match");
        }

        return new ValidationState(true,"Success") ;
    }
}