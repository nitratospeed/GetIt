package com.example.getit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button sign_up_button = findViewById(R.id.sign_up_button);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeSignUp(new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
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
                    }});
            }
        });
    }

    public void MakeSignUp(final VolleyCallBack callBack){
        EditText email = findViewById(R.id.email);
        final String emailText = email.getText().toString();
        EditText password = findViewById(R.id.password);
        final String passwordText = password.getText().toString();
        //UsuarioDAO usuarioDAO = new UsuarioDAO(getBaseContext());

        try {
            //usuarioDAO.SignUp(emailText,passwordText);
            // Instantiate the RequestQueue.
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("getitrest.azurewebsites.net")
                    .appendPath("api")
                    .appendPath("users");
            String url = builder.build().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   Log.e("OK",response.toString());
                   callBack.onSuccess();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error",error.toString());
                }
            }) {
                @Override
                public byte[] getBody() {
                    HashMap<String, String> params2 = new HashMap<String, String>();
                    params2.put("Email", emailText);
                    params2.put("Password", passwordText);
                    return new JSONObject(params2).toString().getBytes();
                }

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

    public interface VolleyCallBack {
        void onSuccess();
    }
}
