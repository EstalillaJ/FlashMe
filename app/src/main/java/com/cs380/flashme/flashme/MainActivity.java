package com.cs380.flashme.flashme;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cs380.flashme.flashme.util.ExpandableListItem;
import com.cs380.flashme.flashme.util.MyListAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ExpandableListItem> mainListItems;
    private Resources resources;
    private List<String> subjects;
    private MyListAdapter mainListAdapter;
    private Button quizButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
