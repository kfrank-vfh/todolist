package todolist.mad.vfh.kfrank.de.todolist.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by kfrank on 04.06.2017.
 */

public abstract class AsyncProgressDialogTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private ProgressDialog dialog;

    private Context context;

    private String title;

    private String message;

    public AsyncProgressDialogTask(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
        execute();
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, title, message);
    }

    @Override
    protected void onPostExecute(Result result) {
        dialog.cancel();
    }
}
