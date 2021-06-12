package com.example.practica;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListChatActivity extends AppCompatActivity {
    private ImageView backButton;
    private RecyclerView eventList;
    private EditText search;
    private Button newChat;

    private JSONArray users;
    private ListUserAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chats);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButtonEvent);
        eventList = (RecyclerView) findViewById(R.id.usersList);
        search = (EditText) findViewById(R.id.searchUser);
        newChat = (Button) findViewById(R.id.newChat);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        users = new JSONArray();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(ListChatActivity.this, "Please write a Name!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                } else {
                    String value = search.getText().toString();
                    searchUserFromAPI(value);
                }
            }
        });

        for (int i = 0; i < 20; i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", (i+1));
                object.put("name", ("User "+(i+1)));

                users.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        updateUI();
        //getUsersFromAPI();
    }

    private void updateUI() {
        adapter = new ListUserAdapter(users);
        eventList.setAdapter(adapter);
    }

    private void getUsersFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/messages/users";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    users = response;
                    updateUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onresponse Error " + error);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getInstance().getToken());
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }

    private void searchUserFromAPI(String value) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ("http://puigmal.salle.url.edu/api/users/search?s="+value);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast toast;
                if (response.length() == 0) {
                    toast = Toast.makeText(ListChatActivity.this, "User Not Found", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(ListChatActivity.this, "User Found", Toast.LENGTH_SHORT);
                }
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(ListChatActivity.this, "User Not Found", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                System.out.println(error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getInstance().getToken());
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }
}