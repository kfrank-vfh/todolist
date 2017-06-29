package todolist.mad.vfh.kfrank.de.todolist.util.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.model.Contact;

/**
 * Created by Kevin Frank on 03.06.2017.
 */

public class ContactListAdapter extends ArrayAdapter<Contact> {

    private Map<View, Contact> contactsToViews = new HashMap<>();

    public ContactListAdapter(@NonNull Context context) {
        super(context, R.layout.todo_list_item);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get contact
        Contact contact = getItem(position);
        // inflate new content view if null
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.contact_list_item, null);
            contactsToViews.put(convertView, contact);
            ((Activity) getContext()).registerForContextMenu(convertView);
        }
        // find all views
        final ImageView contactPhotoView = (ImageView) convertView.findViewById(R.id.contactPhoto);
        final TextView contactNameView = (TextView) convertView.findViewById(R.id.contactName);

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

    public Contact getContactToView(View view) {
        return contactsToViews.get(view);
    }
}
