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
 * Created by evan on 4/9/2015.
 */
public class LoginActivity extends Activity {

    JSONParser jsonParser = new JSONParser();

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private UserLoginTask mAuthTask = null;
    private EditText mPasswordView;
    private EditText mUsernameView;
    private Button mRegisterButton;
    private View mLoginFormView;

    private static String url = "http://cse.msu.edu/~harazimb/login_app.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mPasswordView = (EditText) findViewById(R.id.password_login);
        mUsernameView = (EditText) findViewById(R.id.username_login);

        mRegisterButton= (Button) findViewById(R.id.register_button_login);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);



    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegister()
    {
        //Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        //If there is already a authorization task going on, don't do anything.
        if(mAuthTask!=null)
        {
            return;
        }

        else{
            // Execute registration.
            mAuthTask = new UserLoginTask(username,password);
            mAuthTask.execute((Void) null);
        }

    }

    /**
     *
     */
    public class UserLoginTask extends AsyncTask<Void,Void,Boolean> {

        private final String mUsername;
        private final String mPassword;
        UserLoginTask(String username, String password)
        {
            mUsername=username;
            mPassword=password;
        }

        @Override
        protected Boolean doInBackground(Void... args)
        {
            //Create username/password param.
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username",mUsername));
            params.add(new BasicNameValuePair("password",mPassword));

            // Getting json Object.
            JSONObject json = jsonParser.makeHttpRequest(url,"POST",params);

            try{
                int success=json.getInt(TAG_SUCCESS);

                if(success==1){

                    return true;
                } else{
                    return false;
                }

            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask=null;

            if(success)
            {
                SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username",mUsername);
                editor.putString("password",mPassword);
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);

                finish();
            }
            else
            {
                mUsernameView.setError(getString(R.string.error_invalid_username));
                mUsernameView.requestFocus();
            }

        }

        @Override
        protected void onCancelled()
        {
            mAuthTask=null;
        }
    }
}
