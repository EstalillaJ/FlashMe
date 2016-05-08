package com.cs380.flashme.flashme;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cs380.flashme.flashme.data.DBConstants;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;
import com.cs380.flashme.flashme.network.CreateFlashCardRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*Updated by Ryan, 4/26/16
*
*/



public class New_Card_Activity extends AppCompatActivity implements  Response.Listener<String> {

    private String frontText;
    private String backText;
    private String subject;
    private int courseNum;

    private Spinner subjectSpinner;
    private Spinner courseNumSpinner;
    private EditText frontDescription;
    private EditText backDescription;
    private DBHelper dbHelper;
    private ArrayList<Integer> courseNums;
    private ArrayAdapter<Integer> courseNumAdapter;
    private FlashCard card;
    private int onlineId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        String subjectFromIntent = getIntent().getStringExtra(IntentConstants.SUBJECT_KEY);
        String courseNumFromIntent = getIntent().getStringExtra(IntentConstants.COURSE_NUM_KEY);

        dbHelper = DBHelper.getInstance(this);

        subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        courseNumSpinner = (Spinner) findViewById(R.id.courseNumberSpinner);
        frontDescription = (EditText) findViewById(R.id.Side_Front_Text);
        backDescription = (EditText) findViewById(R.id.Side_Back_Text);

        if (getIntent().getBooleanExtra(IntentConstants.EXISTING_CARD_KEY, false)) {
            card = dbHelper.getCard(
                    getIntent().getLongExtra(IntentConstants.CARD_ID_KEY, 0L),
                    subjectFromIntent,
                    Integer.parseInt(courseNumFromIntent)
            );
            frontDescription.setText(card.getFront());
            backDescription.setText(card.getBack());
        }

        ArrayList<String> subjectList = dbHelper.getSubjects();
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(this, R.layout.subject_layout, subjectList);

        subjectAdapter.setDropDownViewResource(R.layout.subject_layout);
        subjectSpinner.setAdapter(subjectAdapter);

        if (!subjectFromIntent.equals(""))
            subjectSpinner.setSelection(subjectList.indexOf(subjectFromIntent));


        courseNums = dbHelper.getCoursesNumbersInSubject((String) subjectSpinner.getSelectedItem());
        courseNumAdapter = new ArrayAdapter<Integer>(this, R.layout.subject_layout, courseNums);
        courseNumAdapter.setDropDownViewResource(R.layout.subject_layout);
        courseNumSpinner.setAdapter(courseNumAdapter);

        if (!courseNumFromIntent.equals(""))
            courseNumSpinner.setSelection(courseNums.indexOf(Integer.parseInt(courseNumFromIntent)));

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subject = (String) parent.getItemAtPosition(position);
                courseNumAdapter.clear();
                courseNumAdapter.addAll(dbHelper.getCoursesNumbersInSubject(subject));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

    }

    public void testSending () {

    }

    public void CardSubmitButton(View v){
        frontText = frontDescription.getText().toString();
        backText = backDescription.getText().toString();
        subject = (String) subjectSpinner.getSelectedItem();
        courseNum = (int) courseNumSpinner.getSelectedItem();

        if (card != null) {
            card.setFront(frontText);
            card.setBack(backText);
            card.setCourseNum(courseNum);
            card.setSubject(subject);
            dbHelper.save(card);
        }
        else {
            card = new FlashCard(this,
                    subject,
                    courseNum,
                    frontText,
                    backText,
                    DBConstants.NO_USER,
                    DBConstants.NO_USER
            );
        }
        CreateFlashCardRequest createFlashCardRequest = new CreateFlashCardRequest(frontText,
                backText,
                card.getDateCreated(),
                courseNum, card.getUserId(),
                card.getAccuracy(),
                card.getNumAttempts(),
                this);

        RequestQueue queue = Volley.newRequestQueue(New_Card_Activity.this);
        queue.add(createFlashCardRequest);


    }

    public void saveCard(int onlineId){



        if (card != null) {
            card.setFront(frontText);
            card.setBack(backText);
            card.setCourseNum(courseNum);
            card.setSubject(subject);
            card.setOnlineId(onlineId);
            dbHelper.save(card);
        }
        else {
            card = new FlashCard(this,
                    subject,
                    courseNum,
                    frontText,
                    backText,
                    onlineId,
                    DBConstants.NO_USER
                    );
        }

        finish();
    }

    public void onResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            onlineId = jsonResponse.getInt("id");
            if (success) {
                Intent intent = new Intent(New_Card_Activity.this, LoginActivity.class);
                New_Card_Activity.this.startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(New_Card_Activity.this);
                builder.setMessage("You failed to create a notecard")
                        .setNegativeButton("Try again", null)
                        .create()
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveCard(onlineId);
    }





}
