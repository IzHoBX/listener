package com.zongchou.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.SpeechRecognitionNotAvailable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Ready extends AppCompatActivity {
    private final int RESULT_SPEECH = 2;
    private final int CAMERA_REQUEST = 3;
    private int total = 0;

    TextView ageText;
    ArrayList<Profile> temp;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        try {
            record();
        } catch (SpeechRecognitionNotAvailable speechRecognitionNotAvailable) {
            speechRecognitionNotAvailable.printStackTrace();
        } catch (GoogleVoiceTypingDisabledException e) {
            e.printStackTrace();
        }
        temp = new ArrayList<>();
        name = (TextView) findViewById(R.id.name);
        ageText = (TextView) findViewById(R.id.age);
    }

    protected void stop(View view) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        ArrayList<String> groups = new ArrayList<>();

        try {
            InputStream inputStream = getApplicationContext().openFileInput("folders.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String x;

                while ( (x = bufferedReader.readLine()) != null ) {
                    groups.add(x);
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }


        FileOutputStream fos = openFileOutput(groups.size() + ".txt", Context.MODE_PRIVATE);
        for(int i=0; i<temp.size(); i++)
            fos.write((temp.get(i).name + " " + temp.get(i).age + "\n").getBytes());
        fos.close();

        Intent i = new Intent(getApplicationContext(), Summary.class);
        i.putExtra("total", total);
        startActivity(i);
    }

    protected void record() throws SpeechRecognitionNotAvailable, GoogleVoiceTypingDisabledException {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, RESULT_SPEECH);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                process(text.get(0));
                Log.i("Test", text.get(0));
                try {
                    record();
                } catch (SpeechRecognitionNotAvailable speechRecognitionNotAvailable) {
                    speechRecognitionNotAvailable.printStackTrace();
                } catch (GoogleVoiceTypingDisabledException e) {
                    e.printStackTrace();
                }
            }
        }else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ((ImageView) findViewById(R.id.photo)).setImageBitmap(imageBitmap);
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                try {
                    FileOutputStream ostream = new FileOutputStream(cachePath);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private void process(String x) {
        int i;
        String name1, name2, temp = "";
        String age;
        String[] y = x.split(" ");

        // declare a Scanner object to read input
        Scanner myScanner = new Scanner(System.in);

        // read input and process them accordingly
        for(i=0; i<y.length && !y[i].equals("name"); i++);

        if(i>y.length-4) {
            Toast.makeText(this, "didnt catch what your name", Toast.LENGTH_SHORT).show();
        } else {
            name.setText("Name: " + y[i + 2] + " " + y[i + 3]);
        }

        for(i=0; i<y.length && !y[i].equals("age"); i++);

        if(i>y.length-3) {
            Toast.makeText(this, "didnt catch what your age", Toast.LENGTH_SHORT).show();
        } else {
            ageText.setText("Age: " + y[i+2]);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            Profile x = new Profile(name.getText().toString(), ageText.getText().toString());
            temp.add(x);
            total++;
            Toast.makeText(this, "Successfully added 1 person!", Toast.LENGTH_SHORT).show();
            ageText.setText("Age: ");
            name.setText("Name: ");
            ((ImageView) findViewById(R.id.photo)).setImageResource(android.R.drawable.ic_menu_camera);
            try {
                record();
            } catch (SpeechRecognitionNotAvailable speechRecognitionNotAvailable) {
                speechRecognitionNotAvailable.printStackTrace();
            } catch (GoogleVoiceTypingDisabledException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    protected void photo(View view) throws IOException {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

}

