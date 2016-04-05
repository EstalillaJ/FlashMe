package com.cs380.flashme.flashme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    private String[] mainListItems = {"Quiz Yourself!", "Add A New FlashCard", "My Favorite Cards"};
    private ArrayAdapter<String> mMainListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.main_list);

        mMainListAdapter = new ArrayAdapter<>(this, R.layout.list_item_main, mainListItems);

        listView.setAdapter(mMainListAdapter);
    }
}
