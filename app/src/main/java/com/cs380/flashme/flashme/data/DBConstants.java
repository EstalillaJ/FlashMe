package com.cs380.flashme.flashme.data;

/**
 * Created by josh on 4/5/16.
 * This class contains the string constants you should use when interacting with the DB.
 */
public class DBConstants {

    public static final int NO_USER = -1;
    public static final int USER_ID = 1;


    public static final class Cards {
        // Used for posting ratings of cards to server
        public static final String OLD_RATING = "oldRating";
        public static final int NO_RATING = -1;
        public static final int NO_ONLINE_ID = -1;
        public static final String TABLE_NAME = "Cards";
        public static final String ID = "id";
        public static final String COLUMN_FRONT = "front";
        public static final String COLUMN_BACK = "back";
        public static final String COLUMN_DATE_CREATED = "date_created";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String ONLINE_ID = "online_id";
        public static final String COLUMN_ACCURACY = "accuracy";
        public static final String COLUMN_NUMBER_OF_ATTEMPTS = "attempts";
        public static final String COLUMN_LOCAL_RATING = "localRating";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_NUM_RATINGS = "numRatings";
    }

    public static final class Courses {
        public static final String TABLE_NAME = "Courses";
        public static final String ID = "id";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_COURSE_NUM = "course_num";
        public static final String COLUMN_ACCURACY = "accuracy";

    }


    public static final class Tutorial{
        public static final String TABLE_NAME = "tutorial";
        public static final String COLUMN_VIEWED = "viewed";
        public static final int NOT_VIEWED = 0;
        public static final int VIEWED = 1;
    }
}
