package com.cs380.flashme.flashme;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity implements ProgressGenerator.OnCompleteListener, View.OnClickListener {

    private  EditText EDIT_USERNAME;
    private  EditText EDIT_PASSWORD;
    private  ActionProcessButton BUTTON_LOGIN;
    private  TextView REGISTER_LINK;
    private  TextView SKIP_LOGIN_BUTTON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EDIT_USERNAME = (EditText) findViewById(R.id.username);
        EDIT_PASSWORD = (EditText) findViewById(R.id.password);
        BUTTON_LOGIN = (ActionProcessButton) findViewById(R.id.loginButton);
        REGISTER_LINK = (TextView) findViewById(R.id.registerLink);
        SKIP_LOGIN_BUTTON = (TextView) findViewById(R.id.skipLogin);

        BUTTON_LOGIN.setOnClickListener(this);
        REGISTER_LINK.setOnClickListener(this);
        SKIP_LOGIN_BUTTON.setOnClickListener(this);
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.loginButton:
                login();
                break;
            case R.id.registerButton:
                register();
                break;
            case R.id.skipLogin:
                skipLogin();
                break;
        }
    }

    public void login(){
        final ProgressGenerator PROGRESS_GENERATOR = new ProgressGenerator(this);
        BUTTON_LOGIN.setMode(ActionProcessButton.Mode.ENDLESS);

        PROGRESS_GENERATOR.start(BUTTON_LOGIN);

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

    public void register(){
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }

    public void skipLogin(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    public void onComplete(){

    }
}
