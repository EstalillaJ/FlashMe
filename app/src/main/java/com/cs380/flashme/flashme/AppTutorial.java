package com.cs380.flashme.flashme;

import android.content.Intent;
import android.os.Bundle;

import com.cs380.flashme.flashme.data.DBHelper;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by josh on 5/5/16.
 */
public class AppTutorial extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState){
        addSlide(AppIntroFragment.newInstance("Selecting a Course", "Your first step to creating a new notecard is through the course tab. Simply click on the drop down menu and select the Course you wish to create a notecard for!", R.drawable.background, android.R.color.holo_blue_light));
        addSlide(AppIntroFragment.newInstance("Selecting a Course Number", "Next, tap the drop down menu for your desired Course Number, then tap the course you desire.", R.drawable.background, 124));
        addSlide(AppIntroFragment.newInstance("Writing Your Notecard", "You will see two fields below the Course Number field. These are the Front of Card and Back of Card fields. Tap them and enter the information you wish to study!", R.drawable.background, android.R.color.holo_blue_light));
        addSlide(AppIntroFragment.newInstance("Creating and Saving Your Notecards", "Once you are done with your card descriptions, there is only one thing left to do! Submit it! Simply tap the CREATE button on the bottom of the screen!", R.drawable.background, android.R.color.holo_blue_light));
        addSlide(AppIntroFragment.newInstance("Congratulations!", "You have successfully created your new notecard! You can find all your other notecards and download new cards through the 'View Subject' and 'Get New Cards!' menus! Thanks for your support of Flash Me!", R.drawable.background, android.R.color.holo_blue_light));

        showSkipButton(true);
        setProgressButtonEnabled(true);

    }

    private void loadMainActivity(){
        DBHelper.getInstance(this).setTutorialViewed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onDonePressed(){
        loadMainActivity();
    }

    @Override
    public void onSlideChanged(){

    }

    @Override
    public void onNextPressed(){

    }
}
