package com.cs380.flashme.flashme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cs380.flashme.flashme.data.DBConstants.Cards;
import com.cs380.flashme.flashme.data.DBConstants.Courses;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by josh on 4/5/16.
 * A class to handle the creation and upgrade of our database.
 */
public class DBHelper extends SQLiteOpenHelper{

    //Database Versions correspond to schema changes
    private static final int DATABASE_VERSION = 7;
    private static DBHelper sInstance;
    private Context mContext;

    static final String DATABASE_NAME = "flashMe.db";

    private DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

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
                Courses.COLUMN_ACCURACY + " REAL NOT NULL DEFAULT 100.00" +
                " );";

        final String SQL_CREATE_CARDS_TABLE = "CREATE TABLE " + Cards.TABLE_NAME + " (" +
                Cards.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cards.COLUMN_DATE_CREATED + " TEXT NOT NULL, " +
                Cards.COLUMN_FRONT + " TEXT NOT NULL, " +
                Cards.COLUMN_BACK + " TEXT NOT NULL, " +
                Cards.COLUMN_ACCURACY + " REAL NOT NULL DEFAULT 100.00, " +
                Cards.COLUMN_NUMBER_OF_ATTEMPTS + " INTEGER NOT NULL DEFAULT 0, " +
                Cards.USER_ID + " INTEGER NOT NULL DEFAULT " + DBConstants.NO_USER + ", " +
                Cards.ONLINE_ID + " INTEGER NOT NULL DEFAULT " + DBConstants.NO_USER + ", " +
                Cards.COLUMN_COURSE_ID + " INTEGER NOT NULL, " +
                "  FOREIGN KEY (" + Cards.COLUMN_COURSE_ID + ") REFERENCES " + Courses.TABLE_NAME +
                "(" + Courses.ID + ")" + " );";

        db.execSQL(SQL_CREATE_CARDS_TABLE);
        db.execSQL(SQL_CREATE_COURSES_TABLE);

