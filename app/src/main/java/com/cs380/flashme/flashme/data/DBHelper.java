package com.cs380.flashme.flashme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.cs380.flashme.flashme.data.DBConstants.*;
/**
 * Created by josh on 4/5/16.
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
                Cards.COLUMN_USER_MADE + " INTEGER NOT NULL DEFAULT " + DBConstants.NOT_USER_MADE +
                Cards.COLUMN_COURSE_ID + "INTEGER NOT NULL, " +
                "  FOREIGN KEY (" + Cards.COLUMN_COURSE_ID + ") REFERENCES " + Courses.TABLE_NAME +
                "(" + Courses.ID + ")" + " );";


    }

    @Override
    // This will only be fire if we change the schema of the DB. Will
    // not implement unless we need to change schema.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
