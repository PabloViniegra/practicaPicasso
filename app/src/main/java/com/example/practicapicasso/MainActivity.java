package com.example.practicapicasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button btnDownload = null;
    Button btnSave = null;
    Button btnPermissions = null;
    ImageView image = null;
    public final String MESSAGE_ERROR_IO = "Ha saltado una IOException";
    static String firstGroup = "https://lh3.googleusercontent.com/proxy/887bHSpuM52lTANHg5nYVN39BvLIOwgDQcNiA1SR8DF-NMChWA3mM8wDiUs-b411j2opT989rfX_wyC9FWEhdO6ylrD8-L2ZZHpzT_T6aa0XLN9eyvZQTtRBEA";
    static String secondGroup = "https://images-na.ssl-images-amazon.com/images/I/41hiicBrfbL.jpg";
    static String thirdGroup = "https://images-na.ssl-images-amazon.com/images/I/51leB-OF7sL._AC_SY400_.jpg";
    private Timer timer = null;
    private TimerTask task = null;
    private Context mcontext = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDownload = findViewById(R.id.btnDownload);
        btnSave = findViewById(R.id.btnSave);
        btnPermissions = findViewById(R.id.btnPermissions);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer();
            }
        });

        btnPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public static void loadImage(String url) {
        Picasso.get().load(url);
    }

    public static void saveImage(final String url, ImageView image) {
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
                try {
                    file.createNewFile();
                    FileOutputStream fileOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOut);
                    fileOut.flush();
                    fileOut.close();
                } catch (IOException e) {
                    Log.e("IOException", e.getLocalizedMessage());
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    //Contiene la tarea schedulada
    public void chronometer() {

        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        MainActivity.loadImage(MainActivity.firstGroup);
                        MainActivity.loadImage(MainActivity.secondGroup);
                        MainActivity.loadImage(MainActivity.thirdGroup);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(task, 1, 1000);
    }

}