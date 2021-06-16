package com.example.practica;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class ListChatActivity extends AppCompatActivity {
    private ImageView backButton;
    private RecyclerView eventList;
    private EditText search;
    private Button newChat;

    private JSONArray users;
    private JSONArray allUsers;
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
                    searchUser(value);
                }
            }
        });

        searchUsersFromAPI();
        getUsersFromAPI();
    }

    private void updateUI() {
        adapter = new ListUserAdapter(users);
        eventList.setAdapter(adapter);
    }

    private void searchUser(String value) {
        boolean found = false;

        try {
            int i;

            for (i = 0; i < allUsers.length(); i++) {
                if (value.equals(allUsers.getJSONObject(i).getString("name"))) {
                    found = true;
                    break;
                }
            }

            if (found) {
                Intent intent = new Intent(ListChatActivity.this, ChatActivity.class);
                intent.putExtra("userID", allUsers.getJSONObject(i).getString("id"));
                intent.putExtra("userName", allUsers.getJSONObject(i).getString("name"));
                intent.putExtra("userLastName", allUsers.getJSONObject(i).getString("last_name"));
                intent.putExtra("userImage", allUsers.getJSONObject(i).getString("image"));
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(ListChatActivity.this, "User not found!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }

        } catch (Exception e) {}
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

    private void searchUsersFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/users/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                allUsers = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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