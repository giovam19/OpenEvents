package com.example.practica;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PerfilOption extends AppCompatActivity {
    private ImageView backButton;
    private TextView userName;
    private EditText name;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private Button image;
    private TextView numCreatedEvents;
    private TextView numParticipatedEvents;
    private TextView numContactedPeople;
    private Button save;

    private int eventosCreados;
    private int eventosParticipados;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButton);
        userName = (TextView) findViewById(R.id.userNamePO);
        name = (EditText) findViewById(R.id.userName);
        lastname = (EditText) findViewById(R.id.user);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.newPassword);
        repeatPassword = (EditText) findViewById(R.id.newPassword2);
        image = (Button) findViewById(R.id.newImage);
        numCreatedEvents = (TextView) findViewById(R.id.numEventosCreados);
        numParticipatedEvents = (TextView) findViewById(R.id.numEventosParticipados);
        numContactedPeople = (TextView) findViewById(R.id.numPersonasContactadas);
        save = (Button) findViewById(R.id.buttonSave);

        getNumCreated();
        getNumParticipated();

        userName.setText(User.getUser().getUserName());
        name.setHint(User.getUser().getName());
        lastname.setHint(User.getUser().getLastname());
        email.setHint(User.getUser().getEmail());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = name.getText().toString();
                String lastnam = lastname.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String repPass = repeatPassword.getText().toString();

                if (pass.equals(repPass)) {
                    actualiceUserData(nam, lastnam, mail, pass);
                    actUserRequest(pass);
                    userName.setText(User.getUser().getUserName());
                } else {
                    Toast toast = Toast.makeText(PerfilOption.this, "Passwords don't match!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });
    }

    private void actualiceUserData(String nam, String lastnam, String mail, String pass) {
        if (!nam.equals(User.getUser().getName()) && !nam.equals("") && !nam.equals(null)) {
            User.getUser().setName(nam);
        }
        if (!lastnam.equals(User.getUser().getLastname()) && !lastnam.equals("") && !lastnam.equals(null)) {
            User.getUser().setLastname(lastnam);
        }
        if (!mail.equals(User.getUser().getEmail()) && !mail.equals("") && !mail.equals(null)) {
            User.getUser().setEmail(mail);
        }
        if (!pass.equals(User.getUser().getPassword()) && !pass.equals("") && !pass.equals(null)) {
            User.getUser().setPassword(pass);
        }
        User.getUser().actUserName();
    }

    private void actUserRequest(String pass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/";

        JSONObject params = new JSONObject();
        try {
            params.put("name", User.getUser().getName());
            params.put("last_name", User.getUser().getLastname());
            params.put("email", User.getUser().getEmail());
            params.put("password", User.getUser().getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest or = new JsonObjectRequest(Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast toast = Toast.makeText(PerfilOption.this, "Actualizacion Succesfull!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 40);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(PerfilOption.this, "Actualization Error", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 40);
                toast.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getUser().getToken());
                return params;
            }
        };

        queue.add(or);
    }

    private void getNumCreated() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/"+User.getUser().getID()+"/events";

        JsonArrayRequest or = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                try {
                    eventosCreados = response.length();
                    numCreatedEvents.setText(String.valueOf(eventosCreados));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                eventosCreados = -1;
                System.out.println("error: "+error);
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(User.getUser().getID()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getUser().getToken());
                return params;
            }
        };

        queue.add(or);
    }

    private void getNumParticipated() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/"+User.getUser().getID()+"/assistances";

        JsonArrayRequest or = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                try {
                    eventosParticipados = response.length();
                    numParticipatedEvents.setText(String.valueOf(eventosParticipados));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                eventosParticipados = -1;
                System.out.println("error: "+error);
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(User.getUser().getID()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getUser().getToken());
                return params;
            }
        };

        queue.add(or);
    }
}
