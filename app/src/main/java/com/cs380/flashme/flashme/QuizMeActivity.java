package com.cs380.flashme.flashme;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs380.flashme.flashme.Util.OnSwipeTouchListener;
import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuizMeActivity extends AppCompatActivity {

    //new card set
    private Button correctButton, incorrectButton;
    private TextView cardQuestion,cardIndexView;
    private double accuracyStats;
    private int currentCardIndex;
    private DBHelper dbHelper;
    private Course course;
    private List<FlashCard> cards;
    private boolean correct, incorrect;
    private FlashCard currentCard;
    private boolean frontDisplayed = true;
    private Resources resources;
    private FrameLayout cardFrame;



    //new
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_me);

        resources = getResources();

        dbHelper = DBHelper.getInstance(this);
        Intent intent = getIntent();

        course = dbHelper.getCourse(
                intent.getStringExtra(IntentConstants.SUBJECT_KEY),
                Integer.parseInt(intent.getStringExtra(IntentConstants.COURSE_NUM_KEY))
        );
        cards = course.getCards();

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


        currentCardIndex = 0;
        currentCard = cards.get(currentCardIndex);

        // The card frame covers a larger area then the text. So they don't have to swipe
        // directly on the text for the swipe to be registered.
        cardFrame = (FrameLayout) findViewById(R.id.cardFrame);

        cardQuestion = (TextView) findViewById(R.id.cardQuestion);
        cardIndexView = (TextView) findViewById(R.id.cardIndexView);




        new CountDownTimer(5000,100) {
            public void onTick(long millisUntilFinished){
                cardQuestion.setText("Start in " + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
            }

            public void onFinish(){
                cardQuestion.setText(currentCard.getFront());
                //We don't set this until the timer is done. Otherwise they could swipe left
                //before the start
                cardFrame.setOnTouchListener(new OnSwipeTouchListener(QuizMeActivity.this){
                    public void onSwipeLeft(){
                        displayNextCard();
                    }
                    public void onClick(){
                        cardClicked();
                    }
                    public void onSwipeRight(){
                        displayPreviousCard();
                    }
                });

                //Start timer and let them know what card they are on
                cardIndexView.setText("On card 1 of " + cards.size());
                Chronometer chronometer = (Chronometer) findViewById(R.id.quizTimer);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

            }
        }.start();






    }


    public void correctButtonClick(){
        correct = true;
        //updateAccuracy()
        correctButton.setVisibility(View.INVISIBLE);
        incorrectButton.setVisibility(View.INVISIBLE);
    }
    public void incorrectButtonClick(){
        incorrect = true;
        checkUserAnswer();
        correctButton.setVisibility(View.INVISIBLE);
        incorrectButton.setVisibility(View.INVISIBLE);
    }



    public void cardClicked(){
        TextView textView = (TextView) findViewById(R.id.cardQuestion);
        if (frontDisplayed) {
            textView.setText(currentCard.getBack());
            correctButton.setVisibility(View.VISIBLE);
            incorrectButton.setVisibility(View.VISIBLE);
            frontDisplayed = false;
        }
        else {
            textView.setText(currentCard.getFront());
            correctButton.setVisibility(View.INVISIBLE);
            incorrectButton.setVisibility(View.INVISIBLE);
            frontDisplayed = true;
        }
    }

    public void displayNextCard(){
        currentCardIndex++;
        if (currentCardIndex < cards.size()){
            currentCard = cards.get(currentCardIndex);
            cardQuestion.setText(currentCard.getFront());
        }
        else{
            currentCard = null;
            cardQuestion.setText(resources.getString(R.string.endOfSet));
        }
        //In case they swipe left while the back was displayed
        frontDisplayed = true;
        correctButton.setVisibility(View.INVISIBLE);
        incorrectButton.setVisibility(View.INVISIBLE);
    }

    public void displayPreviousCard(){
        if (currentCardIndex > 0){
            currentCardIndex--;
            currentCard = cards.get(currentCardIndex);
            cardQuestion.setText(currentCard.getFront());


            //If they swiped right on the first card
            //we don't want to change any of these values
            frontDisplayed = true;
            correctButton.setVisibility(View.INVISIBLE);
            incorrectButton.setVisibility(View.INVISIBLE);
        }


    }

    public void checkUserAnswer(){
        if(correct = true){
            cards.get(currentCardIndex).getAccuracy();
           // currentCard.
        }else{
            cards.get(currentCardIndex).getAccuracy();
        }
    }
}
