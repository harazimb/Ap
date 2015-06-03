package com.harazim.android.ap;

import android.app.Activity;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class EditGroupActivity extends Activity {

    private final String url = "http://cse.msu.edu/~moraneva/uploadImage.php";

    String user;
    TextView groupTextView;
    private String mImagePath;
    private AddImageTask mAddImageTask;
    private JSONParser jsonParser  = new JSONParser();
    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPref = EditGroupActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String value = extras.getString("group_name");
        groupTextView = (TextView) findViewById(R.id.groupTextView);
        groupTextView.setText(value);
        user = sharedPref.getString("username","not good");
        if(user.equals("not good"))
        {
            System.exit(0);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_group, menu);
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

    public void AddImage(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);


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

                    //I think the 10 means 10% quality, we'll use this for now.
                    bm.compress(Bitmap.CompressFormat.JPEG,10,baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    mAddImageTask = new AddImageTask(encodedImage,mImagePath);
                    mAddImageTask.execute((Void) null);
                }
        }
    }

    /**
     * Async task to create a group and insert into database.
     */
    public class AddImageTask extends AsyncTask<Void,Void,Boolean> {

        String mPath,mImage;
        AddImageTask(String image,String path)
        {
            mImage = image;
            mPath = path;
        }

        @Override
        protected Boolean doInBackground(Void... args){
            //Create username/password param.
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("groupName",groupTextView.getText().toString()));
            params.add(new BasicNameValuePair("ownerName",user));
            params.add(new BasicNameValuePair("imagePath",mPath));
            params.add(new BasicNameValuePair("image",mImage));
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
            Intent i = new Intent(EditGroupActivity.this, TestImageViewActivity.class);
            i.putExtra("groupName", groupTextView.getText().toString());
            i.putExtra("ownerName",user);
            i.putExtra("imagePath",mPath);
            startActivity(i);

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
