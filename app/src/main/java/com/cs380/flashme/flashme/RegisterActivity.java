package com.cs380.flashme.flashme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Christian on 4/23/16.
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void test() {

        // Create variable, of type EditText, and then makes that of type edit text
        // and goes back to being assigned to textName
        final EditText TEXT_NAME = (EditText) findViewById(R.id.textName);
        final EditText EDIT_USERNAME = (EditText) findViewById(R.id.editUsername);
        final EditText EDIT_PASSWORD = (EditText) findViewById(R.id.editPassword);

        final Button  REGISTER_BUTTON = (Button) findViewById(R.id.registerButton);

    }
}
