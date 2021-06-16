package com.example.practica;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private ImageView backButton;
    private RecyclerView chatList;
    private EditText input;
    private FloatingActionButton send;
    private TextView userName;
    private boolean isInChat;
    private int numMessages;

    private Thread actChat;
    private String userID;
    private JSONObject userInfo;
    private JSONArray chatsArray;
    private ListChatAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        isInChat = true;
        numMessages = 0;
        backButton = (ImageView) findViewById(R.id.backButtonCH);
        userName = (TextView) findViewById(R.id.userNameCH);
        input = (EditText) findViewById(R.id.input);
        send = (FloatingActionButton) findViewById(R.id.send);
        chatList = (RecyclerView) findViewById(R.id.messageList);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        chatsArray = new JSONArray();
        userID = getIntent().getStringExtra("userID");
        userInfo = new JSONObject();
        actChat = new Thread(new AsyncChatUpdate(this));

        try {
            userInfo.put("id", getIntent().getStringExtra("userID"));
            userInfo.put("name", getIntent().getStringExtra("userName"));
            userInfo.put("lastname", getIntent().getStringExtra("userLastName"));
            userInfo.put("image", getIntent().getStringExtra("userImage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        userName.setText(getIntent().getStringExtra("userName"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInChat = false;
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input.getText().toString().equals(""))
                    sendMessage();
            }
        });

        actChat.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isInChat = false;
    }

    public void updateUI() {
        adapter = new ListChatAdapter(chatsArray, userInfo);
        chatList.setAdapter(adapter);
        chatList.smoothScrollToPosition(chatsArray.length() - 1);
    }

    public void getMessagesFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/messages/"+userID;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    chatsArray = response;
                    if (chatsArray.length() > numMessages) {
                        numMessages = chatsArray.length();
                        updateUI();
                    }
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

    private void sendMessage() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/messages/";

        JSONObject params = new JSONObject();
        try {
            params.put("content", input.getText().toString());
            params.put("user_id_send", User.getInstance().getID());
            params.put("user_id_recived", Integer.parseInt(userID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                input.setText("");
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
        queue.add(request);
    }

    public int getNumMessages() {
        return numMessages;
    }

    public boolean isInChat() {
        return isInChat;
    }
}