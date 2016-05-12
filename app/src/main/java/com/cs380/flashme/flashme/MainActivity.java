package com.cs380.flashme.flashme;


import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import com.cs380.flashme.flashme.data.IntentConstants;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Flash Me!</font>"));
        setSupportActionBar(toolbar);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        double percentImageView = .50;
        double percentButton = .10;
        ImageView imageView = (ImageView) findViewById(R.id.cardImage);
        imageView.getLayoutParams().height = (int) (height*percentImageView);



        Button createNoteCard = (Button) findViewById(R.id.createNotecardButton);


        createNoteCard.setOnClickListener(this);
        createNoteCard.getLayoutParams().height = (int) (height*percentButton);

        Button viewSubjectButton = (Button) findViewById(R.id.viewSubjectsButton);
        viewSubjectButton.getLayoutParams().height = (int) (height*percentButton);

        viewSubjectButton.setOnClickListener(this);

        Button getNewCardsButton = (Button) findViewById(R.id.getNewCardsButton);
        getNewCardsButton.getLayoutParams().height = (int) (height*percentButton);

        getNewCardsButton.setOnClickListener(this);
    }




    public void onClick(View v) {
        switch(v.getId()){
            case R.id.viewSubjectsButton:
                launchViewSubjects();
                break;
            case R.id.createNotecardButton:
                launchCreateNotecard();
                break;
            case R.id.getNewCardsButton:
                launchGetNewCards();
                break;

        }
    }


    public void launchViewSubjects(){
        Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
        startActivity(intent);
    }

    public void launchCreateNotecard(){
        Intent intent = new Intent(getApplicationContext(), New_Card_Activity.class);
        intent.putExtra(IntentConstants.SUBJECT_KEY, "");
        intent.putExtra(IntentConstants.COURSE_NUM_KEY, "");
        startActivity(intent);
    }

    public void launchGetNewCards(){
        Intent intent = new Intent(getApplicationContext(), CardsFromDatabase.class);
        startActivity(intent);
    }


}




