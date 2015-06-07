package com.harazim.android.ap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 6/6/2015.
 */

class GroupImage
{
    byte[] mEncodedImageArray;

    public GroupImage(byte[] image)
    {
        mEncodedImageArray = image;
    }

    public byte[] GetEncodedImageArray()
    {
        return mEncodedImageArray;
    }

    public void SetEncodedImageArray(byte[] image)
    {
        mEncodedImageArray = image;
    }
}

public class ImageAdapter extends ArrayAdapter<GroupImage>
{
    private List<GroupImage> mImageList;
    private Context mContext;
    private int mSize = 0;


    public ImageAdapter(List<GroupImage> imageList, Context context)
    {
        super(context,R.layout.activity_test_image_view,imageList);
        mImageList = imageList;
        mContext = context;
        mSize = mImageList.size();
    }

    private static class ImageHolder
    {
        public ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageHolder holder = new ImageHolder();
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder.imageView = new ImageView(mContext);
            holder.imageView.setLayoutParams(new
                    GridView.LayoutParams(80,80));
            holder.imageView.setScaleType(
                    ImageView.ScaleType.CENTER_CROP);
            holder.imageView.setPadding(4,4,4,4);
            byte[] array = mImageList.get(position).mEncodedImageArray;
            Bitmap bitmap = BitmapFactory.decodeByteArray(array,0,array.length);
            holder.imageView.setImageBitmap(bitmap);
            convertView = holder.imageView;

            convertView.setTag(holder);
        }

        else
        {
            holder = (ImageHolder) convertView.getTag();
        }

        return convertView;
    }

}
