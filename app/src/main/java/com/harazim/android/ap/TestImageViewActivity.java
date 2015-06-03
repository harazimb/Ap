package com.harazim.android.ap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TestImageViewActivity extends Activity {

    String mGroupName,mOwnerName,mImagePath;
    GetImageTask mTask;
    ImageView imageView;
    JSONParser jsonParser = new JSONParser();

    private final String url = "http://cse.msu.edu/~moraneva/downloadImage.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_view);
        Bundle extras = getIntent().getExtras();
        mGroupName = extras.getString("groupName");
        mOwnerName = extras.getString("ownerName");
        mImagePath = extras.getString("imagePath");
        imageView = (ImageView) findViewById(R.id.imageView);
        mTask = new GetImageTask();
        mTask.execute((Void) null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_image_view, menu);
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

    public class GetImageTask extends AsyncTask<Void, Void, byte[]> {

        @Override
        protected byte[] doInBackground(Void... args) {

            byte[] encoded = new byte[]{};
            //Create username/password param.
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("groupName", mGroupName));
            params.add(new BasicNameValuePair("ownerName",mOwnerName));
            params.add(new BasicNameValuePair("imagePath",mImagePath));
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            if (json != null) {
                try {
                    String aray= json.get("image").toString();
                    encoded = Base64.decode(aray,Base64.DEFAULT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return encoded;
        }

        //I made this work
        @Override
        protected void onPostExecute(byte[] encoded)
        {
            Bitmap bmp= BitmapFactory.decodeByteArray(encoded,0,encoded.length);
            imageView.setImageBitmap(bmp);
        }

        protected void onCancelled() {
            mTask = null;
        }
    }
}
