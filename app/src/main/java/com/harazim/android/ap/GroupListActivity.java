package com.harazim.android.ap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GroupListActivity extends Activity {

    GetGroupsTask mTask;
    ArrayList<Group> groupList;
    GroupAdapter adapter;
    ListView lv;
    String uname;
    Context mContext;
    TextView mTextView;

    private static String url = "http://cse.msu.edu/~moraneva/get_groups.php";

    JSONArrayParser jsonParser = new JSONArrayParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        mTextView = (TextView) findViewById(R.id.noGroupsTextView);
        lv = (ListView) findViewById(R.id.groupListView);
        mContext = this.getApplicationContext();

        try {
            StoredUser user = new StoredUser(mContext);
            uname = user.GetUsername();
            displayGroupList();
        }
        catch(UserNotFoundException ex)
        {
            // User wasn't found in local cache, shouldn't reach here ever but just in case,
            // kick them back to Login and make them re-authenticate
            Intent i = new Intent(GroupListActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

    }

    private void displayGroupList() {
        mTask = new GetGroupsTask();
        mTask.execute((Void) null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_list, menu);
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

    public class GetGroupsTask extends AsyncTask<Void, Void, ArrayList<Group>> {

        @Override
        protected ArrayList<Group> doInBackground(Void... args) {
            ArrayList<Group> list = new ArrayList<>();

            //Create username/password param.
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", uname));

            JSONArray json = jsonParser.makeHttpRequest(url, "POST", params);

            if (json != null) {
                try {
                    for (int a = 0; a < json.length(); a++) {
                        String gName = json.getString(a);
                        Group g = new Group(gName);
                        list.add(g);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        //I made this work
        @Override
        protected void onPostExecute(final ArrayList<Group> list) {
            groupList = list;
            if (groupList.size() != 0) {
                adapter = new GroupAdapter(groupList, mContext);
                lv.setAdapter(adapter);
                View v;
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                        View temp = null;
                        v = lv.getAdapter().getView(position, temp, null);
                        final TextView textView = (TextView) v.findViewById(R.id.groupName);
                        Intent i = new Intent(GroupListActivity.this, EditGroupActivity.class);
                        i.putExtra("group_name", textView.getText().toString());
                        startActivity(i);
                    }
                });

            } else {
                mTextView.setText("You have no groups");
            }
        }

        protected void onCancelled() {
            mTask = null;
        }
    }

    public void CreateGroup(View view) {
        Intent i = new Intent(mContext, CreateGroupActivity.class);
        startActivity(i);
    }

    public void GroupOnClick(View view) {
        TextView text = (TextView) view.findViewById(R.id.groupName);
        Intent i = new Intent(GroupListActivity.this, EditGroupActivity.class);
        i.putExtra("group_name", text.getText().toString());
        startActivity(i);
    }

    public void MakeFriends(View view) {
        Intent i = new Intent(getApplicationContext(), AddFriend.class);
        startActivity(i);
    }
}
