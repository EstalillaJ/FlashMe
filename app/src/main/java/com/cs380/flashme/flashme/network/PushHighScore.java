package com.cs380.flashme.flashme.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brennan on 5/17/2016.
 */
public class PushHighScore extends StringRequest {

    // Fields
    private static final String HIGHSCORE_PUSH_URL = "http://flashmedatabase.netne.net/insertAccuracy.php";
    private Map<String, String> params;

    // Default Constructor
    public PushHighScore(long courseID, long userID, double anAccuracy, Response.Listener<String> listener){
        super(Method.POST, HIGHSCORE_PUSH_URL, listener, null);
        params = new HashMap<>();
        params.put("courseID", Long.toString(courseID));
        params.put("userID", Long.toString(userID));
        params.put("accuracy", Double.toString(anAccuracy));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }



}
