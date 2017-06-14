package todolist.mad.vfh.kfrank.de.todolist.operations;

import android.net.Uri;

import todolist.mad.vfh.kfrank.de.todolist.model.Contact;

/**
 * Created by Kevin Frank on 13.06.2017.
 */

public interface IContactAccessOperations {

    public Contact getContactToUri(Uri contactUri);

    public Contact getContactToId(String contactId);
}
