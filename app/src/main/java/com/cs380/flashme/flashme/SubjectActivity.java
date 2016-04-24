package com.cs380.flashme.flashme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.IntentConstants;

import java.util.ArrayList;

public class SubjectActivity extends Activity {

    // Fields
    private DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        database = DBHelper.getInstance(this);

        // This is the method that makes all of the populating work
        populateSubjectListView();

    }

    /**
     * This method populates the subject listView on the creation
     * of the activity_subject. This will list whatever courses that
     * are stored in the database, and not list the courses until
     * the user clicks on the subject.
     *
     */
    private void populateSubjectListView() {

        // Finding the listView resource.
        ListView subjectView = (ListView) findViewById(R.id.subjectListView);

        // Populating ArrayList with subjects from database
        ArrayList<String> subjectList = database.getSubjects();

        // Creating ArrayAdapter
        ArrayAdapter subjectAdapter = new ArrayAdapter<String>(this, R.layout.subject_layout, subjectList);

        // set array adapter as the listviews adapter
        subjectView.setAdapter(subjectAdapter);

        // Listener for when an subject is clicked
        subjectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // testing to make sure clickable works
                Toast.makeText(SubjectActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
                String subjectString = (String) parent.getItemAtPosition(position);
                populateCourseListView(subjectString);
            }
        });
    }

    /**
     * Populates the course listView whenever the user clicks on the subject
     * and populates that subject with course list views.
     *
     */
    private void populateCourseListView(final String subject) {

        // invoking courses to populate the correct subjects
        ArrayList<Integer> courselist = database.getCoursesNumbersInSubject(subject);


        // Finding the listView resource.
        ListView courseView = (ListView) findViewById(R.id.courseListView);

        // Creating ArrayAdapter
        ArrayAdapter subjectAdapter = new ArrayAdapter<Integer>(this, R.layout.subject_layout, courselist);

        // setting courseView adapter
        courseView.setAdapter(subjectAdapter);

        courseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), CourseActivity.class);
                Integer courseInt = (Integer) parent.getItemAtPosition(position);
                String courseString = Integer.toString(courseInt);
                intent.putExtra(IntentConstants.SUBJECT_KEY, subject);
                intent.putExtra(IntentConstants.COURSE_NUM_KEY, courseString);
                startActivity(intent);
            }
        });
    }


}
