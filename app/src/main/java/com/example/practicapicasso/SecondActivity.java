package com.example.practicapicasso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    ImageView img1 = null;
    ImageView img2 = null;
    ImageView img3 = null;
    Button btnBack = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        img1 = findViewById(R.id.firstImageView);
        img2 = findViewById(R.id.secondImageView);
        img3 = findViewById(R.id.thirdImageView);
        btnBack = findViewById(R.id.buttonBack);

        Intent receive = getIntent();
        ArrayList<String> aux = receive.getStringArrayListExtra("IMAGELIST");
        Picasso.get().load(aux.get(0)).into(img1);
        Picasso.get().load(aux.get(1)).into(img2);
        Picasso.get().load(aux.get(2)).into(img3);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}