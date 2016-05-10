package com.cs380.flashme.flashme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.IntentConstants;

import java.util.ArrayList;

public class CardsFromDatabase extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DBHelper dbHelper;
    private Spinner subjectSpinner, courseNumSpinner;
    private ArrayList<Integer>  courseNums;
    private ArrayAdapter<Integer> courseNumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_from_database);
        String subjectFromIntent = getIntent().getStringExtra(IntentConstants.SUBJECT_KEY);
        String courseNumFromIntent = getIntent().getStringExtra(IntentConstants.COURSE_NUM_KEY);

        dbHelper = DBHelper.getInstance(this);

        subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        courseNumSpinner = (Spinner) findViewById(R.id.courseNumberSpinner);


        ArrayList<String> subjectList = dbHelper.getSubjects();
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(this, R.layout.subject_layout, subjectList);

        subjectAdapter.setDropDownViewResource(R.layout.subject_layout);
        subjectSpinner.setAdapter(subjectAdapter);
        if (!subjectFromIntent.equals(""))
            subjectSpinner.setSelection(subjectList.indexOf(subjectFromIntent));

        courseNums = dbHelper.getCoursesNumbersInSubject((String) subjectSpinner.getSelectedItem());
        courseNumAdapter = new ArrayAdapter<Integer>(this, R.layout.subject_layout, courseNums);
        courseNumAdapter.setDropDownViewResource(R.layout.subject_layout);
        courseNumSpinner.setAdapter(courseNumAdapter);

        if (!courseNumFromIntent.equals(""))
            courseNumSpinner.setSelection(courseNums.indexOf(Integer.parseInt(courseNumFromIntent)));

        subjectSpinner.setOnItemSelectedListener(this);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String subject = (String) parent.getItemAtPosition(position);
        courseNumAdapter.clear();
        courseNumAdapter.addAll(dbHelper.getCoursesNumbersInSubject(subject));
    }

    public void onNothingSelected(AdapterView<?> parent){

    }


}
