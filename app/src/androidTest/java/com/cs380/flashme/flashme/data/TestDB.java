package com.cs380.flashme.flashme.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDB extends AndroidTestCase {

    public static final String LOG_TAG = TestDB.class.getSimpleName();

    // Since we want each TestDB2 to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each TestDB2 is executed to delete the database.  This makes
        sure that we always have a clean TestDB2.
     */
    public void setUp() {
        deleteTheDatabase();
    }


    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(DBConstants.Cards.TABLE_NAME);
        tableNameHashSet.add(DBConstants.Courses.TABLE_NAME);

        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
        SQLiteDatabase db = DBHelper.getInstance(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );


        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + DBConstants.Cards.TABLE_NAME+ ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> cardColumns = new HashSet<String>();
        cardColumns.add(DBConstants.Cards.ID);
        cardColumns.add(DBConstants.Cards.COLUMN_ACCURACY);
        cardColumns.add(DBConstants.Cards.COLUMN_BACK);
        cardColumns.add(DBConstants.Cards.COLUMN_FRONT);
        cardColumns.add(DBConstants.Cards.COLUMN_COURSE_ID);
        cardColumns.add(DBConstants.Cards.COLUMN_DATE_CREATED);
        cardColumns.add(DBConstants.Cards.COLUMN_USER_MADE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            cardColumns.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                cardColumns.isEmpty());
        db.close();
    }



    public void testInsertCard() {
        //TODO this TestDB2 should not use getCourse()
        deleteTheDatabase();
        DBHelper db = DBHelper.getInstance(mContext);
        db.insertDefaultSubjects(db.getWritableDatabase());
        FlashCard original = new FlashCard(mContext, "Computer Science", 457, "Front", "Back", DBConstants.NOT_USER_MADE);
        assertTrue("The location row was not inserted properly", original.id == 1);
        Course course = db.getCourse("Computer Science", 457);
        assertTrue("Course id not correct", course.id == 1);
        assertTrue("Not the right number of cards", course.getCards().size() == 1);
        FlashCard card = course.getCards().get(0);

        assertTrue("Card has wrong front", card.getFront().equals("Front"));
        assertTrue("Card has wrong back", card.getBack().equals("Back"));
        assertTrue("Card has wrong subject", card.getSubject().equals("Computer Science"));
        assertTrue("Card has wrong course number", card.getCourseNum() == 457);
        assertTrue("Card has wrong usermade value", card.getUserMade() == DBConstants.NOT_USER_MADE);
        assertTrue("Card has wrong id", card.id == original.id);

    }

    public void testRemoveCard(){
        deleteTheDatabase();
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase writable = dbHelper.getWritableDatabase();

        dbHelper.insertDefaultSubjects(writable);
        dbHelper.insertDefaultCards(writable);

        Course course = dbHelper.getCourse("Computer Science", 457);
        writable.close();
        course.removeCard(course.getCards().get(0));

        assertTrue("Did not remove card", course.getCards().isEmpty());

        Cursor cursor = dbHelper.getReadableDatabase().query(DBConstants.Cards.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Card not deleted from database:", cursor.getCount() == 0);

    }


}