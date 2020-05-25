package com.example.ms3codingchallenge;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewListContents extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontents_layout);

        ListView listView = (ListView) findViewById(R.id.listView);
        myDb = new DatabaseHelper(this);

        ArrayList<String> list = new ArrayList<>();
        Cursor data = myDb.getListContents();

        if(data.getCount() == 0){
            Toast.makeText(ViewListContents.this, "Error! The Database was empty", Toast.LENGTH_LONG).show();
        }else{
            while (data.moveToNext()){
                list.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            }
        }
    }
}
