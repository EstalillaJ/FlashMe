package com.cs380.flashme.flashme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.IntentConstants;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String subject = getIntent().getStringExtra(IntentConstants.SUBJECT_KEY);
        String courseNum = getIntent().getStringExtra(IntentConstants.COURSE_NUM_KEY);

        DBHelper dbHelper = DBHelper.getInstance(this);

        Course course = dbHelper.getCourse(subject, Integer.parseInt(courseNum));

        TextView textview = (TextView) findViewById(R.id.courseTitle);
        textview.setText(subject);

        ListView courseList = (ListView) findViewById(R.id.courseListView);
        ArrayAdapter subjectAdapter = new ArrayAdapter<Integer>(this, R.layout.subject_layout, courseList); // not entirely sure if implementation is sound; will iterate further
        courseList.setAdapter(subjectAdapter);
        // method used to test; should set to name of subject on activity instantiation

    }
}
