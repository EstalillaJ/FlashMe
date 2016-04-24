package com.cs380.flashme.flashme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Christian on 4/23/16.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText EDIT_USERNAME = (EditText) findViewById(R.id.editUsername);
        final EditText EDIT_PASSWORD = (EditText) findViewById(R.id.editPassword);
        final Button BUTTON_LOGIN = (Button) findViewById(R.id.loginButton);
        final TextView REGISTER_LINK = (TextView) findViewById(R.id.registerLink);

        REGISTER_LINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);


            }
        });
    }
}
