package com.example.getit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button sign_up_button = findViewById(R.id.sign_up_button);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeSignUp();
            }
        });
    }

    public void MakeSignUp(){
        EditText email = findViewById(R.id.email);
        String emailText = email.getText().toString();
        EditText password = findViewById(R.id.password);
        String passwordText = password.getText().toString();
        UsuarioDAO usuarioDAO = new UsuarioDAO(getBaseContext());

        try {
            usuarioDAO.SignUp(emailText,passwordText);
            //show toast
            Context context = getApplicationContext();
            CharSequence text = "Ahora puedes iniciar sesión";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            //
            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            finish();  //Kill the activity from which you will go to next activity
            startActivity(i);

        } catch (DAOException e) {
            //show toast
            Context context = getApplicationContext();
            CharSequence text = e.getMessage();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            //
        }
    }
}
