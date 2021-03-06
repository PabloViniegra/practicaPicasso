package com.example.practicapicasso;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button btnDownload = null;
    Button btnSave = null;
    Button btnPermissions = null;
    ImageView image = null;
    Button btnPass = null;
    static String aux = "";
    final int TAG = 1;
    int contador = 0;
    Timer timer = null;
    final int MY_PERMISSION_WRITE = 3;
    final int MY_PERMISSION_INTERNET = 2;
    public final String INTERNET_ACCEPTED = "Los permisos de Internet han sido otorgados";
    public final String WRITE_ACCEPTED = "Los permisos de escritura han sido otorgados";
    public final String DENIED_PERMISSION = "El permiso ha sido denegado";
    static String firstGroup = "https://images-na.ssl-images-amazon.com/images/I/71ckXZHkllL._AC_SX355_.jpg";
    static String secondGroup = "https://images-na.ssl-images-amazon.com/images/I/41hiicBrfbL.jpg";
    static String thirdGroup = "https://images-na.ssl-images-amazon.com/images/I/51leB-OF7sL._AC_SY400_.jpg";
    public String firstGroupYoutube = "https://www.youtube.com/watch?v=DelhLppPSxY&ab_channel=AvengedSevenfold";
    public String secondGroupYoutube = "https://www.youtube.com/watch?v=HL9kaJZw8iw&ab_channel=lambofgodVEVO";
    public String thirdGroupYoutube = "https://www.youtube.com/watch?v=B07cF9ECUv8&ab_channel=ThePit";
    static ArrayList<String> URLCollection = new ArrayList<>();
    private Context mcontext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collectURLInArraylist();
        mcontext = getApplicationContext();
        btnDownload = findViewById(R.id.btnDownload);
        btnSave = findViewById(R.id.btnSave);
        btnPermissions = findViewById(R.id.btnPermissions);
        btnPass = findViewById(R.id.btnPass);
        image = findViewById(R.id.RockImages);


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
                saveImage(aux);

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aux.equalsIgnoreCase(URLCollection.get(0))){
                    Uri uri = Uri.parse(firstGroupYoutube);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (aux.equalsIgnoreCase(URLCollection.get(1))) {
                    Uri uri = Uri.parse(secondGroupYoutube);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (aux.equalsIgnoreCase(URLCollection.get(2))) {
                    Uri uri = Uri.parse(thirdGroupYoutube);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                } else {
                    Log.d(String.valueOf(TAG), "Parece que el evento no ha funcionado. Mira si las condiciones se están cumpliendo.");
                    Toast.makeText(mcontext, "Ha ocurrido algún error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(MainActivity.this, SecondActivity.class);
                sendIntent.putStringArrayListExtra("IMAGELIST",URLCollection);
                startActivity(sendIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer!= null)
            chronometer();
    }

    public void loadImage(String url) {
        Picasso.get().load(url).into(image);
    }

    public void saveImage(final String url) {
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                if (!directory.exists())
                    directory.mkdir();
                File fichero = new File(directory,new Date().toString().concat(".jpg"));
                if (!fichero.exists()) {
                    try {
                        fichero.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try (FileOutputStream fileOut = new FileOutputStream(fichero)) {
                  bitmap.compress(Bitmap.CompressFormat.JPEG,90,fileOut);
                  fileOut.flush();
                    Toast.makeText(mcontext, "Save!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
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

        //Bucle para recorrer el arraylist de URL y cargarlas con Picasso
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Bucle para recorrer el arraylist de URL y cargarlas con Picasso
                        loadImage(URLCollection.get(contador));
                        aux = URLCollection.get(contador);
                        contador++;
                        if (contador==3)
                            contador=0;
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