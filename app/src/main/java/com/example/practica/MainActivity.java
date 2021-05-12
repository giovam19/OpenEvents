package com.example.practica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText pass;
    private Button loginButton;
    private TextView regsitrationButton;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailLogin);
        pass = (EditText) findViewById(R.id.passLogin);

        loginButton = (Button) findViewById(R.id.nextLoginButton);
        regsitrationButton = (TextView) findViewById(R.id.registrationButton);
        accessToken = null;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser, passUser;

                emailUser = "algo@gmail.com";//email.getText().toString();
                passUser = "123456789";//pass.getText().toString();

                loginRequest(emailUser, passUser);

            }
        });

        regsitrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginRequest(String emailUser, String passUser) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/login/";

        JSONObject params = new JSONObject();
        try {
            params.put("email", emailUser);
            params.put("password", passUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest or = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    accessToken = response.getString("accessToken");
                    Intent intent = new Intent(MainActivity.this, ListEvents.class);
                    intent.putExtra("accessToken", accessToken);
                    intent.putExtra("email", emailUser);
                    System.out.println(accessToken);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("no acces token");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast;

                toast = Toast.makeText(MainActivity.this, "Usuario o Contraseña\nno validos", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 40);

                toast.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-type", "application/json");
                return params;
            }
        };

        queue.add(or);
    }

}