package com.cs380.flashme.flashme;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cs380.flashme.flashme.Util.ProgressGenerator;
import com.cs380.flashme.flashme.Util.Session;
import com.cs380.flashme.flashme.data.DBConstants;
import com.cs380.flashme.flashme.data.DBHelper;
import com.cs380.flashme.flashme.network.LoginRequest;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Christian on 4/23/16.
 */

public class LoginActivity extends AppCompatActivity implements ProgressGenerator.OnCompleteListener, View.OnClickListener {

    private EditText EDIT_USERNAME;
    private EditText EDIT_PASSWORD;
    private ActionProcessButton BUTTON_LOGIN;
    private TextView REGISTER_LINK;
    private TextView SKIP_LOGIN_BUTTON;
    public static String PREFS = "LoginPrefs";
    private CheckBox CHECKBOX;
    private SharedPreferences settings;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DBHelper dbHelper = DBHelper.getInstance(this);
        if (dbHelper.tutorialNotViewed()){
            startActivity(new Intent(this, AppTutorial.class));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if ((settings = getSharedPreferences(PREFS, 0)).getBoolean("stayLoggedIn", false)){
            Session.logIn(settings.getLong("userID", DBConstants.NO_USER));
            startActivity(new Intent(this, MainActivity.class));
        }
        EDIT_USERNAME = (EditText) findViewById(R.id.username);
        EDIT_PASSWORD = (EditText) findViewById(R.id.password);
        BUTTON_LOGIN = (ActionProcessButton) findViewById(R.id.loginButton);
        REGISTER_LINK = (TextView) findViewById(R.id.registerLink);
        SKIP_LOGIN_BUTTON = (TextView) findViewById(R.id.skipLogin);
        CHECKBOX = (CheckBox) findViewById(R.id.stayLoggedInBox);
        BUTTON_LOGIN.setOnClickListener(this);
        REGISTER_LINK.setOnClickListener(this);
        SKIP_LOGIN_BUTTON.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                login();
                break;
            case R.id.registerLink:
                register();
                break;
            case R.id.skipLogin:
                skipLogin();
                break;
        }
    }

    public void login() {
        final ProgressGenerator PROGRESS_GENERATOR = new ProgressGenerator(this);
        BUTTON_LOGIN.setMode(ActionProcessButton.Mode.ENDLESS);

        final String username = EDIT_USERNAME.getText().toString();
        final String password = EDIT_PASSWORD.getText().toString();

        PROGRESS_GENERATOR.start(BUTTON_LOGIN);

        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        PROGRESS_GENERATOR.success();
                        BUTTON_LOGIN.setProgress(90);
                        String name = jsonResponse.getString("name");
                        Session.logIn(jsonResponse.getLong("userid"));
                        if (CHECKBOX.isChecked())
                            stayLoggedIn();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("username", username);
                        LoginActivity.this.startActivity(intent);
                    } else {
                        BUTTON_LOGIN.setProgress(-1);
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

    public void register() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }

    public void skipLogin() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void stayLoggedIn(){
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("stayLoggedIn", true);
        editor.putLong("userID", Session.userId);
        editor.commit();
    }


    public void onComplete() {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.cs380.flashme.flashme/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.cs380.flashme.flashme/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
