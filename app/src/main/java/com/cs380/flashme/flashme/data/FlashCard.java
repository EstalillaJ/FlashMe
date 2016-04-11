package com.cs380.flashme.flashme.data;

import android.content.Context;

/**
 * Created by josh on 4/8/16.
 */
public class FlashCard {

    private final String subject;
    private final int courseNum;
    private final String front;
    private final String back;
    private final int userMade;
    private final long CARD_ID;

    public FlashCard(Context context, String subject, int courseNum, String front, String back,
                     int userMade){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userMade = userMade;
        DBHelper dbHelper = new DBHelper(context);
        CARD_ID = dbHelper.createCard(this);
    }

    protected FlashCard(String subject, int courseNum, String front, String back,
                        int userMade, long CARD_ID){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userMade = userMade;
        this.CARD_ID = CARD_ID;
    }

    public String getSubject(){
        return subject;
    }

    public int getCourseNum(){
        return courseNum;
    }

    public String getFront(){
        return front;
    }

    public String getBack(){
        return back;
    }

    public long getID(){
        return CARD_ID;
    }

    public boolean isUserMade(){
        return userMade != DBConstants.NOT_USER_MADE;
    }



}
