package todolist.mad.vfh.kfrank.de.todolist.util.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import todolist.mad.vfh.kfrank.de.todolist.model.Contact;

/**
 * Created by Kevin Frank on 13.06.2017.
 */

public class ContactListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public ContactListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != oldCount) {
            View child = getChildAt(0);
            if (child == null) {
                super.onDraw(canvas);
                return;
            }
            int height = child.getHeight() + 1;
            oldCount = getCount();
            params = getLayoutParams();
            params.height = getCount() * height;
            setLayoutParams(params);
        }
        super.onDraw(canvas);
    }
}
