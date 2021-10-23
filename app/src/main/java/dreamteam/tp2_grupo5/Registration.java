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

import dreamteam.tp2_grupo5.async.HttpPost;
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

        String emailText = email.getText().toString();
        ValidationState validateEmail = validateEmail(emailText);
        if(!validateEmail.getStatus()){
            ok = false;
            Toast.makeText(Registration.this, validateEmail.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String passwordText = password.getText().toString();
        ValidationState validatePassword = validatePassword(passwordText, repeatedPassword);

        if(!validatePassword.getStatus()){
            ok= false;
            Toast.makeText(Registration.this, validatePassword.getMessage() , Toast.LENGTH_SHORT).show(); //Chequear como mostrar ambos toast si suceden al mismo tiempo.
        }

        String commissionText = commission.getText().toString();

        if(!commissionText.equals("2900") && !commissionText.equals("3900")){
            ok = false;
            Toast.makeText(Registration.this, "Commission should be 2900 or 3900", Toast.LENGTH_SHORT).show();
        }
        String nameText = name.getText().toString();
        String lastnameText = lastname.getText().toString();
        String dniText = dni.getText().toString();
        String groupText = group.getText().toString();


        if(nameText.isEmpty() || lastnameText.isEmpty() || dniText.isEmpty() || groupText.isEmpty()){
            ok = false;
            Toast.makeText(Registration.this, "Fields should not be empty", Toast.LENGTH_SHORT).show();
        }


        Map<String,String> values = new HashMap<>();
        values.put("env","TEST");
        values.put("name",nameText);
        values.put("lastname",lastnameText);
        values.put("dni",dniText);
        values.put("email", emailText);
        values.put("password", passwordText );
        values.put("commission", commissionText);
        values.put("group",groupText);

        if(ok) {
            HttpPost task = new HttpPost(values);
            task.execute("http://so-unlam.net.ar/api/api/register");
//            if(task.getStatusCode() == 200){
//                Toast.makeText(Registration.this, "Welcome!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Registration.this, Homepage.class);
//                startActivity(intent);
//            } Mover esto
        }
    }

    private ValidationState validateEmail(String emailText){

        if(emailText.isEmpty()){
            return new ValidationState(false, "Email should not be empty");
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return new ValidationState(false, "Should be a valid email");
        }

        return new ValidationState(true,"Success");
    }

    private ValidationState validatePassword(String passwordText, EditText repeatedPassword){
        int minimumLength = 8; // <--Change this to a local env

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