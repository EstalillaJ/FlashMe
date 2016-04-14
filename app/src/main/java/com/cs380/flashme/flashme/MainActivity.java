package com.cs380.flashme.flashme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs380.flashme.flashme.data.DBConstants;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] mainListItems = {"Quiz Yourself!", "Add A New FlashCard", "My Favorite Cards"};
    private ArrayAdapter<String> mMainListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.cardView);
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.insertCourse("Biology", 101);
        ArrayList<Integer> biologyCourses = dbHelper.getCoursesInSubject("Biology");
        ArrayList<String> subjects =  dbHelper.getSubjects();

        if (subjects.contains("Biology") && subjects.contains("Computer Science") && subjects.contains("Mathematics"))
            textView.setText("Success");
        FlashCard newCard = new FlashCard("Mathematics", 330, "Blah", "Blah", DBConstants.NOT_USER_MADE);

        dbHelper.insertCard(newCard);
        ArrayList<FlashCard> flashCards = dbHelper.getCardsInCourse("Mathematics", 330);
        textView.setText( textView.getText() + flashCards.get(0).front + " " + biologyCourses.get(0));

    }

    public void createNotecardButton(View v) {
        // do something when the button is clicked
    }

    public void viewCoursesButton(View v) {
// do something when the button is clicked
    }

    public void previousSessionButton(View v) {
// do something when the button is clicked
    }

}
