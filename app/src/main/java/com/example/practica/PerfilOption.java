package com.example.practica;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PerfilOption extends AppCompatActivity {
    private ImageView backButton;
    private ImageView userImage;
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
    private String imagePath;

    private int eventosCreados;
    private int eventosParticipados;
    private int chatsIniciados;

    private static final int PICK_IMAGE = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButton);
        userImage = (ImageView) findViewById(R.id.userImageUO);
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
        getNumChats();

        userName.setText(User.getInstance().getUserName());
        name.setHint(User.getInstance().getName());
        lastname.setHint(User.getInstance().getLastname());
        email.setHint(User.getInstance().getEmail());
        if (User.getInstance().getImage().contains("https://") || User.getInstance().getImage().contains("http://"))
            Picasso.get().load(User.getInstance().getImage()).into(userImage);


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
                    userName.setText(User.getInstance().getUserName());
                } else {
                    Toast toast = Toast.makeText(PerfilOption.this, "Passwords don't match!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                startActivityForResult(intent, PICK_IMAGE);
            }
        });
    }

    private void actualiceUserData(String nam, String lastnam, String mail, String pass) {
        if (!nam.equals(User.getInstance().getName()) && !nam.equals("") && !nam.equals(null)) {
            User.getInstance().setName(nam);
        }
        if (!lastnam.equals(User.getInstance().getLastname()) && !lastnam.equals("") && !lastnam.equals(null)) {
            User.getInstance().setLastname(lastnam);
        }
        if (!mail.equals(User.getInstance().getEmail()) && !mail.equals("") && !mail.equals(null)) {
            User.getInstance().setEmail(mail);
        }
        if (!pass.equals(User.getInstance().getPassword()) && !pass.equals("") && !pass.equals(null)) {
            User.getInstance().setPassword(pass);
        }
        if (imagePath.contains("https://") || imagePath.contains("http://")) {
            User.getInstance().setImage(imagePath);
        }

        User.getInstance().actUserName();
    }

    private void actUserRequest(String pass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/";

        JSONObject params = new JSONObject();
        try {
            params.put("name", User.getInstance().getName());
            params.put("last_name", User.getInstance().getLastname());
            params.put("email", User.getInstance().getEmail());
            params.put("password", User.getInstance().getPassword());
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
                    if (User.getInstance().getImage().contains("https://") || User.getInstance().getImage().contains("http://"))
                        Picasso.get().load(User.getInstance().getImage()).into(userImage);

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
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }
        };

        queue.add(or);
    }

    private void getNumCreated() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/"+User.getInstance().getID()+"/events";

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
                params.put("ID", String.valueOf(User.getInstance().getID()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }
        };

        queue.add(or);
    }

    private void getNumParticipated() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/"+User.getInstance().getID()+"/assistances/finished";

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
                params.put("ID", String.valueOf(User.getInstance().getID()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }
        };

        queue.add(or);
    }

    private void getNumChats() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/messages/users";

        JsonArrayRequest or = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                try {
                    chatsIniciados = response.length();
                    numContactedPeople.setText(String.valueOf(chatsIniciados));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                chatsIniciados = -1;
                System.out.println("error: "+error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getInstance().getToken());
                return params;
            }
        };

        queue.add(or);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == PICK_IMAGE) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            imagePath = uri.getPath();
        }
    }
}
