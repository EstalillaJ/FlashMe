package com.cs380.flashme.flashme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.cs380.flashme.flashme.data.DBConstants;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
    * passNum, passSubject as null
    *
     */
    private ArrayAdapter<String> mMainListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subjectButton();

    }

    public void createNotecardButton(View v) {
        Button viewSubjectButton = (Button)findViewById(R.id.createNotecardButton);

        viewSubjectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), New_Card_Activity.class);
                intent.putExtra(IntentConstants.SUBJECT_KEY, null);
                intent.putExtra(IntentConstants.COURSE_NUM_KEY, null);
                startActivity(intent);

            }
        });
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
