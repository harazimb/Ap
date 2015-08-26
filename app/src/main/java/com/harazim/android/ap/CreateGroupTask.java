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
public class CreateGroupTask extends GrouptureTaskBase<Boolean> {

    //URL to go to
    private final String url = "http://cse.msu.edu/~moraneva/create_group.php";

    // Member variables
    String mGroupName;
    String mOwnerID;
    JSONParser jsonParser;

    /**
     * Constructor
     *
     * @param groupName Name of group to create
     * @param ownerID   owner of group
     */
    CreateGroupTask(String groupName, String ownerID) {
        mGroupName = groupName;
        mOwnerID = ownerID;

        jsonParser = new JSONParser();
    }

    /**
     * Do the actual work for creating a group. for now calls to php but we can change if needed.
     *
     * @param args arguments
     * @return Success
     */
    @Override
    protected Boolean doInBackground(Void... args) {
        //Create username/password param.
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("groupname", mGroupName));
        params.add(new BasicNameValuePair("ownername", mOwnerID));

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
