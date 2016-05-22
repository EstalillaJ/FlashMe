package com.cs380.flashme.flashme.data;

import android.content.Context;

import java.util.List;

/**
 * Created by josh on 4/17/16.
 */
public class Course {

    private List<FlashCard> cards;
    private  int courseNum;
    private  double accuracy;
    private  String subject;
    private int userMade;
    private Context context;
    protected long id;


    protected Course(List<FlashCard> cards, double accuracy, String subject, int courseNum,
                  int userMade, long id, Context context) {
        this.cards = cards;
        this.accuracy = accuracy;
        this.subject = subject;
        this.courseNum = courseNum;
        this.userMade = userMade;
        this.id = id;
        this.context = context;
    }

    public void removeCard(FlashCard card){
        if (cards.contains(card)) {
            cards.remove(card);
            updateAccuracy();
            DBHelper dbHelper = DBHelper.getInstance(context);
            dbHelper.removeCard(card);
            dbHelper.updateCourse(this);
        }
    }

    public void updateAccuracy(){
        if (cards.size() == 0){
            accuracy = 100.00;
        }
        else {
            double total = 0;
            for (FlashCard card: cards)
                total += card.getAccuracy();
            accuracy = total/cards.size();
        }
    }

    public void addCard(FlashCard card){
        cards.add(card);
    }

    public int isUserMade() {
        return userMade;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<FlashCard> getCards(){
        return cards;
    }
    public long getId() {return id;}

}
