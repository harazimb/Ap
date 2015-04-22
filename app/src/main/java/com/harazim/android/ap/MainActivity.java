package com.harazim.android.ap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    ListView lv;
    ArrayList<Friend> friendList;
    ArrayList<Friend> memberList;
    FriendAdapter frAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lv = (ListView) findViewById(R.id.listview);
        displayFriendList();
    }

    private void displayFriendList() {
        friendList = new ArrayList<>();
        friendList.add(new Friend("Evan"));
        friendList.add(new Friend("Peter"));
        friendList.add(new Friend("Sam"));
        friendList.add(new Friend("Karan"));
        friendList.add(new Friend("Connor"));
        friendList.add(new Friend("Bryce"));
        friendList.add(new Friend("Jim"));
        friendList.add(new Friend("Jaime"));
        friendList.add(new Friend("Jayson"));


        frAdapter = new FriendAdapter(friendList, this);
        lv.setAdapter(frAdapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = lv.getPositionForView(buttonView);
        if (pos < frAdapter.getSize()) {
            Friend f = friendList.get(pos);
            f.setSelected(isChecked);


            Toast.makeText(this, "Clicked on Friend: " + f.getName() + ". State: is "
                    + isChecked, Toast.LENGTH_SHORT).show();
        }
    }

    public void add(View view) {
        Intent i = new Intent(getApplicationContext(), group.class);
        startActivity(i);
    }
}