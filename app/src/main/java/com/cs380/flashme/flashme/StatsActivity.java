package com.cs380.flashme.flashme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.IntentConstants;

public class StatsActivity extends AppCompatActivity {
    private String subject;
    private String courseNum;
    private String courseTitle;
    private String courseAccuracy;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        DBHelper dbHelper = DBHelper.getInstance(this);

        subject = getIntent().getStringExtra(IntentConstants.SUBJECT_KEY);
        courseNum =  getIntent().getStringExtra(IntentConstants.COURSE_NUM_KEY);
        course = dbHelper.getCourse(subject, Integer.parseInt(courseNum));

        // Sets course title
        courseTitle = subject + " " + courseNum + " Statistics:";
        TextView textView = (TextView) findViewById(R.id.MyStats);
        textView.setText(courseTitle);

        // Sets accuracy
        courseAccuracy = "Accuracy: " +course.getAccuracy() +"%";
        TextView textView2 =(TextView) findViewById(R.id.GlobalAccuracy);
        textView.setText(courseAccuracy);



    }
}