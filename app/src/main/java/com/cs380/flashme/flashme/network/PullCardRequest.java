package com.cs380.flashme.flashme.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by josh on 5/9/16.
 */
public class PullCardRequest extends StringRequest{
    private static final String PULLCARDS_REQUEST_URL = "http://flashmedatabase.netne.net/searchCards.php";
    private Map<String, String> params;

    /**
     *
     * @param courseId
     * @param listener
     */
    public PullCardRequest(long courseId, long userId, Response.Listener<String> listener){
        super(Method.POST, PULLCARDS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("courseid", ""+courseId);
        params.put("userid", ""+userId);


        //TODO We need to find a way to let the POST method know which card id's we've already seen
        // Possibly just put all ten with -1 for numbers if we didnt get 10?
        // (Assuming we limit our pull to ten cards).

    }

    public Map<String,String> getParams(){
        return params;
    }
}
