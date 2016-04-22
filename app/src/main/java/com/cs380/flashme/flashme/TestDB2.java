package com.cs380.flashme.flashme;

import android.content.Context;
import android.database.Cursor;

import com.cs380.flashme.flashme.data.Course;
import com.cs380.flashme.flashme.data.DBConstants;
import com.cs380.flashme.flashme.data.DBHelper;

/**
 * Created by josh on 4/20/16.
 */
public class TestDB2 {
    private Context  context;

    private DBHelper dbHelper;
    public TestDB2(Context context){
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }

    /**
     * To be run before testRemoveCard
     */
    public void testModifyCard(){
        Course course = dbHelper.getCourse("Computer Science",457);
        course.getCards().get(0).setCourseNum(312);
        String dateCreated = course.getCards().get(0).getDateCreated();
        dbHelper.save(course.getCards().get(0));
        Course newCourse = dbHelper.getCourse("Computer Science", 312);
        if (!newCourse.getCards().get(0).getDateCreated().equals(dateCreated))
            throw new RuntimeException("");
    }

    public void testRemoveCard(){
        Course course = dbHelper.getCourse("Computer Science", 312);
        course.removeCard(course.getCards().get(0));

        if (!course.getCards().isEmpty()){
            throw new RuntimeException("");
        }

        Cursor cursor = dbHelper.getReadableDatabase().query(DBConstants.Cards.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        if (cursor.getCount() != 0){
            throw new RuntimeException("");
        }
    }


}
