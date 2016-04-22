package com.cs380.flashme.flashme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cs380.flashme.flashme.data.DBConstants.Cards;
import com.cs380.flashme.flashme.data.DBConstants.Courses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by josh on 4/5/16.
 * A class to handle the creation and upgrade of our database.
 */
public class DBHelper extends SQLiteOpenHelper{

    //Database Versions correspond to schema changes
    private static final int DATABASE_VERSION = 1;
    private static DBHelper sInstance;
    private Context mContext;

    static final String DATABASE_NAME = "cards.db";

    private DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context){
        if (sInstance == null){
            sInstance = new DBHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_COURSES_TABLE = "CREATE TABLE " + Courses.TABLE_NAME + " (" +
                Courses.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Courses.COLUMN_COURSE_NUM + " INTEGER NOT NULL, " +
                Courses.COLUMN_SUBJECT + " TEXT NOT NULL, " +
                Courses.COLUMN_ACCURACY + " REAL NOT NULL DEFAULT 100.00, " +
                Courses.COLUMN_USER_MADE + " INTEGER NOT NULL DEFAULT " + DBConstants.NOT_USER_MADE
                + " );";

        final String SQL_CREATE_CARDS_TABLE = "CREATE TABLE " + Cards.TABLE_NAME + " (" +
                Cards.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cards.COLUMN_DATE_CREATED + " TEXT NOT NULL, " +
                Cards.COLUMN_FRONT + " TEXT NOT NULL, " +
                Cards.COLUMN_BACK + " TEXT NOT NULL, " +
                Cards.COLUMN_ACCURACY + " REAL NOT NULL DEFAULT 100.00, " +
                Cards.COLUMN_USER_MADE + " INTEGER NOT NULL DEFAULT " + DBConstants.NOT_USER_MADE + ", " +
                Cards.COLUMN_COURSE_ID + " INTEGER NOT NULL, " +
                "  FOREIGN KEY (" + Cards.COLUMN_COURSE_ID + ") REFERENCES " + Courses.TABLE_NAME +
                "(" + Courses.ID + ")" + " );";

        db.execSQL(SQL_CREATE_CARDS_TABLE);
        db.execSQL(SQL_CREATE_COURSES_TABLE);

        insertDefaultSubjects(db);
        insertDefaultCards(db);

    }

    @Override
    // This will only be fired if we change the version of the DB. Will
    // not implement unless we need to change schema.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void insertDefaultSubjects(SQLiteDatabase db){

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



    public void insertDefaultCards(SQLiteDatabase db){



        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        values.put(Cards.COLUMN_COURSE_ID, 1);
        values.put(Cards.COLUMN_FRONT, "Describe the difference between classification and regression");
        values.put(Cards.COLUMN_BACK, "Classification is discrete, regresssion is continous");
        values.put(Cards.COLUMN_DATE_CREATED, dateFormat.format(Calendar.getInstance().getTime()));
        values.put(Cards.COLUMN_USER_MADE, DBConstants.NOT_USER_MADE);


        db.insert(Cards.TABLE_NAME, null, values);



    }

    public void insertCourse(String subject, int courseNum){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Courses.COLUMN_USER_MADE, DBConstants.NOT_USER_MADE);
        values.put(Courses.COLUMN_COURSE_NUM, courseNum);
        values.put(Courses.COLUMN_SUBJECT, subject);

        db.insert(Courses.TABLE_NAME, null, values);


    }

    public long insertCard(FlashCard card){
        int courseId = getCourseId(card.getSubject(), card.getCourseNum());

        SQLiteDatabase db  = getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(Cards.COLUMN_COURSE_ID, courseId);
        values.put(Cards.COLUMN_FRONT, card.getFront());
        values.put(Cards.COLUMN_BACK, card.getBack());
        values.put(Cards.COLUMN_DATE_CREATED, card.getDate_created());
        values.put(Cards.COLUMN_USER_MADE, card.getUserMade());
        values.put(Cards.COLUMN_ACCURACY, card.getAccuracy());

        return db.insert(Cards.TABLE_NAME, null, values);
    }

    private int getCourseId(String subject, int courseNum){
        SQLiteDatabase db = getReadableDatabase();
        String selection = Courses.COLUMN_SUBJECT + " = ? AND " + Courses.COLUMN_COURSE_NUM + " = ? ";

        Cursor cursor = db.query(true,
                Courses.TABLE_NAME,
                new String[]{Courses.ID},
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

    public ArrayList<Integer> getCoursesInSubject(String subject){
        SQLiteDatabase db = getReadableDatabase();
        String selection = Courses.COLUMN_SUBJECT + " = ? ";
        Cursor cursor = db.query(true,
                Courses.TABLE_NAME,
                new String[] {Courses.COLUMN_COURSE_NUM},
                selection,
                new String[] {subject},
                null,
                null,
                null,
                null
        );


        ArrayList<Integer> courseNumbers = new ArrayList<>();
        int courseNumberIndex = cursor.getColumnIndex(Courses.COLUMN_COURSE_NUM);
        while (cursor.moveToNext()){
            courseNumbers.add(cursor.getInt(courseNumberIndex));
        }
        return courseNumbers;
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
        int dateCreatedIndex = cursor.getColumnIndex(Cards.COLUMN_DATE_CREATED);
        int accuracyIndex = cursor.getColumnIndex(Cards.COLUMN_ACCURACY);
        int idIndex = cursor.getColumnIndex(Cards.ID);

        while (cursor.moveToNext()){
                cards.add(
                        new FlashCard(
                                subject,
                                courseNum,
                                cursor.getString(frontIndex),
                                cursor.getString(backIndex),
                                cursor.getInt(userCreatedIndex),
                                cursor.getString(dateCreatedIndex),
                                cursor.getDouble(accuracyIndex),
                                cursor.getInt(idIndex)
                        )
                );
        }


        return cards;
    }



    public int modifyCard(FlashCard card){
        SQLiteDatabase db = getWritableDatabase();

        int courseId = getCourseId(card.getSubject(),card.getCourseNum());
        ContentValues values = new ContentValues();
        String whereClause = Cards.ID + " = ?";
        String[] whereArgs = {Long.toString(card.id)};

        values.put(Cards.COLUMN_ACCURACY, card.getAccuracy());
        values.put(Cards.COLUMN_USER_MADE, card.getUserMade());
        values.put(Cards.COLUMN_BACK, card.getBack());
        values.put(Cards.COLUMN_COURSE_ID, courseId);
        values.put(Cards.COLUMN_DATE_CREATED, card.getDate_created());
        values.put(Cards.COLUMN_FRONT, card.getFront());



        return db.update(Cards.TABLE_NAME, values, whereClause, whereArgs);
    }

    public long save(FlashCard card){
        if (card.isNew){
            return insertCard(card);
        }
        else if (card.isModified){
            modifyCard(card);
        }
        return card.id;


    }


}
