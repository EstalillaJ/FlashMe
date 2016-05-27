package com.cs380.flashme.flashme.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XerxcesPC on 5/24/2016.
 */
public class PullHighScoresRequest extends StringRequest {

    private static final String PULLHIGHSCORES_REQUEST_URL = "http://flashmedatabase.netne.net/pullHighscores.php";
    private HashMap<String, String> params;

    public PullHighScoresRequest(long CourseID, Response.Listener<String> listener){

        super(Method.POST, PULLHIGHSCORES_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("course_id", CourseID + "");


    }

    @Override
    public Map<String, String> getParams(){ return params; }
}
