package com.cs380.flashme.flashme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.FlashCard;
import com.cs380.flashme.flashme.data.IntentConstants;
import com.cs380.flashme.flashme.network.PullCardRequest;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardsFromDatabase extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Response.Listener<String>, View.OnClickListener,
AdapterView.OnItemClickListener, OnDismissCallback {
    private DBHelper dbHelper;
    private Spinner subjectSpinner, courseNumSpinner;
    private ArrayList<Integer> courseNums;
    private ArrayAdapter<Integer> courseNumAdapter;
    private String mostRecentSubject;
    private int mostRecentCourseNum;
    private long mostRecentCourseId;
    private List<FlashCard> pulledCards;
    private DynamicListView cardListView;
    private ArrayAdapter<String> cardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_from_database);


        dbHelper = DBHelper.getInstance(this);

        //SETTING UP SPINNERS
        subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        courseNumSpinner = (Spinner) findViewById(R.id.courseNumberSpinner);


        ArrayList<String> subjectList = dbHelper.getSubjects();
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(this, R.layout.plaintext_layout, subjectList);

        subjectAdapter.setDropDownViewResource(R.layout.plaintext_layout);
        subjectSpinner.setAdapter(subjectAdapter);


        courseNums = dbHelper.getCoursesNumbersInSubject((String) subjectSpinner.getSelectedItem());
        courseNumAdapter = new ArrayAdapter<Integer>(this, R.layout.plaintext_layout, courseNums);
        courseNumAdapter.setDropDownViewResource(R.layout.plaintext_layout);
        courseNumSpinner.setAdapter(courseNumAdapter);



        subjectSpinner.setOnItemSelectedListener(this);


        //BUTTON
        Button pullCardsButton = (Button) findViewById(R.id.pullCardsButton);
        pullCardsButton.setOnClickListener(this);

        //SET UP LIST VIEW
        cardListView = (DynamicListView) findViewById(R.id.cardsFromDB);
        cardListView.setVisibility(View.INVISIBLE);
        cardListView.setOnItemClickListener(this);
        cardListView.enableSwipeToDismiss(this);


    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String subject = (String) parent.getItemAtPosition(position);
        courseNumAdapter.clear();
        courseNumAdapter.addAll(dbHelper.getCoursesNumbersInSubject(subject));
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void pullCards() {
        mostRecentSubject = (String) subjectSpinner.getSelectedItem();
        mostRecentCourseNum = (int) courseNumSpinner.getSelectedItem();
        mostRecentCourseId = dbHelper.getCourseId(
                mostRecentSubject,
                mostRecentCourseNum
        );
        PullCardRequest pullCardRequest = new PullCardRequest(mostRecentCourseId, this);
        RequestQueue queue = Volley.newRequestQueue(CardsFromDatabase.this);
        queue.add(pullCardRequest);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pullCardsButton:
                pullCards();
                break;

        }
    }

    public void onResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int numCards = jsonResponse.getInt("numCards");
            if (pulledCards == null)
                pulledCards = new ArrayList<>();
            if (numCards > 0) {
                for (int i = 0; i < numCards; i++) {
                    JSONObject cardJson = jsonResponse.getJSONObject(Integer.toString(i));
                    int onlineId = cardJson.getInt("id");
                    String front = cardJson.getString("front");
                    String back = cardJson.getString("back");
                    String date_created = cardJson.getString("date_created");
                    int made_by = cardJson.getInt("user_id");

                    pulledCards.add(
                            new FlashCard(
                                    mostRecentSubject,
                                    mostRecentCourseNum,
                                    front,
                                    back,
                                    date_created,
                                    onlineId,
                                    made_by)
                    );

                    resetCardListView();


                }
            } else if (numCards==0){
                Toast.makeText(this, "Looks like there aren't any cards for that course yet!", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void resetCardListView() {


        ArrayList<String> cardFronts = new ArrayList<String>();
        for (FlashCard card : pulledCards)
            cardFronts.add(card.getFront());

        cardListAdapter = new ArrayAdapter<>(this, R.layout.plaintext_layout, cardFronts);

        //set on click listener for cardList

        cardListView.setAdapter(cardListAdapter);
        cardListView.setVisibility(View.VISIBLE);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {


    }
}