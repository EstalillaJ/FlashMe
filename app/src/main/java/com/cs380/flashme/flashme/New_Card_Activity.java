package com.cs380.flashme.flashme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cs380.flashme.flashme.data.FlashCard;

public class New_Card_Activity extends AppCompatActivity {

    private EditText SUBJECT;
    private EditText COURSE_NUMBER;
    private EditText FRONT_DESCRIPTION;
    private EditText BACK_DESCRIPTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__card_);

        SUBJECT = (EditText) findViewById(R.id.Course_Subject_ID);
        COURSE_NUMBER = (EditText) findViewById(R.id.Course_Number_ID);
        FRONT_DESCRIPTION = (EditText) findViewById(R.id.Side_Front_Text);
        BACK_DESCRIPTION = (EditText) findViewById(R.id.Side_Back_Text);

    }


    public void CardSubmitButton(View v){

        String Course_Subject = SUBJECT.getText().toString();
        int Course_Number = Integer.parseInt(COURSE_NUMBER.getText().toString());
        String Front_Description = FRONT_DESCRIPTION.getText().toString();
        String Back_Description = BACK_DESCRIPTION.getText().toString();

        FlashCard newCard = new FlashCard(Course_Subject, Course_Number, Front_Description, Back_Description, 1);

        Intent intent = new Intent(this, MainActivity.class);



    }

    public void CardViewButton(View v){

        Intent intent = new Intent(this, Card_Viewer.class);
        startActivity(intent);

    }



}
