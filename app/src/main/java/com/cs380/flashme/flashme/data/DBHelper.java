package com.cs380.flashme.flashme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cs380.flashme.flashme.data.DBConstants.Cards;
import com.cs380.flashme.flashme.data.DBConstants.Courses;

import java.util.ArrayList;

/**
 * Created by josh on 4/5/16.
 * A class to handle the creation and upgrade of our database.
 */
public class DBHelper extends SQLiteOpenHelper{

    //Database Versions correspond to schema changes
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "cards.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_COURSES_TABLE = "CREATE TABLE " + Courses.TABLE_NAME + " (" +
                Courses.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Courses.COLUMN_COURSE_NUM + " INTEGER NOT NULL, " +
                Courses.COLUMN_SUBJECT + " TEXT NOT NULL, " +
                Courses.COLUMN_USER_MADE + " INTEGER NOT NULL DEFAULT " + DBConstants.NOT_USER_MADE
                + " );";

        final String SQL_CREATE_CARDS_TABLE = "CREATE TABLE " + Cards.TABLE_NAME + " (" +
                Cards.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cards.COLUMN_DATE_CREATED + " TEXT NOT NULL, " +
                Cards.COLUMN_FRONT + " TEXT NOT NULL, " +
                Cards.COLUMN_BACK + " TEXT NOT NULL, " +
                Cards.COLUMN_USER_MADE + " INTEGER NOT NULL DEFAULT " + DBConstants.NOT_USER_MADE + ", " +
                Cards.COLUMN_COURSE_ID + " INTEGER NOT NULL, " +
                "  FOREIGN KEY (" + Cards.COLUMN_COURSE_ID + ") REFERENCES " + Courses.TABLE_NAME +
                "(" + Courses.ID + ")" + " );";

        db.execSQL(SQL_CREATE_CARDS_TABLE);
        db.execSQL(SQL_CREATE_COURSES_TABLE);
    }

    @Override
    // This will only be fired if we change the version of the DB. Will
    // not implement unless we need to change schema.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void insertSubjects(){
        SQLiteDatabase db = getWritableDatabase();
        final String[][] defaultCourses = {
                {"Computer Science","457"},
                {"Mathematics", "330"},
                {"Computer Science", "312"},
                {"Mathematics", "260"},
                {"Computer Science", "380"}
        };

        ContentValues values;

        for (String[] course: defaultCourses){
            values = new ContentValues();
            values.put(Courses.COLUMN_SUBJECT, course[0]);
            values.put(Courses.COLUMN_COURSE_NUM, course[1]);
            values.put(Courses.COLUMN_USER_MADE, DBConstants.NOT_USER_MADE);
            db.insert(Courses.TABLE_NAME, null, values);
        }



    }



    public void insertDefaultCards(){

        int courseId = getCourseId("Computer Science", 457);

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(Cards.COLUMN_COURSE_ID, courseId);
        values.put(Cards.COLUMN_FRONT, "Describe the difference between classification and regression");
        values.put(Cards.COLUMN_BACK, "Classification is discrete, regresssion is continous");
        values.put(Cards.COLUMN_DATE_CREATED, "2016-04-13");
        values.put(Cards.COLUMN_USER_MADE, DBConstants.NOT_USER_MADE);


        db.insert(Cards.TABLE_NAME, null, values);



    }

    private int getCourseId(String subject, int courseNum){
        SQLiteDatabase db = getReadableDatabase();
        String selection = Courses.COLUMN_SUBJECT + " = ? AND " + Courses.COLUMN_COURSE_NUM + " = ? ";

        Cursor cursor = db.query(true,
                Courses.TABLE_NAME,
                null,
                selection,
                new String[]{subject, Integer.toString(courseNum)},
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() != 1){
            Log.d("DBHELPER","Count: "+cursor.getCount());
        }

        cursor.moveToNext();
        int courseId = cursor.getInt(cursor.getColumnIndex(Courses.ID));

        return courseId;
    }

    public ArrayList<String> getSubjects(){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                                true,
                                Courses.TABLE_NAME,
                                new String[] {Courses.COLUMN_SUBJECT},
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        );

        if (cursor.getCount() == 0){
            Log.d("DBHELPER", "GetSubjects");
        }

        ArrayList<String> subjectList = new ArrayList<>();
        while (cursor.moveToNext()){
            subjectList.add(cursor.getString(0));
        }


        return subjectList;
    }

    public ArrayList<FlashCard> getCardsInCourse(String subject, int courseNum){

        int courseId = getCourseId(subject, courseNum);
        final String selection = Cards.COLUMN_COURSE_ID + " = ?";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                Cards.TABLE_NAME,
                null,
                selection,
                new String[]{Integer.toString(courseId)},
                null,
                null,
                null,
                null
        );

        ArrayList<FlashCard> cards = new ArrayList<>();

        if (cursor.getCount() == 0){
            Log.d("DBHELPER","CARDS_IN_COURSE");
        }

        int frontIndex = cursor.getColumnIndex(Cards.COLUMN_FRONT);
        int backIndex = cursor.getColumnIndex(Cards.COLUMN_BACK);
        int userCreatedIndex = cursor.getColumnIndex(Cards.COLUMN_USER_MADE);

        while (cursor.moveToNext()){
                cards.add(
                        new FlashCard(
                                subject,
                                courseNum,
                                cursor.getString(frontIndex),
                                cursor.getString(backIndex),
                                cursor.getInt(userCreatedIndex)
                        )
                );
        }


        return cards;
    }


}
