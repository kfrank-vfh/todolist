package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import java.util.Arrays;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.TodoListApplication;
import todolist.mad.vfh.kfrank.de.todolist.model.Contact;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.operations.interfaces.IContactAccessOperations;
import todolist.mad.vfh.kfrank.de.todolist.util.adapters.ContactListAdapter;
import todolist.mad.vfh.kfrank.de.todolist.util.reductions.EmptyAsyncTask;
import todolist.mad.vfh.kfrank.de.todolist.util.widgets.DateTimePicker;

public class TodoDetailActivity extends AppCompatActivity {

    // VIEWS
    private EditText nameView;
    private EditText descriptionView;
    private DateTimePicker dueDateView;
    private Switch doneView;
    private Switch favouriteView;

    // OPTION MENU ITEMS
    private MenuItem addContactItem;
    private MenuItem saveTodoItem;
    private MenuItem deleteTodoItem;

    // CURRENT SELECTED CONTACT FOR CONTEXT MENU
    private Contact currentSelectedContact;

    // CONTACT ACCESS OPERATIONS
    private IContactAccessOperations contactAccessOperations;

    // CONTACT LIST ADAPTER
    private ContactListAdapter contactListAdapter;

    // TODOITEM OF DETAIL VIEW
    private TodoItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);
        contactAccessOperations = ((TodoListApplication) getApplication()).getContactAccessOperations();

        // get all views
        nameView = (EditText) findViewById(R.id.name);
        descriptionView = (EditText) findViewById(R.id.description);
        dueDateView = (DateTimePicker) findViewById(R.id.dueDate);
        dueDateView.setDateTimeFormat(TodoItem.dueDateFormat);
        doneView = (Switch) findViewById(R.id.done);
        favouriteView = (Switch) findViewById(R.id.favourite);
        ListView contactListView = (ListView) findViewById(R.id.contactList);

        // fill views with item data
        item = (TodoItem) getIntent().getExtras().get(Codes.Extra.ITEM);
        nameView.setText(item.getName());
        descriptionView.setText(item.getDescription());
        dueDateView.setDateTime(item.getDueDate());
        doneView.setChecked(item.isDone());
        favouriteView.setChecked(item.isFavourite());
        contactListAdapter = new ContactListAdapter(this);
        contactListAdapter.addAll(item.getContacts());
        contactListView.setAdapter(contactListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // populate options menu
        getMenuInflater().inflate(R.menu.todo_details_options_menu, menu);
        // get all option menu items
        addContactItem = menu.findItem(R.id.addContact);
        saveTodoItem = menu.findItem(R.id.saveTodo);
        deleteTodoItem = menu.findItem(R.id.deleteTodo);
        // set visibility of deleteTodoItem
        deleteTodoItem.setVisible(item.getId() >= 0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(addContactItem)) {
            callContactPickerIntent();
            return true;
        } else if (item.equals(saveTodoItem)) {
            returnToOverview(Codes.Response.SAVE_ITEM_CODE);
            return true;
        } else if (item.equals(deleteTodoItem)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.todo_detail_delete_alert_title));
            builder.setMessage(getString(R.string.todo_detail_delete_alert_message));
            builder.setPositiveButton(getString(R.string.todo_detail_delete_alert_delete_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    returnToOverview(Codes.Response.DELETE_ITEM_CODE);
                }
            });
            builder.setNegativeButton(getString(R.string.todo_detail_delete_alert_cancel_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
            return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // populate context menu
        getMenuInflater().inflate(R.menu.todo_details_context_menu, menu);
        // get all context menu items
        MenuItem sendSmsItem = menu.findItem(R.id.sendSMS);
        MenuItem sendMailItem = menu.findItem(R.id.sendMail);
        MenuItem removeContactItem = menu.findItem(R.id.removeContact);
        // get contact to view
        Contact contact = contactListAdapter.getContactToView(v);
        currentSelectedContact = contact;
        // set enabled of items depending on contact
        if (contact == null) {
            sendSmsItem.setEnabled(false);
            sendMailItem.setEnabled(false);
            removeContactItem.setEnabled(false);
        } else {
            sendSmsItem.setEnabled(contact.getPhone() != null);
            sendMailItem.setEnabled(contact.getMail() != null);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sendSMS) {
            Uri smsUri = Uri.fromParts(Codes.Uri.SMS_TO, currentSelectedContact.getPhone(), null);
            Intent intent = new Intent(Codes.Action.SEND_TO, smsUri);
            intent.putExtra(Codes.Extra.SMS_BODY, this.item.getName() + "\n" + this.item.getDescription());
            startActivity(intent);
        } else if (item.getItemId() == R.id.sendMail) {
            Uri mailUri = Uri.fromParts(Codes.Uri.MAIL_TO, currentSelectedContact.getMail(), null);
            Intent intent = new Intent(Codes.Action.SEND_TO, mailUri);
            intent.putExtra(Codes.Extra.MAIL_SUBJECT, this.item.getName());
            intent.putExtra(Codes.Extra.MAIL_BODY, this.item.getDescription());
            startActivity(Intent.createChooser(intent, getString(R.string.todo_detail_mail_intent_chooser_title)));
        } else if (item.getItemId() == R.id.removeContact) {
            contactListAdapter.remove(currentSelectedContact);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (item.equals(getTodoItemFromGUI())) {
            returnToOverview(Codes.Response.NO_OP_CODE);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.todo_detail_changed_alert_title));
            builder.setMessage(getString(R.string.todo_detail_changed_alert_message));
            builder.setPositiveButton(getString(R.string.todo_detail_changed_alert_save_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    returnToOverview(Codes.Response.SAVE_ITEM_CODE);
                }
            });
            builder.setNeutralButton(getString(R.string.todo_detail_changed_alert_cancel_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton(getString(R.string.todo_detail_changed_alert_discard_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    returnToOverview(Codes.Response.NO_OP_CODE);
                }
            });
            builder.create().show();
        }
    }

    private void callContactPickerIntent() {
        Intent intent = new Intent(Codes.Action.PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Codes.Request.PICK_CONTACT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Codes.Request.PICK_CONTACT_CODE && resultCode == Activity.RESULT_OK) {
            final Uri contactUri = data.getData();
            new EmptyAsyncTask() {
                @Override
                protected Object doInBackground(Object... params) {
                    return contactAccessOperations.getContactToUri(contactUri);
                }

                @Override
                protected void onPostExecute(Object result) {
                    Contact contact = (Contact) result;
                    if (!contactListAdapter.hasContact(contact)) {
                        contactListAdapter.add(contact);
                    }
                }
            };
        }
    }

    private void returnToOverview(int responseCode) {
        Intent intent = new Intent();
        if (Arrays.asList(Codes.Response.SAVE_ITEM_CODE, Codes.Response.DELETE_ITEM_CODE).contains(responseCode)) {
            item.adoptData(getTodoItemFromGUI());
            intent.putExtra(Codes.Extra.ITEM, item);
        }
        setResult(responseCode, intent);
        finish();
    }

    private TodoItem getTodoItemFromGUI() {
        TodoItem item = new TodoItem();
        String name = nameView.getText().toString();
        item.setName((name == null || name.trim().isEmpty()) ? null : name);
        String description = descriptionView.getText().toString();
        item.setDescription((description == null || description.trim().isEmpty()) ? null : description);
        item.setDueDate(dueDateView.getDateTime());
        item.setDone(doneView.isChecked());
        item.setFavourite(favouriteView.isChecked());
        for (int i = 0; i < contactListAdapter.getCount(); i++) {
            item.addContact(contactListAdapter.getItem(i));
        }
        return item;
    }
}
