package com.cs380.flashme.flashme;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cs380.flashme.flashme.Util.OnSwipeTouchListener;
import com.cs380.flashme.flashme.Util.Session;
import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;
import com.cs380.flashme.flashme.network.PushHighScore;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuizMeActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<String>{

    //new card set
    private Button correctButton, incorrectButton;
    private TextView cardQuestion,cardIndexView, swipeHint;
    private double accuracyStats;
    private int currentCardIndex;
    private DBHelper dbHelper;
    private Course course;
    private List<FlashCard> cards;
    private boolean correct, incorrect;
    private boolean accuracyChanged = false;
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
        accuracyStats = course.getAccuracy();

        cards = course.getCards();

        correctButton = (Button) findViewById(R.id.correctButton);
        correctButton.setVisibility(View.INVISIBLE);
        correctButton.setOnClickListener(this);

        incorrectButton = (Button) findViewById(R.id.incorrectButton);
        incorrectButton.setVisibility(View.INVISIBLE);
        incorrectButton.setOnClickListener(this);


        currentCardIndex = 0;
        currentCard = cards.get(currentCardIndex);

        // The card frame covers a larger area then the text. So they don't have to swipe
        // directly on the text for the swipe to be registered.
        cardFrame = (FrameLayout) findViewById(R.id.cardFrame);

        cardQuestion = (TextView) findViewById(R.id.cardQuestion);
        cardIndexView = (TextView) findViewById(R.id.cardIndexView);


        swipeHint = (TextView) findViewById(R.id.swipeHint);

        new QuizMeCountDown(3000,100).start();
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.incorrectButton:
                incorrectButtonClick();
                displayNextCard();
                break;
            case R.id.correctButton:
                correctButtonClick();
                displayNextCard();
                break;
        }
    }


    public void correctButtonClick(){
        cards.get(currentCardIndex).setAccuracy(true);
        dbHelper.save(cards.get(currentCardIndex));

        course.updateAccuracy();
        correctButton.setVisibility(View.INVISIBLE);
        incorrectButton.setVisibility(View.INVISIBLE);
        accuracyChanged = true;
    }
    public void incorrectButtonClick(){
        cards.get(currentCardIndex).setAccuracy(false);
        dbHelper.save(cards.get(currentCardIndex));
        course.updateAccuracy();
        correctButton.setVisibility(View.INVISIBLE);
        incorrectButton.setVisibility(View.INVISIBLE);
        accuracyChanged = true;
    }

    public void cardClicked(){
        if (currentCardIndex < cards.size()) {
            TextView textView = (TextView) findViewById(R.id.cardQuestion);
            if (frontDisplayed) {
                textView.setText(currentCard.getBack());
                correctButton.setVisibility(View.VISIBLE);
                incorrectButton.setVisibility(View.VISIBLE);
                frontDisplayed = false;
            } else {
                textView.setText(currentCard.getFront());
                correctButton.setVisibility(View.INVISIBLE);
                incorrectButton.setVisibility(View.INVISIBLE);
                frontDisplayed = true;
            }
        }
    }

    public void displayNextCard(){
        currentCardIndex++;
        if (currentCardIndex < cards.size()){
            currentCard = cards.get(currentCardIndex);
            cardQuestion.setText(currentCard.getFront());
            cardIndexView.setText(String.format(
                    getString(R.string.cardIndexString),
                    currentCardIndex+1,
                    cards.size()) );
        }
        else{
            currentCard = null;
            cardIndexView.setText(getString(R.string.cardIndexEnd));
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
            cardIndexView.setText(String.format(
                    getString(R.string.cardIndexString),
                    currentCardIndex+1,
                    cards.size()));

            //If they swiped right on the first card
            //we don't want to change any of these values
            frontDisplayed = true;
            correctButton.setVisibility(View.INVISIBLE);
            incorrectButton.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onBackPressed(){
        if (this.accuracyChanged && Session.loggedIn) {
            PushHighScore highScore = new PushHighScore(course.getId(), Session.userId, course.getAccuracy(), this);
            RequestQueue queue = Volley.newRequestQueue(QuizMeActivity.this);
            queue.add(highScore);
        }
        dbHelper.updateCourse(course);
        super.onBackPressed();
    }

    @Override
    public void onResponse(String response) {

    }


    private class QuizMeCountDown extends CountDownTimer implements Animation.AnimationListener {

        public QuizMeCountDown(long time, long update){
            super(time,update);
        }

        public void onTick(long millisUntilFinished){
            if (millisUntilFinished < 1000)
                cardQuestion.setText(R.string.app_name);
            else
                cardQuestion.setText(String.format(
                        getString(R.string.timerCountDown),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                ));
        }

        public void onFinish(){
            cardQuestion.setText(currentCard.getFront());
            // We don't set this until the timer is done. Otherwise they could swipe left
            // before the start
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
            cardIndexView.setText(String.format(
                    getString(R.string.cardIndexString),
                    currentCardIndex+1,
                    cards.size()));

            Chronometer chronometer = (Chronometer) findViewById(R.id.quizTimer);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            AlphaAnimation fadeOut = new AlphaAnimation(1.0f,0.0f);
            fadeOut.setAnimationListener(this);
            fadeOut.setDuration(1000);
            swipeHint.startAnimation(fadeOut);
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            swipeHint.setText(getString(R.string.emptyString));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