        insertDefaultSubjects(db);

    }

    @Override
    // This will only be fired if we change the version of the DB. Will
    // not implement unless we need to change schema.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Cards.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Courses.TABLE_NAME);
        onCreate(db);
    }



    public void insertDefaultSubjects(SQLiteDatabase db){
        try {
            InputStream is = mContext.getAssets().open("insertCourses.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            db.beginTransaction();

            while ((line = br.readLine())!= null){
                db.execSQL(line);
            }
            db.setTransactionSuccessful();
        }
        catch (IOException e){

        }
        db.endTransaction();
    }





    public long insertCard(FlashCard card){
        long courseId = getCourseId(card.getSubject(), card.getCourseNum());

        SQLiteDatabase db  = getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(Cards.COLUMN_COURSE_ID, courseId);
        values.put(Cards.COLUMN_FRONT, card.getFront());
        values.put(Cards.COLUMN_BACK, card.getBack());
        values.put(Cards.COLUMN_DATE_CREATED, card.getDateCreated());
        values.put(Cards.USER_ID, card.getUserId());
        values.put(Cards.COLUMN_ACCURACY, card.getAccuracy());
        values.put(Cards.COLUMN_NUMBER_OF_ATTEMPTS, card.getNumAttempts());
        values.put(Cards.ONLINE_ID, card.getOnlineId());

        card.setId(db.insert(Cards.TABLE_NAME, null, values));
        return card.getId();
    }

    public long getCourseId(String subject, int courseNum){
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
        long courseId = cursor.getInt(cursor.getColumnIndex(Courses.ID));
        cursor.close();
        return courseId;
    }

    /**
     *
     * @return a list of all subjects in the database
     */

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
        cursor.close();
        return subjectList;
    }

    public ArrayList<Integer> getCoursesNumbersInSubject(String subject){
        SQLiteDatabase db = getReadableDatabase();
        String selection = Courses.COLUMN_SUBJECT + " = ? ";
        Cursor cursor = db.query(true,
                Courses.TABLE_NAME,
                new String[]{Courses.COLUMN_COURSE_NUM},
                selection,
                new String[]{subject},
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
        cursor.close();
        return courseNumbers;

    }


    /**
     *
     * @param subject - The subject of the course
     * @param courseNum - The course number of the course
     * @return - A course object with accuracy and flashcards, or null if there is no course for
     * the given arguments.
     */
    public Course getCourse(String subject, int courseNum){

        long courseId = getCourseId(subject, courseNum);
        final String cardSelection = Cards.COLUMN_COURSE_ID + " = ?";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cardCursor = db.query(
                Cards.TABLE_NAME,
                null,
                cardSelection,
                new String[]{Long.toString(courseId)},
                null,
                null,
                null,
                null
        );

        ArrayList<FlashCard> cards = new ArrayList<>();


        if (cardCursor.getCount() != 0) {
            int frontIndex = cardCursor.getColumnIndex(Cards.COLUMN_FRONT);
            int backIndex = cardCursor.getColumnIndex(Cards.COLUMN_BACK);
            int userCreatedIndex = cardCursor.getColumnIndex(Cards.USER_ID);
            int dateCreatedIndex = cardCursor.getColumnIndex(Cards.COLUMN_DATE_CREATED);
            int accuracyIndex = cardCursor.getColumnIndex(Cards.COLUMN_ACCURACY);
            int attemptsIndex = cardCursor.getColumnIndex(Cards.COLUMN_NUMBER_OF_ATTEMPTS);
            int idIndex = cardCursor.getColumnIndex(Cards.ID);

            while (cardCursor.moveToNext()) {
                cards.add(
                        new FlashCard(
                                subject,
                                courseNum,
                                cardCursor.getString(frontIndex),
                                cardCursor.getString(backIndex),
                                cardCursor.getInt(userCreatedIndex),
                                cardCursor.getString(dateCreatedIndex),
                                cardCursor.getDouble(accuracyIndex),
                                cardCursor.getInt(attemptsIndex),
                                DBConstants.NO_USER,
                                cardCursor.getInt(idIndex)
                        )
                );
            }

        }


        String courseSelection = Courses.ID + " = ?";
        Cursor courseCursor = db.query(true,
                Courses.TABLE_NAME,
                new String[] {Courses.COLUMN_ACCURACY},
                courseSelection,
                new String[] {Long.toString(courseId)},
                null,
                null,
                null,
                null
        );

        if (courseCursor.getCount() == 1){

            int accIndex = courseCursor.getColumnIndex(Courses.COLUMN_ACCURACY);
            courseCursor.moveToNext();
            Course course = new Course(cards,
                    courseCursor.getDouble(accIndex),
                    subject,
                    courseNum,
                    DBConstants.NO_USER,
                    courseId,
                    mContext);
            courseCursor.close();
            cardCursor.close();
            return course;
        }


        return null;
    }


    /**
     *
     * @param card - card to be modified
     * @return returns the number of rows affected (1), or throws an exception if multiple rows affected
     */
    public int modifyCard(FlashCard card){
        SQLiteDatabase db = getWritableDatabase();

        long courseId = getCourseId(card.getSubject(),card.getCourseNum());
        ContentValues values = new ContentValues();
        String whereClause = Cards.ID + " = ?";
        String[] whereArgs = {Long.toString(card.id)};

        values.put(Cards.COLUMN_ACCURACY, card.getAccuracy());
        values.put(Cards.USER_ID, card.getUserId());
        values.put(Cards.COLUMN_BACK, card.getBack());
        values.put(Cards.COLUMN_COURSE_ID, courseId);
        values.put(Cards.COLUMN_DATE_CREATED, card.getDateCreated());
        values.put(Cards.COLUMN_FRONT, card.getFront());
        values.put(Cards.COLUMN_NUMBER_OF_ATTEMPTS, card.getNumAttempts());
        values.put(Cards.ONLINE_ID, card.getOnlineId());

        int rowsAffected = db.update(Cards.TABLE_NAME, values, whereClause, whereArgs);
        if (rowsAffected > 1)
            throw new RuntimeException("Multiple cards modified by one call to modifyCard()");
        return rowsAffected;
    }

    /**
     *
     * @param card - card to be saved to the database, new or modified
     * @return the database id of the card that was saved.
     */
    public long save(FlashCard card){
        if (card.isNew)
            return insertCard(card);
        else if (card.isModified)
            modifyCard(card);
        return card.id;
    }

    public void removeCard(FlashCard card){

        SQLiteDatabase db = getWritableDatabase();
        String deleteQuery = Cards.ID + " = ?";

        db.delete(Cards.TABLE_NAME,
                deleteQuery,
                new String[] {Long.toString(card.id)}
        );
    }

    public void updateCourse(Course course){
        SQLiteDatabase db = getWritableDatabase();
        String courseSelection = Courses.ID + " = ?";

        ContentValues values = new ContentValues();

        values.put(Courses.COLUMN_ACCURACY, course.getAccuracy());
        values.put(Courses.COLUMN_COURSE_NUM, course.getCourseNum());
        values.put(Courses.COLUMN_SUBJECT, course.getSubject());

        db.update(Courses.TABLE_NAME,
                values,
                courseSelection,
                new String[]{Long.toString(course.id)}
        );
    }

    public FlashCard getCard(long id, String subject, int courseNum){
        SQLiteDatabase db = getReadableDatabase();
        String cardSelection = Cards.ID +" = ? ";



        Cursor cursor = db.query(true,
                Cards.TABLE_NAME,
                null,
                cardSelection,
                new String[] {Long.toString(id)},
                null,
                null,
                null,
                null
            );


        int frontIndex = cursor.getColumnIndex(Cards.COLUMN_FRONT);
        int backIndex = cursor.getColumnIndex(Cards.COLUMN_BACK);
        int dateCreatedIndex = cursor.getColumnIndex(Cards.COLUMN_DATE_CREATED);
        int userMadeIndex = cursor.getColumnIndex(Cards.USER_ID);
        int accuracyIndex = cursor.getColumnIndex(Cards.COLUMN_ACCURACY);
        int numAttemptsIndex = cursor.getColumnIndex(Cards.COLUMN_NUMBER_OF_ATTEMPTS);
        int onlineId = cursor.getColumnIndex(Cards.ONLINE_ID);
        cursor.moveToNext();
        FlashCard card = new FlashCard(
                subject,
                courseNum,
                cursor.getString(frontIndex),
                cursor.getString(backIndex),
                Integer.parseInt(cursor.getString(userMadeIndex)),
                cursor.getString(dateCreatedIndex),
                Double.parseDouble(cursor.getString(accuracyIndex)),
                Integer.parseInt(cursor.getString(numAttemptsIndex)),
                Integer.parseInt(cursor.getString(onlineId))
                ,id);

        return card;
    }
}
