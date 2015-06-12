package com.harazim.android.ap;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends Activity {

    JSONParser jsonParser = new JSONParser();

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mAuthTask = null;
    private EditText mPasswordView;
    private EditText mUsernameView;
    private Button mRegisterButton;
    private View mRegisterFormView;

    private static String url = "http://cse.msu.edu/~moraneva/register_app.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mPasswordView = (EditText) findViewById(R.id.password);
        mUsernameView = (EditText) findViewById(R.id.username);

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegister() {
        //If there is already a authorization task going on, don't do anything.
        if (mAuthTask != null) {
            return;
        }

        //Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Get the values of both text fields
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Checking for valid password
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //check for valid username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            //There was an error so focus on what field caused the error.
            focusView.requestFocus();
        } else {
            // Execute registration.
            mAuthTask = new UserRegisterTask(username, password);
            mAuthTask.execute((Void) null);
        }

    }

    /**
     * Is the input password valid? Was thinking greater than 4 characters??
     *
     * @param password password entered
     * @return true if valid
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Going to implement code to check if username already taken here.
     *
     * @param username username entered.
     * @return true if valid.
     */
    private boolean isUsernameValid(String username) {
        return true;
    }

    /**
     *
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserRegisterTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            //Create username/password param.
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", mUsername));
            params.add(new BasicNameValuePair("password", mPassword));

            // Getting json Object.
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    return true;
                } else {
                    return false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                SharedPreferences sharedPref = RegisterActivity.this.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", mUsername);
                editor.putString("password", mPassword);
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            } else {
                mUsernameView.setError(getString(R.string.error_invalid_username));
                mUsernameView.requestFocus();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void LoginFromRegister(View view) {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}



