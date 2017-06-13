package todolist.mad.vfh.kfrank.de.todolist.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.util.Comparator;
import java.util.Date;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.model.Contact;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.operations.ITodoItemCrudOperations;

/**
 * Created by Kevin Frank on 03.06.2017.
 */

public class ContactListAdapter extends ArrayAdapter<Contact> {

    public ContactListAdapter(@NonNull Context context) {
        super(context, R.layout.todo_list_item);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // inflate new content view if null
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.contact_list_item, null);
        }
        // find all views
        final ImageView contactPhotoView = (ImageView) convertView.findViewById(R.id.contactPhoto);
        final TextView contactNameView = (TextView) convertView.findViewById(R.id.contactName);

        // get contact
        Contact contact = getItem(position);
        // set content of views
        if (contact.getPhoto() != null) {
            ByteArrayInputStream stream = new ByteArrayInputStream(contact.getPhoto());
            Drawable drawable = Drawable.createFromStream(stream, "contactPhoto");
            contactPhotoView.setImageDrawable(drawable);
        }
        contactNameView.setText(contact.getName());
        // return content view
        return convertView;
    }
}
