package com.zongchou.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;

/**
 * Created by user on 27-Jan-18.
 */

public class RecordFragment extends android.app.Fragment{
    View myView;
    ArrayList<String> groups;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.record_layout, container, false);
        readGroups();
        return myView;
    }

    private void readGroups() {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        groups = new ArrayList<>();

        try {
            InputStream inputStream = getActivity().openFileInput("folders.txt");

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


    }

    @Override
     public void onViewCreated(View view, Bundle savedInstanceState){

        ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, groups);

        ListView groupView = (ListView) getActivity().findViewById(R.id.groupsView);
        groupView.setAdapter(aa);

        groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //need to press Alt+Enter on red, highlighted area of OnItemClickListener() to get this header
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), Members.class);
                i.putExtra("name", position);
                startActivity(i);
            }
        });


    }

}
