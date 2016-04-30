package com.cs380.flashme.flashme;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
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

public class QuizMeActivity extends AppCompatActivity {

    //new card set
    private Button startButton, nextButton, prevButton, correctButton, incorrectButton;
    private TextView cardQuestion;
    private int width,height;
    private double accuracyStats;
    private int currentCardIndex;
    private DBHelper dbHelper;
    private Course course;
    private List<FlashCard> cards;
    private boolean correct, incorrect;
    private FlashCard currentCard;
    private boolean frontDisplayed = true;
    private Resources resources;



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







        currentCardIndex = 0;
        currentCard = cards.get(currentCardIndex);

        //set up text views
        cardQuestion = (TextView) findViewById(R.id.cardQuestion);
        cardQuestion.setText(currentCard.getFront());

        FrameLayout cardFrame = (FrameLayout) findViewById(R.id.cardFrame);

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
            currentCard = cards.get(0);
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
