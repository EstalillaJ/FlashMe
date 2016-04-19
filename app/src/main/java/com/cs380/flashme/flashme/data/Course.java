package com.cs380.flashme.flashme.data;

import java.util.ArrayList;

/**
 * Created by josh on 4/17/16.
 */
public class Course {

    private  ArrayList<FlashCard> cards;
    private  double accuracy;
    private  String subject;
    private  int courseNum;

    public Course(ArrayList<FlashCard> cards, double accuracy, String subject, int courseNum) {
        this.cards = cards;
        this.accuracy = accuracy;
        this.subject = subject;
        this.courseNum = courseNum;
    }



}
