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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText, usernameEditText, passwordEditText, emailEditText, confirmPasswordEditText;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Create variable, of type EditText, and then makes that of type edit text
        // and goes back to being assigned to textName
        emailEditText = (EditText) findViewById(R.id.email);
        nameEditText = (EditText) findViewById(R.id.textName);
        usernameEditText = (EditText) findViewById(R.id.editUsername);
        passwordEditText = (EditText) findViewById(R.id.editPassword);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPassword);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this );
    }



    public void onClick(View view){
        final String name = nameEditText.getText().toString();
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String email = emailEditText.getText().toString();
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

        RegisterRequest registerRequest = new RegisterRequest(name, username, password, email, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }
}
