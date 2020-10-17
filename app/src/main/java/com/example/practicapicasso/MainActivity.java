package com.example.practicapicasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import java.net.URI;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button btnDownload = null;
    Button btnSave = null;
    Button btnPermissions = null;
    static ImageView image = null;
    final int TAG = 1;
    final int MY_PERMISSION_WRITE = 3;
    final int MY_PERMISSION_INTERNET = 2;
    public final String MESSAGE_ERROR_IO = "Ha saltado una IOException";
    public final String INTERNET_ACCEPTED = "Los permisos de Internet han sido otorgados";
    public final String WRITE_ACCEPTED = "Los permisos de escritura han sido otorgados";
    public final String DENIED_PERMISSION = "El permiso ha sido denegado";
    static String firstGroup = "https://lh3.googleusercontent.com/proxy/887bHSpuM52lTANHg5nYVN39BvLIOwgDQcNiA1SR8DF-NMChWA3mM8wDiUs-b411j2opT989rfX_wyC9FWEhdO6ylrD8-L2ZZHpzT_T6aa0XLN9eyvZQTtRBEA";
    static String secondGroup = "https://images-na.ssl-images-amazon.com/images/I/41hiicBrfbL.jpg";
    static String thirdGroup = "https://images-na.ssl-images-amazon.com/images/I/51leB-OF7sL._AC_SY400_.jpg";
    public String firstGroupYoutube = "https://www.youtube.com/watch?v=DelhLppPSxY&ab_channel=AvengedSevenfold";
    public String secondGroupYoutube = "https://www.youtube.com/watch?v=HL9kaJZw8iw&ab_channel=lambofgodVEVO";
    public String thirdGroupYoutube = "https://www.youtube.com/watch?v=B07cF9ECUv8&ab_channel=ThePit";
    static ArrayList<String> URLCollection = new ArrayList<>();
    private Timer timer = null;
    private TimerTask task = null;
    private Context mcontext = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collectURLInArraylist();
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
                onRequestPermissions();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(image).equalsIgnoreCase(URLCollection.get(0))){
                    Uri uri = Uri.parse(firstGroupYoutube);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (String.valueOf(image).equalsIgnoreCase(URLCollection.get(1))) {
                    Uri uri = Uri.parse(secondGroupYoutube);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (String.valueOf(image).equalsIgnoreCase(URLCollection.get(2))) {
                    Uri uri = Uri.parse(thirdGroupYoutube);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                } else {
                    Log.d(String.valueOf(TAG), "Parece que el evento no ha funcionado. Mira si las condiciones se están cumpliendo.");
                    Toast.makeText(mcontext, "Ha ocurrido algún error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void loadImage(String url) {
        Picasso.get().load(url).into(image);
    }

    public static void saveImage(final String url, ImageView image) {
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String dirName = Environment.getExternalStorageDirectory().toString() + "/practicaPicasso/images";
                File dir = new File(dirName);
                try {
                    if (!dir.exists())
                        dir.mkdir();
                    File file = new File(url);
                    file.createNewFile();
                    FileOutputStream fileOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
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
                        //Bucle para recorrer el arraylist de URL y cargarlas con Picasso
                        for (String s : URLCollection) {
                            loadImage(s);
                        }

                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(task, 1, 2000);
    }

    // Método para pedir permisos al usuario
    public void onRequestPermissions() {
        if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSION_INTERNET);
            Toast.makeText(mcontext, INTERNET_ACCEPTED, Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_WRITE);
            Toast.makeText(mcontext, WRITE_ACCEPTED, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mcontext, DENIED_PERMISSION, Toast.LENGTH_SHORT).show();
        }
    }

    public void collectURLInArraylist () {
        URLCollection.add(firstGroup);
        URLCollection.add(secondGroup);
        URLCollection.add(thirdGroup);

    }

}