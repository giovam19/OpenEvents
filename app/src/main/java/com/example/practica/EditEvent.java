package com.example.practica;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class EditEvent extends AppCompatActivity {
    private static final int TITLE_CODE = 1;
    private static final int DESCRIPTION_CODE = 2;
    private static final int UBI_CODE = 3;
    private static final int MAX_PPL_CODE = 4;
    private static final int TYPE_CODE = 5;
    private static final int START_MONTH_CODE = 6;
    private static final int START_DAY_CODE = 7;
    private static final int END_MONTH_CODE = 8;
    private static final int END_DAY_CODE = 9;

    private ImageView backButton;
    private TextView contactButton;
    private TextView eventTitle;
    private Button guardar;
    private Button eliminar;
    private ImageView imageEvent;
    private TextView monthStart;
    private TextView monthEnd;
    private TextView dayStart;
    private TextView dayEnd;
    private TextView description;
    private TextView ubication;
    private TextView maxPpl;
    private TextView type;
    private int eventID;
    private int ownerID;

    private String msValue;
    private String meValue;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_layout);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        backButton = (ImageView) findViewById(R.id.backButtonEvent);
        contactButton = (TextView) findViewById(R.id.contact);
        eventTitle = (TextView) findViewById(R.id.eventTitle);
        guardar = (Button) findViewById(R.id.añadir_event);
        eliminar = (Button) findViewById(R.id.quitar_event);
        imageEvent = (ImageView) findViewById(R.id.image_event);
        monthStart = (TextView) findViewById(R.id.monthTextStart);
        monthEnd = (TextView) findViewById(R.id.monthTextEnd);
        dayStart = (TextView) findViewById(R.id.dayTextStart);
        dayEnd = (TextView) findViewById(R.id.dayTextEnd);
        description = (TextView) findViewById(R.id.descriptionText);
        ubication = (TextView) findViewById(R.id.ubicationText);
        maxPpl = (TextView) findViewById(R.id.maxPeopleText);
        type = (TextView) findViewById(R.id.typeText);

        contactButton.setVisibility(View.INVISIBLE);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) eliminar.getLayoutParams();
        params.width = 250;
        params.setMarginEnd(80);
        eliminar.setText("Eliminar");
        eliminar.setTextSize(12);
        eliminar.setLayoutParams(params);

        ConstraintLayout.LayoutParams aparams = (ConstraintLayout.LayoutParams) guardar.getLayoutParams();
        aparams.width = 250;
        aparams.setMarginStart(80);
        guardar.setText("Guardar");
        guardar.setTextSize(12);
        guardar.setLayoutParams(aparams);

        String title = getIntent().getStringExtra("eventTilte");
        String descript = getIntent().getStringExtra("eventDescription");
        String loc = getIntent().getStringExtra("eventUbi");
        String maxppl = getIntent().getStringExtra("eventMaxppl");
        String tpe = getIntent().getStringExtra("eventType");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        eventID = getIntent().getIntExtra("eventID", -1);
        ownerID = getIntent().getIntExtra("eventOwner", -1);

        eventTitle.setText(title);
        description.setText(descript);
        ubication.setText(loc);
        maxPpl.setText(maxppl);
        type.setText(tpe);
        separateDateInitEnd(startDate, endDate);

        msValue = parseMonth(monthStart.getText().toString());
        meValue = parseMonth(monthEnd.getText().toString());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (description.getText().toString().length() < 10) {
                    Toast toast = Toast.makeText(EditEvent.this, "Description should have more than 10 characters", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                } else {
                    editEventToAPI();
                }
            }
        });

        eventTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", eventTitle.getText().toString());
                startActivityForResult(intent, TITLE_CODE);
            }
        });

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", description.getText().toString());
                startActivityForResult(intent, DESCRIPTION_CODE);
            }
        });

        ubication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", ubication.getText().toString());
                startActivityForResult(intent, UBI_CODE);
            }
        });

        maxPpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", maxPpl.getText().toString());
                startActivityForResult(intent, MAX_PPL_CODE);
            }
        });

        monthStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", monthStart.getText().toString());
                startActivityForResult(intent, START_MONTH_CODE);
            }
        });

        dayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", dayStart.getText().toString());
                startActivityForResult(intent, START_DAY_CODE);
            }
        });

        monthEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", monthEnd.getText().toString());
                startActivityForResult(intent, END_MONTH_CODE);
            }
        });

        dayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", dayEnd.getText().toString());
                startActivityForResult(intent, END_DAY_CODE);
            }
        });

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, ChangeTextDialog.class);
                intent.putExtra("actual", type.getText().toString());
                startActivityForResult(intent, TYPE_CODE);
            }
        });
    }

    private void separateDateInitEnd(String start, String end) {
        start = start.replace("T", " ");
        String[] camps = start.split(" ");
        String[] date = camps[0].split("-");

        date[1] = date[1].replace("-", "");
        String month = createMonth(date[1]);

        end = end.replace("T", " ");
        String[] camps2 = end.split(" ");
        String[] date2 = camps2[0].split("-");

        date2[1] = date2[1].replace("-", "");
        String month2 = createMonth(date2[1]);

        monthStart.setText(month);
        monthEnd.setText(month2);

        dayStart.setText(date[2]);
        dayEnd.setText(date2[2]);
    }

    private String createMonth(String date) {
        switch (date) {
            case "01":
                return "Jan";
            case  "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "Apr";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return "";
        }
    }

    private String parseMonth(String month) {
        switch (month) {
            case "Jan":
                return "01";
            case  "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                return "00";
        }
    }

    private void deleteEvent() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/events/"+eventID;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast toast = Toast.makeText(EditEvent.this, "Event Eliminated", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();

                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onresponse Error " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getInstance().getToken());
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (data == null) {
            return;
        }

        String newText = data.getStringExtra("newText");

        if (!newText.equals("")) {
            switch (requestCode) {
                case TITLE_CODE:
                    eventTitle.setText(newText);
                    break;
                case DESCRIPTION_CODE:
                    description.setText(newText);
                    break;
                case UBI_CODE:
                    ubication.setText(newText);
                    break;
                case MAX_PPL_CODE:
                    maxPpl.setText(newText);
                    break;
                case TYPE_CODE:
                    type.setText(newText);
                    break;
                case START_MONTH_CODE:
                    monthStart.setText(newText);
                    msValue = parseMonth(newText);
                    break;
                case START_DAY_CODE:
                    dayStart.setText(newText);
                    break;
                case END_MONTH_CODE:
                    monthEnd.setText(newText);
                    meValue = parseMonth(newText);
                    break;
                case END_DAY_CODE:
                    dayEnd.setText(newText);
                    break;
            }
        }
    }

    private void editEventToAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://puigmal.salle.url.edu/api/events/"+eventID;

       JSONObject params = new JSONObject();
       try {
           params.put("name", eventTitle.getText().toString());
           params.put("location", ubication.getText().toString());
           params.put("description", description.getText().toString());
           params.put("eventStart_date", "2021-"+msValue+"-"+dayStart.getText().toString()+"T15:00:00.000Z");
           params.put("eventEnd_date", "2021-"+meValue+"-"+dayEnd.getText().toString()+"T15:00:00.000Z");
           params.put("n_participators", Integer.parseInt(maxPpl.getText().toString()));
           System.out.println(params.get("eventStart_date"));
           params.put("type", type.getText().toString());
       } catch (Exception e) {
           e.printStackTrace();
       }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Toast toast = Toast.makeText(EditEvent.this, "Edit successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(EditEvent.this, "Error editing event", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                System.out.println(error);
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
}