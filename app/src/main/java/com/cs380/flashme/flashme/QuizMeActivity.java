package com.cs380.flashme.flashme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;

import java.util.ArrayList;
import java.util.List;

public class QuizMeActivity extends AppCompatActivity {

    //new card set
    private Button startButton, nextButton, prevButton, correctButton, incorrectButton;
    private TextView cardQuestion;
    private int width,height;
    private String frontQuestion, backAnswer, dateCreated, id, courseID;
    private boolean isUserMade;
    private double accuracyStats;
    private int cardIndex;
    private DBHelper dbHelper;
    private Course course;
    private List<FlashCard> cards;
    private boolean correct, incorrect;
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

        correctButton = (Button) findViewById(R.id.correctButton);
        correctButton.setVisibility(View.INVISIBLE);
        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctButtonClick();
            }
        });

        incorrectButton = (Button) findViewById(R.id.incorrectButton);
        incorrectButton.setVisibility(View.INVISIBLE);
        incorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incorrectButtonClick();
            }
        });
        //set up next button
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setVisibility(View.INVISIBLE);
        prevButton = (Button) findViewById(R.id.prevButton);
        prevButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call next card and set as view
                nextButtonClick();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevButtonClick();
            }
        });
        //set up start button
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setText(getString(R.string.start));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonClick();
            }
        });


        cardIndex = 0;
        //set up text views
        cardQuestion = (TextView) findViewById(R.id.cardQuestion);
        cardQuestion.setVisibility(View.INVISIBLE);
        cardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ft.attach(back);
                //ft.commit();
                //setContentView(R.layout.quiz_me_back_fragment);
                //cardQuestion.setText("Test Question");
            }
        });

    }
    public ArrayList<String> getCourseInfo(){
        ArrayList<String> courseInfo = new ArrayList<String>();
        return courseInfo;
    }
    public void setDBInfo(){

        getCourseInfo();
        dbHelper = DBHelper.getInstance(this);
        course = dbHelper.getCourse("Computer Science", 457);
        cards = course.getCards();
        frontQuestion = cards.get(cardIndex).getFront();
        backAnswer = cards.get(cardIndex).getBack();
        //frontQuestion =
        //backAnswer =


    }
    public void correctButtonClick(){
        correct = true;
        checkUserAnswer();
        correctButton.setVisibility(View.INVISIBLE);
        incorrectButton.setVisibility(View.INVISIBLE);
    }
    public void incorrectButtonClick(){
        incorrect = true;
        checkUserAnswer();
        correctButton.setVisibility(View.INVISIBLE);
        incorrectButton.setVisibility(View.INVISIBLE);
    }
    public void setCardQuestion(){

        cardQuestion.setText(frontQuestion);
        //setDBInfo();
    }
    public void setCardAnswer(){


        cardQuestion.setText(backAnswer);
        //setDBInfo();
    }
    public void cardQuestionClick(){
        if(cardQuestion.getText().equals(frontQuestion)){
            cardQuestion.setText(backAnswer);
            prevButton.setVisibility(View.VISIBLE);
            correctButton.setVisibility(View.VISIBLE);
        }else{

        }
    }
    public void startButtonClick(){
        startButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        cardQuestion.setVisibility(View.VISIBLE);
    }
    public void nextButtonClick(){

        prevButton.setVisibility(View.VISIBLE);
        if(cardIndex > cards.size()){
            cardQuestion.setText("You have reached the end of this set...\n" + "You made it.");
        }else{
            cardIndex++;
            setDBInfo();
        }

    }
    public void prevButtonClick(){
        if(!(cardIndex == 0)){
            cardIndex--;
            setDBInfo();
        }else{
            prevButton.setVisibility(View.INVISIBLE);
        }
    }
    public void checkUserAnswer(){
        if(correct = true){
            cards.get(cardIndex).getAccuracy();
        }else{
            cards.get(cardIndex).getAccuracy();
        }
    }
}
