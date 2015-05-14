package com.harazim.android.ap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;


public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(checkFirstSignIn())
        {
            SharedPreferences sharedPref = SplashActivity.this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String uname = sharedPref.getString("username","......");
            if(uname.equals("......"))
            {
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run(){

                    Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(i);

                    finish();
                }
            } , SPLASH_DISPLAY_LENGTH);
            }
            else
            {
                new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run(){

                        Intent i = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(i);

                        finish();
                    }
                } , SPLASH_DISPLAY_LENGTH);
            }
        }
        else
        {
            Context context = SplashActivity.this;
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("First",1);
            editor.commit();
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run(){

                    Intent i = new Intent(SplashActivity.this,RegisterActivity.class);
                    startActivity(i);

                    finish();
                }
            } , SPLASH_DISPLAY_LENGTH);
        }
    }

    public boolean checkFirstSignIn()
    {
        SharedPreferences sharedPref = SplashActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        int defaults= 0;
        int first = sharedPref.getInt("First",defaults);

        if(first==0)
        {
            return false;
        }
        return true;



        //Temporary to test.
        //return false;

    }
}
