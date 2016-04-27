package com.cs380.flashme.flashme;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Christian on 4/23/16.
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Create variable, of type EditText, and then makes that of type edit text
        // and goes back to being assigned to textName
        final EditText TEXT_NAME = (EditText) findViewById(R.id.textName);
        final EditText EDIT_USERNAME = (EditText) findViewById(R.id.editUsername);
        final EditText EDIT_PASSWORD = (EditText) findViewById(R.id.editPassword);

        final Button  REGISTER_BUTTON = (Button) findViewById(R.id.registerButton);

        REGISTER_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = TEXT_NAME.getText().toString();
                final String username = EDIT_USERNAME.getText().toString();
                final String password = EDIT_PASSWORD.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("You failed to register")
                                        .setNegativeButton("Try again", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name, username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }

}
