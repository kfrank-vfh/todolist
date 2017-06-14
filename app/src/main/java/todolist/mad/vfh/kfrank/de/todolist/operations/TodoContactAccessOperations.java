package todolist.mad.vfh.kfrank.de.todolist.operations;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import todolist.mad.vfh.kfrank.de.todolist.model.Contact;

/**
 * Created by Kevin Frank on 13.06.2017.
 */

public class TodoContactAccessOperations implements IContactAccessOperations {

    private ContentResolver contentResolver;

    public TodoContactAccessOperations(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public Contact getContactToUri(Uri contactUri) {
        Cursor cursor = contentResolver.query(contactUri, null, null, null, null);
        if (!cursor.moveToNext()) {
            // No contact to uri >> return null
            return null;
        }
        Contact contact = new Contact();
        // get id of contact
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        contact.setId(id);
        // get name of contact
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        contact.setName(name);
        cursor.close();
        // get phone number of contact if available
        cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
        if (cursor.moveToNext()) {
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contact.setPhone(phone);
        }
        cursor.close();
        // get mail of contact if available
        cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
        if (cursor.moveToNext()) {
            String mail = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
            contact.setMail(mail);
        }
        cursor.close();
        // get contact photo thumbnail of available
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        cursor = contentResolver.query(photoUri, new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            byte[] photo = cursor.getBlob(0);
            contact.setPhoto(photo);
        }
        cursor.close();
        // return contact
        return contact;
    }

    @Override
    public Contact getContactToId(String contactId) {
        return getContactToUri(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId));
    }
}
