package com.harazim.android.ap;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

class Friend{

    String name;
    boolean selected = false;

    public Friend(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}

public class FriendAdapter extends ArrayAdapter<Friend>{

    private List<Friend> friendList;
    private Context context;
    private int mSize=0;





    public FriendAdapter(List<Friend> friendList, Context context)
    {
        super(context,R.layout.single_listview_item,friendList);
        this.friendList = friendList;
        this.context = context;
        mSize = this.friendList.size();
    }

    private static class FriendHolder
    {
        public TextView friendName;
        public CheckBox chkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       // View v = convertView;
        FriendHolder holder = new FriendHolder();
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.single_listview_item, null);

            holder.friendName = (TextView) convertView.findViewById(R.id.name);
            holder.chkBox = (CheckBox) convertView.findViewById(R.id.FriendsCheck);

            holder.chkBox.setOnCheckedChangeListener((MainActivity) context);

            //Here was the problem, need to set the tag because if not then when gettag is called
            //In the else statement, it is just null.
            convertView.setTag(holder);
        }

        else
        {
            holder = (FriendHolder) convertView.getTag();
        }

        Friend f = friendList.get(position);
        holder.friendName.setText(f.getName());
        holder.chkBox.setChecked(f.isSelected());
        holder.chkBox.setTag(f);

        return convertView;
    }

    public int getSize(){return mSize;}
}
