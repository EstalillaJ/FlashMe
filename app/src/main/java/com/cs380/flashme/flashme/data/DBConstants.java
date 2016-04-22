package com.cs380.flashme.flashme.data;

/**
 * Created by josh on 4/5/16.
 * This class contains the string constants you should use when interacting with the DB.
 */
public class DBConstants {

    public static final int NOT_USER_MADE = 0;
    public static final int USER_MADE = 1;
    public static final class Cards {
        public static final String TABLE_NAME = "Cards";
        public static final String ID = "id";
        public static final String COLUMN_FRONT = "front";
        public static final String COLUMN_BACK = "back";
        public static final String COLUMN_DATE_CREATED = "date_created";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_USER_MADE = "user_made";
        public static final String COLUMN_ACCURACY = "accuracy";

    }

    public static final class Courses {
        public static final String TABLE_NAME = "Courses";
        public static final String ID = "id";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_COURSE_NUM = "course_num";
        public static final String COLUMN_USER_MADE = "user_made";
        public static final String COLUMN_ACCURACY = "accuracy";

    }
}
