package com.cs380.flashme.flashme.data;

import android.content.Context;

import com.cs380.flashme.flashme.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by josh on 4/8/16.
 */
public class FlashCard {

    private String subject;
    private int courseNum;
    private String front;
    private String back;
    private long userId;
    private String date_created;
    private double accuracy;
    private int numRatings;
    private double rating;
    private int localRating;
    private int numAttempts;

    //For database use
    protected boolean isModified;
    protected boolean isNew;
    protected long id;
    private int onlineId;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * This is the constructor used for new local cards.
     */
    public FlashCard(String subject, int courseNum, String front, String back, int localRating,
                     int onlineId, long userId){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userId = userId;
        this.date_created = dateFormat.format(Calendar.getInstance().getTime());
        this.isNew = true;
        this.isModified = false;
        this.accuracy = 100.00;
        this.numAttempts = 0;
        this.numRatings = 1;
        this.localRating = localRating;
        this.rating = localRating;
        this.onlineId = onlineId;
        //TODO return user id from login
        this.id =  -1;
    }

    /**
     * This is the constructor used for cards pulled from the server.
     *
     */
    public FlashCard( String subject, int courseNum, String front, String back,  String date_created,
                      double rating, int localRating, int numRatings,
                       int onlineId, int userId){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.onlineId = onlineId;
        this.date_created = date_created;
        this.userId = userId;
        this.rating = rating;
        this.localRating = localRating;
        this.numRatings = numRatings;
        this.accuracy = 100.00;
        this.numAttempts = 0;
        this.isNew = true;

    }

    /**
     *
     * This is the constructor used to create flashcard model objects from the local database.
     */
    protected FlashCard(String subject, int courseNum, String front, String back,
                        int userId, String date_created, double accuracy, int numAttempts,
                        double rating, int localRating, int numRatings,
                        int onlineId, long id){

        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userId = userId;
        this.date_created = date_created;
        this.isModified = false;
        this.isNew = false;
        this.accuracy = accuracy;
        this.onlineId = onlineId;
        this.numAttempts = numAttempts;
        if (rating == DBConstants.Cards.NO_RATING)
            this.rating = localRating;
        else{
            this.rating = rating;
        }
        this.localRating = localRating;
        this.numRatings = numRatings;
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        if (!isModified)
            isModified = true;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
        this.isNew = false;

        if (!isModified)
            isModified = true;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
        this.isNew = false;

        if (!isModified)
            isModified = true;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
        this.isNew = false;

        if (!isModified)
            isModified = true;
    }

    public long getUserId() {
        return userId;
    }



    public String getDateCreated() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
        this.isNew = false;

        if (!isModified)
            isModified = true;
    }

    public double getAccuracy() {
        return accuracy;
    }


    public void setAccuracy(boolean guessWasCorrect) {
        double oldAccuracy = this.accuracy;
        numAttempts++;
        if (numAttempts == 0){

        }
        if (guessWasCorrect){
            this.accuracy = (oldAccuracy*(numAttempts-1)+100)/(numAttempts);
        }
        else{
            this.accuracy = (oldAccuracy*(numAttempts-1))/(numAttempts);
        }

        this.isNew = false;

        if (!isModified)
            isModified = true;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
        isModified = true;
    }

    public void setNumRatings(int numRatings){
        this.numRatings = numRatings;
        isModified = true;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public int getLocalRating() {
        return localRating;
    }

    public void setLocalRating(int newRating) {
        //updates the average rating. Note that this will be overwritten later
        //when we make a call to the server. We implement it here incase they are not connected
        if (this.localRating == DBConstants.Cards.NO_RATING && rating != DBConstants.Cards.NO_RATING)
            this.rating = ((this.rating*numRatings)+newRating)/(numRatings);
        else
            this.rating = ((this.rating*(numRatings)-localRating)+newRating)/(numRatings);
        this.localRating = newRating;
        isNew = false;
        isModified = true;
    }

    public long getId(){
        return id;
    }

    public int getNumAttempts() {
        return numAttempts;
    }

    public void setNumAttempts(int numAttempts) {
        this.isNew = false;
        this.numAttempts = numAttempts;
        if (!isModified)
            isModified = true;
    }

    public int getOnlineId(){
        return onlineId;
    }

    public void setOnlineId(int onlineId){
        this.isNew = false;
        this.isModified = true;
        this.onlineId = onlineId;
    }

    public String displayString(Context context){
        String displayString = "";
        boolean halfStarNum = false;
        boolean roundUp = false;
        String fullStar = context.getString(R.string.star_icon); //full star
        String halfStar = context.getString(R.string.half_star_icon);; //half star
        String emptyStar = context.getString(R.string.empty_star_icon); //empty star
        double rating = getRating();
        if (rating - (int) rating > .25){
            if (rating - (int) rating<.75){
                halfStarNum = true;
            }
            else{
                roundUp = true;
            }
        }

        for (int i = 0; i < (int) rating; i++){
            displayString += fullStar;
        }
        if (halfStarNum)
            displayString += halfStar;
        else if (roundUp)
            displayString += fullStar;
        else if (((int) rating - rating) != 0)
            displayString += emptyStar;

        for (int i = 5-(int)rating; i >0; i--)
            displayString+= emptyStar;

        return displayString += "    " + getFront();
    }
    protected void setId(long id) {
        this.id = id;
    }



}
