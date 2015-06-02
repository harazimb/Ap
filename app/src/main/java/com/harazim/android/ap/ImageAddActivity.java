package com.harazim.android.ap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ImageAddActivity extends Activity {
    private static final int SELECT_PHOTO = 100;
    private final String url = "http://cse.msu.edu/~moraneva/upload.php";
    private String mImagePath;
    private JSONParser jsonParser;
    private AddImageTask mAddImageTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_add);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    mImagePath = getPath(selectedImage);
                    Bitmap bm = BitmapFactory.decodeFile((mImagePath));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b,Base64.DEFAULT);
                    mAddImageTask = new AddImageTask(encodedImage,mImagePath);
                    mAddImageTask.execute((Void) null);
                }
        }
    }

    /**
     * Async task to create a group and insert into database.
     */
    public class AddImageTask extends AsyncTask<Void,Void,Boolean> {

        String mImage, mPath;
        AddImageTask(String image, String path)
        {
           mImage = image;
            mPath = path;
        }

        @Override
        protected Boolean doInBackground(Void... args){
            //Create username/password param.
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("image",mImage));
            params.add(new BasicNameValuePair("imagePath",mPath));
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
            //Intent i = new Intent(ImageAddActivity.this,GroupListActivity.class);
            //startActivity(i);

            finish();
        }

        protected void onCancelled()
        {
            mAddImageTask=null;
        }
    }

    public String getPath(Uri uri) {

        if( uri == null ) {
            return null;
        }

        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }



}
