package com.harazim.android.ap;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;


public class group extends Activity{

    TextView a;
    RadioButton r1;
    RadioButton r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        a = (TextView) findViewById(R.id.Mem);
        r1 = (RadioButton) findViewById(R.id.OpenPrivacy);
        r2 = (RadioButton) findViewById(R.id.ClosedPrivacy);
        //Only prints out the last clicked friend
        ArrayList<String> memArray = getIntent().getExtras().getStringArrayList("key");
        String members = memArray.get(0)+", "+memArray.get(0)+", "+memArray.get(0)+"...";
        a.setText(members);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void radio1(View view)
    {
        if(r2.isChecked())
        {
            r2.setChecked(false);
            r1.setChecked(true);
        }
    }

    public void radio2(View view)
    {
        if(r1.isChecked())
        {
            r1.setChecked(false);
            r2.setChecked(true);
        }
    }

    public void addMore(View view)
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
