package com.cs380.flashme.flashme;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.cs380.flashme.flashme.util.ExpandableListItem;
import com.cs380.flashme.flashme.util.MyListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ExpandableListItem> mainListItems;
    private Resources resources;
    private List<String> subjects;
    private MyListAdapter mainListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ListView listView = (ListView) findViewById(R.id.main_list);
        resources = this.getResources();
        subjects = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.default_subjects)));
        mainListItems = new ArrayList<>(Arrays.asList(new ExpandableListItem[]{
                new ExpandableListItem(resources.getString(R.string.new_card_prompt), null),
                new ExpandableListItem("Courses", subjects)
        }));

        mainListAdapter = new MyListAdapter(this, mainListItems);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.main_expandable_list);
        listView.setAdapter(mainListAdapter);
    }



    public void createNotecardButton(View v) {
        // do something when the button is clicked
    }

    public void viewCoursesButton(View v) {
// do something when the button is clicked
    }

    public void previousSessionButton(View v) {
// do something when the button is clicked
    }

}
