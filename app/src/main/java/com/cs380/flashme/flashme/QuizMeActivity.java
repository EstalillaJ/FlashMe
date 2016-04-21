package com.cs380.flashme.flashme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizMeActivity extends AppCompatActivity {

    //new card set
    private Button startButton, nextButton, prevButton;
    private TextView cardQuestion;
    private TextView cardAnswer;
    private int width,height;
    private String frontQuestion, backAnswer, dateCreated, id, courseID;
    private boolean isUserMade;
    private double accuracyStats;
    //private QuizMeCardFragment front;
    //private QuizMeCardFragmentBack back;
    //private Fragment Ffront, Fback;
    //private FragmentManager fm;
    //private FragmentTransaction ft;


    //new
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_me);


        //get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        //front = new QuizMeCardFragment();
        //back = new QuizMeCardFragmentBack();

        //fm = getFragmentManager();
        //ft = fm.beginTransaction();



        //set up next button
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setVisibility(View.INVISIBLE);
        prevButton = (Button) findViewById(R.id.prevButton);
        prevButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call next card and set as view
                cardQuestion.setText("New Question");
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardQuestion.setText("Last Question");
            }
        });
        //set up start button
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setText(getString(R.string.start));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ft.attach(front);
                //ft.commit();
                //setContentView(findViewById(R.id.quizMeCardFragmentFront));
                startButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
                cardQuestion.setVisibility(View.VISIBLE);
            }
        });

        //set up text views
        cardQuestion = (TextView) findViewById(R.id.cardQuestion);
        cardQuestion.setVisibility(View.INVISIBLE);
        cardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ft.attach(back);
                //ft.commit();
                //setContentView(R.layout.quiz_me_back_fragment);
                cardQuestion.setText("Test Question");
            }
        });

    }
    public void setDBInfo(){

        //frontQuestion =
        //backAnswer =


    }



}
