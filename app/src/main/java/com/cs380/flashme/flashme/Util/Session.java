package com.cs380.flashme.flashme.Util;

import com.cs380.flashme.flashme.data.DBConstants;

/**
 * Created by josh on 5/13/16.
 */
public class Session {
    public static boolean loggedIn = false;
    public static long userId = DBConstants.NO_USER;

    public static void logout(){
        loggedIn = false;
        userId = DBConstants.NO_USER;
    }

    public static void logIn(long id){
        loggedIn = true;
        userId = id;
    }

}
