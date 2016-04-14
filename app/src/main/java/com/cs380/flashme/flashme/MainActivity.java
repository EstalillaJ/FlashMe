package com.cs380.flashme.flashme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        dbHelper.insertSubjects();
        dbHelper.insertDefaultCards();
        ArrayList<String> subjects =  dbHelper.getSubjects();

        ArrayList<FlashCard> cards = dbHelper.getCardsInCourse("Computer Science", 457);
        FlashCard card = cards.get(0);
        if (subjects.size() == 2 && subjects.contains("Computer Science") && subjects.contains("Mathematics"))
            textView.setText(card.front + "\n" + card.back);



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
