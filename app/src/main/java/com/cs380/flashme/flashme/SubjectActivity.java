package com.cs380.flashme.flashme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.IntentConstants;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

public class SubjectActivity extends Activity implements AdapterView.OnItemClickListener {

    // Fields
    private DBHelper database;
    private String sub;
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
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(subjectAdapter);

        animationAdapter.setAbsListView(subjectView);
        // set array adapter as the listviews adapter
        subjectView.setAdapter(animationAdapter);

        subjectView.setOnItemClickListener(this);
    }

    /**
     * Populates the course listView whenever the user clicks on the subject
     * and populates that subject with course list views.
     *
     */
    private void populateCourseListView(String subject) {

        // invoking courses to populate the correct subjects
        ArrayList<Integer> courselist = database.getCoursesNumbersInSubject(subject);
        sub =  subject;

        // Finding the listView resource.
        ListView courseView = (ListView) findViewById(R.id.courseListView);

        // Creating ArrayAdapter
        ArrayAdapter courseNumAdapter = new ArrayAdapter<Integer>(this, R.layout.subject_layout, courselist);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(courseNumAdapter);
        animationAdapter.setAbsListView(courseView);
        // setting courseView adapter
        courseView.setAdapter(animationAdapter);

        //TODO maybe move this to onCreate()
        courseView.setOnItemClickListener(this);
    }

    public void onCourseClick(int courseNum){
        Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
        String courseString = Integer.toString(courseNum);
        intent.putExtra(IntentConstants.SUBJECT_KEY, sub);
        intent.putExtra(IntentConstants.COURSE_NUM_KEY, courseString);
        startActivity(intent);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.subjectListView:
                String subjectString = (String) parent.getItemAtPosition(position);
                populateCourseListView(subjectString);
                break;
            case R.id.courseListView:
                Integer courseNum = (Integer) parent.getItemAtPosition(position);
                onCourseClick(courseNum);
                break;

        }

    }


}
