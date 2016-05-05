package com.cs380.flashme.flashme;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cs380.flashme.flashme.Util.ProgressGenerator;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Christian on 4/23/16.
 */

public class LoginActivity extends AppCompatActivity implements ProgressGenerator.OnCompleteListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText EDIT_USERNAME = (EditText) findViewById(R.id.username);
        final EditText EDIT_PASSWORD = (EditText) findViewById(R.id.password);
        final ActionProcessButton BUTTON_LOGIN = (ActionProcessButton) findViewById(R.id.loginButton);
        final TextView REGISTER_LINK = (TextView) findViewById(R.id.registerLink);
        final ProgressGenerator progressGenerator = new ProgressGenerator(this);

        BUTTON_LOGIN.setMode(ActionProcessButton.Mode.ENDLESS);
        REGISTER_LINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);


            }
        });

        TextView skipLoginButton = (TextView) findViewById(R.id.skipLogin);

        skipLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        BUTTON_LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressGenerator.start(BUTTON_LOGIN);
                final String username = EDIT_USERNAME.getText().toString();
                final String password = EDIT_PASSWORD.getText().toString();

                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String name = jsonResponse.getString("name");

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                LoginActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Failed to login")
                                        .setNegativeButton("Try again", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    public void onComplete(){

    }
}
