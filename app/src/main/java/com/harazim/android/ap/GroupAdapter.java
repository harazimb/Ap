package com.harazim.android.ap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by evan on 5/13/2015.
 */


class Group{

    String name;

    // Put a thumbnail picture here?
    public Group(String name) {

        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

public class GroupAdapter extends ArrayAdapter<Group>
{

    private List<Group> mGroupList;
    private Context mContext;
    private int mSize=0;





    public GroupAdapter(List<Group> groupList, Context context)
    {
        super(context,R.layout.single_grouplist_item,groupList);
        mGroupList = groupList;
        mContext = context;
        mSize = mGroupList.size();
    }

    private static class GroupHolder
    {
        public TextView groupName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View v = convertView;
        GroupHolder holder = new GroupHolder();
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.single_grouplist_item, null);

            holder.groupName = (TextView) convertView.findViewById(R.id.groupName);

            //Here was the problem, need to set the tag because if not then when gettag is called
            //In the else statement, it is just null.
            convertView.setTag(holder);
        }

        else
        {
            holder = (GroupHolder) convertView.getTag();
        }

        Group f = mGroupList.get(position);
        holder.groupName.setText(f.getName());
        holder.groupName.setClickable(false);
        return convertView;
    }

    public int getSize(){return mSize;}
}
