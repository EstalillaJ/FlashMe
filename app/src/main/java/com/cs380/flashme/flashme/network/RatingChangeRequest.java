package com.cs380.flashme.flashme.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josh on 5/21/16.
 */
public class RatingChangeRequest extends StringRequest {

    private HashMap<String,String> params = new HashMap<>();
    private static final String URL = "http://flashmedatabase.netnet.net/changeRating.php";

    public RatingChangeRequest(int localRating, int cardOnlineId, long userId, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        params.put("localRating", ""+localRating);
        params.put("cardId", ""+ cardOnlineId);
        params.put("userId", ""+userId);

    }

    @Override
    public Map<String,String> getParams(){ return params;}
}
