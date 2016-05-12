package com.cs380.flashme.flashme.data;

import android.content.Context;

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

    private int numAttempts;

    //For database use
    protected boolean isModified;
    protected boolean isNew;
    protected long id;
    private int onlineId;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FlashCard(Context context, String subject, int courseNum, String front, String back,
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
        this.onlineId = onlineId;
        //TODO return user id from login
        this.id =  -1;
    }

    public FlashCard(String subject, int courseNum, String front, String back, String date_created, int onlineId, int userId){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.onlineId = onlineId;
        this.date_created = date_created;
        this.userId = userId;
        this.accuracy = 100.00;
        this.numAttempts = 0;
        this.isNew = true;
    }


    protected FlashCard(String subject, int courseNum, String front, String back,
                        int userId, String date_created, double accuracy, int numAttempts,
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


    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
        this.isNew = false;

        if (!isModified)
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

    protected void setId(long id) {

    }



}
