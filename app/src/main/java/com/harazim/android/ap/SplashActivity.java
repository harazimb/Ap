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

        File f = new File(
                "/data/data/com.harazim.android.ap/shared_prefs/"+getString(R.string.preference_file_key)+".xml");
        if(f.exists()&&checkSignedIn())
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
        else
        {
            Context context = SplashActivity.this;
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
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

    public boolean checkSignedIn()
    {
        SharedPreferences sharedPref = SplashActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String defaults= "";
        String user = sharedPref.getString("username",defaults);
        String pass = sharedPref.getString("password",defaults);

        if(user=="" || pass=="")
        {
            return false;
        }
        return true;

    }
}
