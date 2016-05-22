package com.cs380.flashme.flashme.network;

/**
 * Created by Christian on 5/6/16.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateFlashCardRequest extends StringRequest{

    private static final String CREATEFLASHCARD_REQUEST_URL = "http://flashmedatabase.netne.net/flashcard.php";
    private Map<String, String> params;

    public CreateFlashCardRequest(String front, String back, String date_created, int course_id, long user_id, int localRating, Response.Listener<String> listener) {
        super(Method.POST, CREATEFLASHCARD_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("front", front);
        params.put("back", back);
        params.put("date_created", date_created);
        params.put("course_id", course_id + "");
        params.put("user_id", user_id + "");
        params.put("localRating", localRating+"");

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
