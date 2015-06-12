package com.harazim.android.ap;

/**
 * Created by evan on 6/11/2015.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {

    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
