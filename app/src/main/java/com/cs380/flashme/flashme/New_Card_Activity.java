package com.cs380.flashme.flashme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cs380.flashme.flashme.Util.Session;
import com.cs380.flashme.flashme.data.DBConstants;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;
import com.cs380.flashme.flashme.network.CreateFlashCardRequest;
import com.cs380.flashme.flashme.network.RatingChangeRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*Updated by Ryan, 4/26/16
*
*/



public class New_Card_Activity extends AppCompatActivity implements  Response.Listener<String>, AdapterView.OnItemSelectedListener,
RatingBar.OnRatingBarChangeListener {

    private String frontText;
    private String backText;
    private String subject;
    private int courseNum;
    private int courseId;


    private Spinner subjectSpinner, courseNumSpinner;
    private EditText frontDescription,backDescription;
    private RatingBar ratingBar;
    private DBHelper dbHelper;
    private ArrayList<Integer> courseNums;
    private ArrayAdapter<Integer> courseNumAdapter;
    private FlashCard card;
    private int onlineId;
    private boolean cardFromOnlineDatabase = false;
    private boolean ratingChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        //Get Data from Intent
        Intent intent = getIntent();
        dbHelper = DBHelper.getInstance(this);
        String subjectFromIntent = intent.getStringExtra(IntentConstants.SUBJECT_KEY);
        String courseNumFromIntent = intent.getStringExtra(IntentConstants.COURSE_NUM_KEY);
        if (intent.getBooleanExtra(IntentConstants.ONLINE_CARD, false))
            cardFromOnlineDatabase = true;

        //Get UI Elements
        subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        courseNumSpinner = (Spinner) findViewById(R.id.courseNumberSpinner);
        frontDescription = (EditText) findViewById(R.id.Side_Front_Text);
        backDescription = (EditText) findViewById(R.id.Side_Back_Text);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //No Rating if your are not logged in
        if (!Session.loggedIn)
            ratingBar.setVisibility(View.INVISIBLE);

        //Set up Subject Spinner
        ArrayList<String> subjectList = dbHelper.getSubjects();
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(this, R.layout.plaintext_layout, subjectList);
        subjectAdapter.setDropDownViewResource(R.layout.plaintext_layout);
        subjectSpinner.setAdapter(subjectAdapter);
        if (!subjectFromIntent.equals(""))
            subjectSpinner.setSelection(subjectList.indexOf(subjectFromIntent));
        subjectSpinner.setOnItemSelectedListener(this);


        //Set up coursenum Spinner
        courseNums = dbHelper.getCoursesNumbersInSubject((String) subjectSpinner.getSelectedItem());
        courseNumAdapter = new ArrayAdapter<>(this, R.layout.plaintext_layout, courseNums);
        courseNumAdapter.setDropDownViewResource(R.layout.plaintext_layout);
        courseNumSpinner.setAdapter(courseNumAdapter);
        if (!courseNumFromIntent.equals(""))
            courseNumSpinner.setSelection(courseNums.indexOf(Integer.parseInt(courseNumFromIntent)));

        //If we are not making a new card, but editing one
        if (intent.getBooleanExtra(IntentConstants.EXISTING_CARD_KEY, false)) {
            card = dbHelper.getCard(
                    getIntent().getLongExtra(IntentConstants.CARD_ID_KEY, 0L),
                    subjectFromIntent,
                    Integer.parseInt(courseNumFromIntent)
            );
            frontDescription.setText(card.getFront());
            backDescription.setText(card.getBack());
            ratingBar.setRating(card.getLocalRating());
        }
        ratingBar.setOnRatingBarChangeListener(this);
    }


    public void onRatingChanged(RatingBar bar, float rating, boolean byUser){
        if (byUser && card != null){
            ratingChanged = true;
        }

    }
    /**
     * Changes the course numbers available when the subject is changed.
     *
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String subject = (String) parent.getItemAtPosition(position);
        courseNumAdapter.clear();
        courseNumAdapter.addAll(dbHelper.getCoursesNumbersInSubject(subject));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

    /**
     * If the card comes from the online database, we need to delete it if they pressed
     * back without pressing create.
     */
    public void onBackPressed () {
        if (cardFromOnlineDatabase)
            dbHelper.removeCard(card);
        super.onBackPressed();
    }

    public void CardSubmitButton(View v){
        frontText = frontDescription.getText().toString();
        backText = backDescription.getText().toString();
        subject = (String) subjectSpinner.getSelectedItem();
        courseNum = (int) courseNumSpinner.getSelectedItem();
        courseId = (int) dbHelper.getCourseId(subject,courseNum);


        //Checks if the card is new or has been changed.
        //Note that the right side of the conditional will not be checked if the card is null.
        if (card == null || !( card.getFront().equals(frontText)
            && card.getBack().equals(backText)
            && card.getCourseNum() == courseNum
            && card.getSubject().equals(subject)) ){
            if (card != null)
                dbHelper.removeCard(card);
            card = new FlashCard(
                        subject,
                        courseNum,
                        frontText,
                        backText,
                        (int) ratingBar.getRating(),
                        DBConstants.Cards.NO_ONLINE_ID,
                        Session.userId
                );

            CreateFlashCardRequest createFlashCardRequest = new CreateFlashCardRequest(frontText,
                    backText,
                    card.getDateCreated(),
                    courseId, card.getUserId(),
                    card.getLocalRating(),
                    this);
            dbHelper.save(card);
            RequestQueue queue = Volley.newRequestQueue(New_Card_Activity.this);
            queue.add(createFlashCardRequest);

        }
        else if (ratingChanged){
            card.setLocalRating((int) ratingBar.getRating());
            dbHelper.save(card);
            //We only update if the card has already been pushed.
            //By the time we get here this only won't be true if
            //They made a card but decided not to push it, or they
            //didn't have internet connection when they first made it.
            if (card.getOnlineId() != DBConstants.Cards.NO_ONLINE_ID){
                RatingChangeRequest ratingChangeRequest =
                        new RatingChangeRequest(card.getLocalRating(), card.getOnlineId(), Session.userId,
                                new Response.Listener<String>(){ public void onResponse(String response){
                                    try{
                                        JSONObject json = new JSONObject(response);
                                        if (json.getBoolean("success")) {
                                            card.setNumRatings(json.getInt("numRatings"));
                                            dbHelper.save(card);
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Error sending rating. Are you online?", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (JSONException e){
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                    }

                                }});
                RequestQueue queue = Volley.newRequestQueue(New_Card_Activity.this);
                queue.add(ratingChangeRequest);
                //We can exit early here
                onBackPressed();
            }
        }


        // This is required so we don't remove it when we call onBackPressed.
        // The user has chosen to keep this card.
        cardFromOnlineDatabase = false;
        onBackPressed();
    }



    public void onResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            onlineId = jsonResponse.getInt("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        card.setOnlineId(onlineId);

        dbHelper.save(card);
    }





}
