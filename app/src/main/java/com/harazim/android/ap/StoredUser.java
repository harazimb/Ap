package com.harazim.android.ap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by evan on 6/11/2015.
 */
public class StoredUser implements IUser {
    private String mUsername;
    private String mPassword;

    public StoredUser(Context context) throws UserNotFoundException{

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mUsername = sharedPref.getString("username", "fuckherrightinthepussy69");
        mPassword = sharedPref.getString("password", "fuckherrightinthepussy69");
        if(mUsername == "fuckherrightinthepussy69")
            throw new UserNotFoundException("User was not found in database, taking to login screen");


    }

    public String GetUsername() {
        return mUsername;
    }

    public String GetPassword() {
        return mPassword;
    }
}
