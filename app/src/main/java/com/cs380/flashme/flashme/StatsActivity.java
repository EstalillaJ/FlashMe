package com.cs380.flashme.flashme;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cs380.flashme.flashme.Util.Session;
import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.data.IntentConstants;
import com.cs380.flashme.flashme.network.PullHighScoresRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity implements Response.Listener<String> {
    private String subject;
    private String courseNum;
    private String courseTitle;
    private String courseAccuracy;
    private Course course;
    private ListView accuracyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        DBHelper dbHelper = DBHelper.getInstance(this);

        subject = getIntent().getStringExtra(IntentConstants.SUBJECT_KEY);
        courseNum =  getIntent().getStringExtra(IntentConstants.COURSE_NUM_KEY);
        course = dbHelper.getCourse(subject, Integer.parseInt(courseNum));

        PullHighScoresRequest PullHighScoresRequest = new PullHighScoresRequest(course.getId(), this);
        RequestQueue queue = Volley.newRequestQueue(StatsActivity.this);
        queue.add(PullHighScoresRequest);

        // Sets course title
        courseTitle = subject + " " + courseNum + " Statistics:";
        TextView textView = (TextView) findViewById(R.id.ClassIDTitle);
        textView.setText(courseTitle);

        // Sets accuracy
        courseAccuracy = "Accuracy: " +course.getAccuracy() +"%";
        TextView textView2 =(TextView) findViewById(R.id.CourseAccuracy);
        textView.setText(courseAccuracy);

        accuracyListView = (ListView) findViewById(R.id.HighScoreList);

    }

    public void onResponse(String response){

        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            JSONObject accuracyResponse;

            if (success) {

                int numScores = jsonResponse.getInt("numScores");

                ArrayList <String> responseList = new ArrayList<>();
                if(numScores == 0) {
                    String notAvailable = "NO HIGH SCORES AVAILABLE";
                    responseList.add(0, notAvailable);
                }
                else{
                    for (int i = 0; i <= numScores - 1; i++) {

                        accuracyResponse = jsonResponse.getJSONObject(i + ""); //has user name and accuracy

                        double accuracy = accuracyResponse.getDouble("accuracy");
                        String userName = accuracyResponse.getString("username");
                        responseList.add(i, userName + ": " + accuracy);

                    }
                }
                ArrayAdapter<String> HighScoreAdapter = new ArrayAdapter<>(this, R.layout.plaintext_layout, responseList);
                accuracyListView.setAdapter(HighScoreAdapter);

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this);
                builder.setMessage("Course ID Failed to Load")
                        .setNegativeButton("Try again", null)
                        .create()
                        .show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
