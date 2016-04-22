package com.cs380.flashme.flashme.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by josh on 4/8/16.
 */
public class FlashCard {

    public final String subject;
    public final int courseNum;
    public final String front;
    public final String back;
    public final int userMade;
    public final String date_created;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FlashCard(String subject, int courseNum, String front, String back,
                     int userMade){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userMade = userMade;
        this.date_created = dateFormat.format(Calendar.getInstance().getTime());
    }



    protected FlashCard(String subject, int courseNum, String front, String back,
                        int userMade, String date_created){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userMade = userMade;
        this.date_created = date_created;
    }






}
