package com.cs380.flashme.flashme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText EDIT_USERNAME = (EditText) findViewById(R.id.editUsername);
        final TextView LOGIN_MESSAGE = (TextView) findViewById(R.id.loginMessage);

    }
}
