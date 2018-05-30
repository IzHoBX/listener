package com.zongchou.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Summary extends AppCompatActivity {
    private int savePressed = 0;
    int total;
    ArrayList<Profile> temp;
    ArrayList<String> folders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent i = getIntent();
        total = i.getIntExtra("total", 0);
        ((TextView) findViewById(R.id.congratulations)).setText("Congratulations! You've met " + i.getIntExtra("total", 0) + " new people today!");
        folders = new ArrayList<String>();
    }

    protected void save(View view) throws IOException {
        savePressed++;
        if(savePressed == 1) {
            ((TextView) findViewById(R.id.congratulations)).animate().alpha(0f).setDuration(1000);
            ((TextView) findViewById(R.id.congratulations)).setVisibility(View.GONE);
            ((EditText) findViewById(R.id.name)).setAlpha(0f);
            ((EditText) findViewById(R.id.name)).setVisibility(View.VISIBLE);
            ((EditText) findViewById(R.id.name)).animate().alpha(1f).setDuration(1000);
        } else {
            if (((EditText) findViewById(R.id.name)).getText().toString().equals("")) {
                Toast.makeText(this, "Please insert a group name for today's session", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (((EditText) findViewById(R.id.name)).getText().toString().equals("1")) {
                Toast.makeText(this, "Invalid group name, please use another name.", Toast.LENGTH_SHORT).show();
                return;
            }
            //store

            try {
                InputStream inputStream = getApplicationContext().openFileInput("folders.txt");

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String x;

                    while ( (x = bufferedReader.readLine()) != null ) {
                        folders.add(x);
                    }

                    inputStream.close();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }

            folders.add(((EditText) findViewById(R.id.name)).getText().toString());

            FileOutputStream fos = openFileOutput("folders.txt", Context.MODE_PRIVATE);
            for(int i=0; i<folders.size(); i++)
                fos.write((folders.get(i) + "\n").getBytes());
            fos.close();

            exit(view);
        }
    }

    protected void exit(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
