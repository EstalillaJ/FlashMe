package com.cs380.flashme.flashme.data;

/**
 * Created by josh on 4/8/16.
 */
public class FlashCard {

    public final String subject;
    public final int courseNum;
    public final String front;
    public final String back;
    public final int userMade;
    private long CARD_ID;

    public FlashCard(String subject, int courseNum, String front, String back,
                     int userMade){
        this.subject = subject;
        this.courseNum = courseNum;
        this.front = front;
        this.back = back;
        this.userMade = userMade;

    }

    public long getCARD_ID(){
        return CARD_ID;
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






}
