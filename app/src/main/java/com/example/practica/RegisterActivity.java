package com.example.practica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextView nombre;
    private TextView apellido;
    private TextView email;
    private TextView password;
    private TextView cpass;
    private Button addImage;
    private ImageView userImage;
    private String imagePath;
    private Button next;

    private JSONObject registerRequest;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerRequest = null;
        imagePath = null;

        nombre = (TextView) findViewById(R.id.nombre);
        apellido = (TextView) findViewById(R.id.apellido);
        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);
        cpass = (TextView) findViewById(R.id.cpassword);
        addImage = (Button) findViewById(R.id.addImage);
        userImage = (ImageView) findViewById(R.id.userImageLoad);
        next = (Button) findViewById(R.id.button_next);

        userImage.setVisibility(View.INVISIBLE);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cargar datos a la api.
                String name, lastname, mail, pass, cpassw;
                Toast toast;

                name = nombre.getText().toString();
                lastname = apellido.getText().toString();
                mail = email.getText().toString();
                pass = password.getText().toString();
                cpassw = cpass.getText().toString();

                if (!pass.equals(cpassw)) {
                    toast = Toast.makeText(RegisterActivity.this, "Passwords dont match!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 40);
                    toast.show();
                } else if (imagePath == null) {
                    imagePath = " ";
                    makeRegister(name, lastname, mail, pass, imagePath);
                } else {
                    makeRegister(name, lastname, mail, pass, imagePath);
                    if (registerRequest == null) {
                        toast = Toast.makeText(RegisterActivity.this, "Register Error\nEmpty camp or Email already exists", Toast.LENGTH_SHORT);
                    } else {
                        toast = Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT);
                    }
                    toast.setGravity(Gravity.TOP, 0, 40);
                    toast.show();
                }
            }
        });
    }

    private void makeRegister(String name, String lastname, String email, String pass, String imagePath) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/";

        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("last_name", lastname);
            params.put("image", imagePath);
            params.put("email", email);
            params.put("password", pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                registerRequest = response;
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error onresponse " + error);
                System.out.println(registerRequest);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-type", "application/json");
                return params;
            }
        };
        queue.add(objectRequest);
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
            Picasso.get().load(uri).into(userImage);
            userImage.setVisibility(View.VISIBLE);
        }
    }
}