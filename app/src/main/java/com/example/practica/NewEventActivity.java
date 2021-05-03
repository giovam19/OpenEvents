package com.example.practica;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class NewEventActivity extends AppCompatActivity {
    private ImageView backButton;
    private Button create;
    private Button addImage;
    private EditText name;
    private EditText description;
    private EditText ubication;
    private EditText creation;
    private EditText start;
    private EditText end;
    private EditText maxPeople;
    private EditText type;
    private String imagePath;

    private static final int PICK_IMAGE = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));

        imagePath = null;
        name = (EditText) findViewById(R.id.nameNE);
        description = (EditText) findViewById(R.id.descriprtionNE);
        ubication = (EditText) findViewById(R.id.ubicationNE);
        creation = (EditText) findViewById(R.id.creationDateNE);
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
                String creat = creation.getText().toString();
                String strt = start.getText().toString();
                String nd = end.getText().toString();
                String mxplp = maxPeople.getText().toString();
                String tpe = type.getText().toString();
                //funcion para crear evento
                finish();
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