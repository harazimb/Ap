package com.harazim.android.ap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    ListView lv;
    ArrayList<Friend> friendList;
    ArrayList<String> memberList= new ArrayList<String>();
    FriendAdapter frAdapter;
    ArrayList<String> temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lv = (ListView) findViewById(R.id.listview);
        displayFriendList();
        Bundle extra = getIntent().getExtras();
        //This needs check the box of a member when previously checked, I tried some code.
        // I need to simulate a click on the friend for each element in the array list temp
        if(extra != null)
        {
            temp = getIntent().getExtras().getStringArrayList("key");
            for(int i = 0; i<temp.size(); i++)
            {
                for(int j = 0; j<friendList.size(); j++)
                {
                    if(friendList.get(j).getName() == temp.get(i))
                    {
                        //friendList.get(j).setSelected(true);
                    }
                }

            }
        }
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
            if(isChecked == false)
            {
                int count = 0;
                while(count<memberList.size())
                {
                    if(memberList.get(count) == f.getName())
                    {
                        memberList.remove(count);
                    }
                    else
                        ++count;
                }
            }
            else
                memberList.add(f.getName());

            //Toast.makeText(this, "Clicked on Friend: " + f.getName() + ". State: is "
            //       + isChecked, Toast.LENGTH_SHORT).show();
        }
    }

    public void add(View view) {

        if(memberList.size() != 0) {

            Bundle bundel = new Bundle();
            bundel.putStringArrayList("key", memberList);

            Intent intent = new Intent(this, CreateGroupActivity.class);
            intent.putExtras(bundel);
            startActivity(intent);
        }

        else
        {
            Intent i = new Intent(getApplicationContext(), CreateGroupActivity.class);
            startActivity(i);
        }
    }
}