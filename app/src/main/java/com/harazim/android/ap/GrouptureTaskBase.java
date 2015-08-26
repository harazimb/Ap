package com.harazim.android.ap;

import android.os.AsyncTask;

/**
 * Created by evan on 8/26/2015.
 */
public abstract class GrouptureTaskBase<T> extends AsyncTask<Void, Void, T> {
    @Override
    protected abstract T doInBackground(Void... args);

    @Override
    protected abstract void onPostExecute(final T returnVal);

    protected abstract void onCancelled();
}
