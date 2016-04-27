package com.cs380.flashme.flashme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;

import java.util.List;

public class CourseActivity extends AppCompatActivity {
    private  String subject;
    private String courseNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         subject = getIntent().getStringExtra(IntentConstants.SUBJECT_KEY);
         courseNum = getIntent().getStringExtra(IntentConstants.COURSE_NUM_KEY);

        DBHelper dbHelper = DBHelper.getInstance(this);

        Course course = dbHelper.getCourse(subject, Integer.parseInt(courseNum));
        List<FlashCard> cards = course.getCards();

        TextView textview = (TextView) findViewById(R.id.courseTitle);
        textview.setText(subject);
        Button createCardButton = (Button) findViewById(R.id.createCardFromCourse);


        ListView courseList = (ListView)   findViewById(R.id.courseListView);
       // ArrayAdapter subjectAdapter = new ArrayAdapter<Integer>(this, R.layout.subject_layout, courseList); // not entirely sure if implementation is sound; will iterate further.
                                                                                                                // commented out for now.
        //courseList.setAdapter(subjectAdapter);

    }

    public void createCardFromCourse() {
        Intent intent = new Intent(this, New_Card_Activity.class);

        intent.putExtra(IntentConstants.COURSE_NUM_KEY, courseNum);
        intent.putExtra(IntentConstants.SUBJECT_KEY, subject);

        startActivity(intent);

    }

   // public void QuizMe() {
     //   Intent intent = new Intent(this, )
    //}
}
