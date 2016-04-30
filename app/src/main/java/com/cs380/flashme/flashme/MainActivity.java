package com.cs380.flashme.flashme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.cs380.flashme.flashme.data.DBConstants;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import android.graphics.Matrix;
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

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        Button createNoteCard = (Button)findViewById(R.id.createNotecardButton);


        createNoteCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), New_Card_Activity.class);
                intent.putExtra(IntentConstants.SUBJECT_KEY, "");
                intent.putExtra(IntentConstants.COURSE_NUM_KEY, "");
                startActivity(intent);

            }
        });

        Button viewSubjectButton = (Button)findViewById(R.id.viewSubjectsButton);

        viewSubjectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                startActivity(intent);
            }
        });
    }



}
