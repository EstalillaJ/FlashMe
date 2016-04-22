package com.cs380.flashme.flashme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> mMainListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subjectButton();

    }

    public void createNotecardButton(View v) {
        // do something when the button is clicked
    }

    public void subjectButton() {
        Button viewSubjectButton = (Button)findViewById(R.id.viewSubjectsButton);

        viewSubjectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                startActivity(intent);
            }
        });
    }
    public void favoriteCoursesButton(View v) {
        // do something when the button is clicked
    }

}
