package com.zongchou.listener;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Members extends AppCompatActivity {
    ArrayList<Profile> members;
    ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        Intent i = getIntent();
        int groupName = i.getIntExtra("name", 0);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        members = new ArrayList<>();
        titles = new ArrayList<>();

        try {
            InputStream inputStream = openFileInput(groupName + ".txt");
            //InputStream inputStream = openFileInput(groupName + ".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String x;

                while ( (x = bufferedReader.readLine()) != null ) {
                  titles.add(x);
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, titles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(getColor(R.color.white));

                // Generate ListView Item using TextView
                return view;
            }

        };

        ListView groupView = (ListView) findViewById(R.id.memberView);
        groupView.setAdapter(aa);

        groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //need to press Alt+Enter on red, highlighted area of OnItemClickListener() to get this header
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
