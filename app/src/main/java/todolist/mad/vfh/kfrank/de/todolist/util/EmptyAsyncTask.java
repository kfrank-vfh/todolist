package todolist.mad.vfh.kfrank.de.todolist.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by kfrank on 04.06.2017.
 */

public abstract class EmptyAsyncTask extends AsyncTask<Object, Object, Object> {

    public EmptyAsyncTask() {
        execute();
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Object doInBackground(Object... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
    }
}
