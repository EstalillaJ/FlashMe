package com.cs380.flashme.flashme;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SubjectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        //populateSubjectListView();

    }

    private void populateSubjectListView() {
        // Create list of items
        String[] tempData = {"Computer Science", "Math", "History"};

        // adapter (Context for the activity, layout to use, items to be displayed)
        ArrayAdapter<String> testAdapter = new ArrayAdapter<String>(this, R.layout.activity_subject, tempData);

        // configuring list view
        ListView list = (ListView) findViewById(R.id.SubjectListView);
        list.setAdapter(testAdapter);
    }


}
