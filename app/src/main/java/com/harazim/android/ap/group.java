package com.harazim.android.ap;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class group extends Activity{


    private static String url = "http://cse.msu.edu/~moraneva/create_group.php";

    JSONParser jsonParser = new JSONParser();
    TextView a;
    EditText mGroupNameView;
    CreateGroupTask mGroupTask;
    RadioButton r1;
    RadioButton r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        a = (TextView) findViewById(R.id.Mem);
        r1 = (RadioButton) findViewById(R.id.OpenPrivacy);
        r2 = (RadioButton) findViewById(R.id.ClosedPrivacy);
        mGroupNameView = (EditText) findViewById(R.id.GroupInput);
        //Only prints out the first clicked friend
        Bundle extra =getIntent().getExtras();
        if(extra != null) {
            ArrayList<String> memArray = getIntent().getExtras().getStringArrayList("key");
            String members;
            if(memArray.size()==1)
            {
                members = memArray.get(0);
            }
            else if(memArray.size()==2)
            {
                members = memArray.get(0)+ ", " + memArray.get(1);
            }
            else {
                members = memArray.get(0) + ", " + memArray.get(1) + ", " + memArray.get(2) + "...";
            }


            a.setText(members);
        }
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

    public void hideKeyboard(View view) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void createGroup(View view)
    {
        /**
         * @TODO Add the code to actually add the group to the database.
         * must be done before we go group list to view groups the person is in.
         */
        String groupName = mGroupNameView.getText().toString();
        if(groupName.isEmpty())
        {
            mGroupNameView.setError("Group must have a name");
            mGroupNameView.requestFocus();
        }
        else
        {
            SharedPreferences sharedPref = group.this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String name=sharedPref.getString("username","err");
            if(name.equals("err"))
            {
                mGroupNameView.setError("Error");
            }
            else {
                mGroupTask = new CreateGroupTask(groupName, name);

                mGroupTask.execute((Void) null);
            }
        }

    }

    /**
     * Async task to create a group and insert into database.
     */
    public class CreateGroupTask extends AsyncTask<Void,Void,Boolean> {

        String mGroupName;
        String mOwnerID;
        CreateGroupTask(String groupName,String ownerID)
        {
            mGroupName=groupName;
            mOwnerID=ownerID;
        }

        @Override
        protected Boolean doInBackground(Void... args){
            //Create username/password param.
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("groupname",mGroupName));
            params.add(new BasicNameValuePair("ownername",mOwnerID));

            JSONObject json = jsonParser.makeHttpRequest(url,"POST",params);

            try{
                int success=json.getInt("success");

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
            Intent i = new Intent(group.this,GroupListActivity.class);
            startActivity(i);

            finish();
        }

        protected void onCancelled()
        {
            mGroupTask=null;
        }
    }
}
