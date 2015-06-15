package com.harazim.android.ap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 6/6/2015.
 */

class GroupImage {
    byte[] mEncodedImageArray;

    public GroupImage(byte[] image) {
        mEncodedImageArray = image;
    }

    public byte[] GetEncodedImageArray() {
        return mEncodedImageArray;
    }

    public void SetEncodedImageArray(byte[] image) {
        mEncodedImageArray = image;
    }
}

public class ImageAdapter extends ArrayAdapter<GroupImage> {
    private List<GroupImage> mImageList;
    private Context mContext;
    private int mSize = 0;
    private LayoutInflater mInflater;

    public ImageAdapter(List<GroupImage> imageList, Context context) {
        super(context, R.layout.grid_item_layout,imageList);
        mInflater = LayoutInflater.from(context);
        mImageList = imageList;
        mContext = context;
        mSize = mImageList.size();
    }

    private static class ImageHolder
    {
        public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageHolder holder = new ImageHolder();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item_layout, null);

            holder.image = (ImageView) convertView.findViewById(R.id.image);


            holder.image.setScaleType(
                    ImageView.ScaleType.CENTER_CROP);
            holder.image.setPadding(4, 4, 4, 4);


            convertView.setTag(holder);
        }

        else
        {
            holder = (ImageHolder) convertView.getTag();
        }

        GroupImage i = mImageList.get(position);
        byte[] array = mImageList.get(position).mEncodedImageArray;
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        File f = new File(mContext.getCacheDir(),"temppic");
        try {
            f.createNewFile();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(array);
            fos.flush();
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //holder.image.setImageBitmap(bitmap);
        Picasso.with(mContext).load(f).into(holder.image);

        return convertView;
    }

}
