package com.cs380.flashme.flashme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.TimedUndoAdapter;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {
    private String subject;
    private String courseNum;
    private Course course;
    private List<FlashCard> cards;
    private String courseTitle;
    private DBHelper dbHelper;
    private ArrayList<String> cardFronts;
    private ArrayAdapter<String> cardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set main ui
        setContentView(R.layout.activity_course);
        //get what was passed to me
        subject = getIntent().getStringExtra(IntentConstants.SUBJECT_KEY);
        courseNum =  getIntent().getStringExtra(IntentConstants.COURSE_NUM_KEY);

        //get a course object, and its cards
        dbHelper = DBHelper.getInstance(this);
        course = dbHelper.getCourse(subject, Integer.parseInt(courseNum));
        cards = course.getCards();

        //set course title for the ui
        courseTitle = subject + " " + courseNum;
        TextView textview = (TextView) findViewById(R.id.courseTitle);
        textview.setText(courseTitle);

        setUpCardList();


    }



    public void onResume(){
        super.onResume();
        course = dbHelper.getCourse(subject, Integer.parseInt(courseNum));
        cards = course.getCards();

        ArrayList<String> cardFronts = new ArrayList<>();
        for (FlashCard card: cards)
            cardFronts.add(card.getFront());

        cardListAdapter.clear();
        cardListAdapter.addAll(cardFronts);
    }

    public void createCardFromCourse(View view) {
        Intent intent = new Intent(this, New_Card_Activity.class);

        intent.putExtra(IntentConstants.COURSE_NUM_KEY, courseNum);
        intent.putExtra(IntentConstants.SUBJECT_KEY, subject);

        startActivity(intent);

    }

    public void FlashMeClicked(View view) {
       Intent intent = new Intent(this, QuizMeActivity.class);

        intent.putExtra(IntentConstants.COURSE_NUM_KEY, courseNum);
        intent.putExtra(IntentConstants.SUBJECT_KEY, subject);

        startActivity(intent);
    }

    public void viewCourseStatistics(View v){
        //TODO launch stats acvtivity
    }


    public void setUpCardList(){
        //create a list containing the fronts of all cards in course
        cardFronts = new ArrayList<>();
        for (FlashCard card: cards)
            cardFronts.add(card.getFront());

        //use the cardFronts list to populate our listview in the ui
        DynamicListView cardListView = (DynamicListView)   findViewById(R.id.cardList);
        cardListAdapter = new ArrayAdapter<>(this, R.layout.subject_layout, cardFronts);

        //set on click listener for cardList
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlashCard card = cards.get(position);
                Intent intent = new Intent(getApplicationContext(), New_Card_Activity.class);
                intent.putExtra(IntentConstants.COURSE_NUM_KEY, courseNum);
                intent.putExtra(IntentConstants.SUBJECT_KEY, subject);
                intent.putExtra(IntentConstants.EXISTING_CARD_KEY, true);
                intent.putExtra(IntentConstants.CARD_ID_KEY, card.getId());
                startActivity(intent);
            }
        });

        //Allow users to swipe and remove cards
        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(cardListAdapter, CourseActivity.this, new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {
                for (int i: reverseSortedPositions) {
                    cardFronts.remove(i);
                    dbHelper.removeCard(cards.get(i));
                }
                cardListAdapter.notifyDataSetChanged();
            }
        });

        swipeUndoAdapter.setAbsListView(cardListView);
        cardListView.setAdapter(swipeUndoAdapter);
        cardListView.enableSimpleSwipeUndo();
    }
}
