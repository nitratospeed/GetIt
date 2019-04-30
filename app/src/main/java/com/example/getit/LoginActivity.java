package com.example.getit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private String NombresApellidos;
    private String Email;
    private int UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        Button sign_in_button = findViewById(R.id.email_sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeLogIn(new LoginActivity.VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        //save session in shared pref
                        SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("NombresApellidos", NombresApellidos);
                        editor.putString("Email", Email);
                        editor.putInt("UserId", UserId);
                        editor.putString("Longitud", "");
                        editor.putString("Latitud", "");
                        editor.commit();

                        //show toast
                        Context context = getApplicationContext();
                        CharSequence text = "¡Bienvenido " + Email + " !";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        //

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        finish();  //Kill the activity from which you will go to next activity
                        startActivity(i);

                    }});
            }
        });

        TextView mSignUpView = findViewById(R.id.crear_cuenta);
        mSignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignUp();
            }
        });

        TextView mRecuperarContrasena = findViewById(R.id.recuperar_contrasena);
        mRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRecuperarContrasena();
            }
        });
    }
    public void MakeLogIn(final VolleyCallBack callBack){

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        boolean cancel = false;
        View focusView = null;

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            try {

                RequestQueue requestQueue = Volley.newRequestQueue(this);

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("getitrest.azurewebsites.net")
                        .appendPath("api")
                        .appendPath("users")
                        .appendPath("login")
                        .appendQueryParameter("Email", email)
                        .appendQueryParameter("Password", password);
                String url = builder.build().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            NombresApellidos = jsonObject.getString("Name") + ' ' + jsonObject.getString("LastName");
                            Email = jsonObject.getString("Email");
                            UserId = jsonObject.getInt("UserId");
                            callBack.onSuccess();
                        }
                        catch (Exception error)
                        {
                            Log.e("Error",error.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error",error.toString());
                        mPasswordView.setError(getString(R.string.error_incorrect_email_or_password));
                        mPasswordView.requestFocus();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                requestQueue.add(stringRequest);
            } catch (Exception e) {
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return (password.length() >= 6 && password.length() <= 10);
    }

    private void gotoSignUp(){
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    private void gotoRecuperarContrasena(){
        Intent i = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
        startActivity(i);
    }

    public interface VolleyCallBack {
        void onSuccess();
    }
}
