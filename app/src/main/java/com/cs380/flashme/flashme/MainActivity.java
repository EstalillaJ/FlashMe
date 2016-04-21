package com.cs380.flashme.flashme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {

    private String[] mainListItems = {"Quiz Yourself!", "Add A New FlashCard", "My Favorite Cards"};
    private ArrayAdapter<String> mMainListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestDB2 testDB2 = new TestDB2(this);


        testDB2.testModifyCard();
        testDB2.testRemoveCard();
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
