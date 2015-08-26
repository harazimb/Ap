package com.harazim.android.ap;

import com.harazim.android.ap.GrouptureTaskBase;
import com.harazim.android.ap.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evan on 8/26/2015.
 */
public class AddImageTask extends GrouptureTaskBase<Boolean> {

    //Constants
    private final String url = "http://cse.msu.edu/~moraneva/upload.php";

    //Members
    String mImage, mPath;
    JSONParser jsonParser;

    //Constructor
    AddImageTask(String image, String path) {
        mImage = image;
        mPath = path;

        jsonParser = new JSONParser();
    }


    @Override
    protected Boolean doInBackground(Void... args) {
        //Create username/password param.
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", mImage));
        params.add(new BasicNameValuePair("imagePath", mPath));
        JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

        try {
            int success = json.getInt("success");

            if (success == 1) {

                return true;
            } else {
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean returnVal) {

    }

    @Override
    protected void onCancelled() {

    }
}
