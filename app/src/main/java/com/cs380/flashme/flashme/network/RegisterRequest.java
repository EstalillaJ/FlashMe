package com.cs380.flashme.flashme.network;

/**
 * Created by Christian on 4/26/16.
 */

import android.content.res.Resources;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cs380.flashme.flashme.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = Resources.getSystem().getString(R.string.registerPHP);
    private Map<String, String> params;

    public RegisterRequest(String name, String username, String password, String email, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
