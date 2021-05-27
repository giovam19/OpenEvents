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
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PuntuationDialog extends AppCompatActivity {
    private EditText punctuation;
    private EditText commentary;
    private TextView title;
    private Button save;

    private int eventID;
    private JSONArray comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuation_dialog);
        setFinishOnTouchOutside(false);

        punctuation = (EditText) findViewById(R.id.puntuationText);
        commentary = (EditText) findViewById(R.id.commentaryText);
        title = (TextView) findViewById(R.id.titleDialog);
        save = (Button) findViewById(R.id.saveCommentary);
        eventID = getIntent().getIntExtra("eventID", -1);

        title.setText(getIntent().getStringExtra("eventTilte"));
        getActualComment();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String com = commentary.getText().toString();
                String punt = punctuation.getText().toString();

                makeComment(com, Integer.parseInt(punt));
            }
        });
    }

    private void getActualComment() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/events/"+eventID+"/assistances/"+User.getUser().getID();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                comment = response;
                try {
                    if (!comment.getJSONObject(0).getString("puntuation").equals("null")) {
                        punctuation.setText(comment.getJSONObject(0).getString("puntuation"));
                        System.out.println("va texto");
                    }

                    if (!comment.getJSONObject(0).getString("comentary").equals("null")) {
                        commentary.setText(comment.getJSONObject(0).getString("comentary"));
                        System.out.println("va texto tambien");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error comment get: " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getUser().getToken());
                return params;
            }
        };

        queue.add(request);
    }

    private void makeComment(String comentario, int puntuacion) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/events/"+eventID+"/assistances/";

        JSONObject params = new JSONObject();
        try {
            params.put("puntuation", puntuacion);
            params.put("comentary", comentario);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast = Toast.makeText(PuntuationDialog.this, "Comment Created!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();

                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(PuntuationDialog.this, "Error creating comment", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                System.out.println("Error comment: " + error);

                finish();
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0)
                        result = new JSONObject(jsonString);

                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + User.getUser().getToken());
                return params;
            }
        };

        queue.add(request);
    }
}