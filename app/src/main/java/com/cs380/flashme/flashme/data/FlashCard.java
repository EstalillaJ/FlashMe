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
    private int userMade;
    private String date_created;
    private double accuracy;
    protected boolean isModified;
    protected boolean isNew;
    protected long id;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FlashCard(Context context, String subject, int courseNum, String front, String back,
                     int userMade){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userMade = userMade;
        this.date_created = dateFormat.format(Calendar.getInstance().getTime());
        this.isNew = true;
        this.isModified = false;
        this.accuracy = 100.00;
        this.id = DBHelper.getInstance(context).save(this);
    }


    protected FlashCard(String subject, int courseNum, String front, String back,
                        int userMade, String date_created, double accuracy, int id){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userMade = userMade;
        this.date_created = date_created;
        this.isModified = false;
        this.isNew = false;
        this.accuracy = accuracy;
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
        if (!isModified)
            isModified = true;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
        if (!isModified)
            isModified = true;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
        if (!isModified)
            isModified = true;
    }

    public int getUserMade() {
        return userMade;
    }

    public void setUserMade(int userMade) {
        this.userMade = userMade;
        if (!isModified)
            isModified = true;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
        if (!isModified)
            isModified = true;
    }

    public double getAccuracy() {
        return accuracy;
    }


    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
        if (!isModified)
            isModified = true;
    }
}
