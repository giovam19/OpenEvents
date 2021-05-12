package com.example.practica;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewEventActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button create;
    private Button addImage;
    private EditText name;
    private EditText description;
    private EditText ubication;
    private EditText start;
    private EditText end;
    private EditText maxPeople;
    private EditText type;
    private String imagePath;
    private String accessToken;

    private JSONObject createRequest;

    private static final int PICK_IMAGE = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        accessToken = getIntent().getStringExtra("accessToken");
        createRequest = null;
        imagePath = null;
        name = (EditText) findViewById(R.id.nameNE);
        description = (EditText) findViewById(R.id.descriprtionNE);
        ubication = (EditText) findViewById(R.id.ubicationNE);
        start = (EditText) findViewById(R.id.startDateNE);
        end = (EditText) findViewById(R.id.endDateNE);
        maxPeople = (EditText) findViewById(R.id.maxPeopleNE);
        type = (EditText) findViewById(R.id.typeNE);
        backButton = (ImageView) findViewById(R.id.backButtonNE);
        create = (Button) findViewById(R.id.createNE);
        addImage = (Button) findViewById(R.id.addImageNE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = name.getText().toString();
                String descrip = description.getText().toString();
                String ubi = ubication.getText().toString();
                String strt = start.getText().toString();
                String nd = end.getText().toString();
                int mxplp = Integer.parseInt(maxPeople.getText().toString());
                String tpe = type.getText().toString();
                if (imagePath == null) {
                    imagePath = " ";
                }

                createEventAPI(nam, descrip, ubi, strt, nd, mxplp, tpe, imagePath);
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                startActivityForResult(intent, PICK_IMAGE);
            }
        });
    }

    private void createEventAPI(String nam, String descrip, String ubi, String strt, String nd, int maxppl, String type, String image) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/events/";

        JSONObject params = new JSONObject();
        try {
            params.put("name", nam);
            params.put("image", image);
            params.put("location", ubi);
            params.put("description", descrip);
            params.put("eventStart_date", strt);
            params.put("eventEnd_date", nd);
            params.put("n_participators", maxppl);
            params.put("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest or = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                createRequest = response;
                Toast toast = Toast.makeText(NewEventActivity.this, "Creation successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 40);
                toast.show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(NewEventActivity.this, "Creation Error\nPlease try again", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 40);
                toast.show();

                System.out.println("error onresponse " + error);
                System.out.println(createRequest);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer" + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
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