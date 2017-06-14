package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.TodoListApplication;
import todolist.mad.vfh.kfrank.de.todolist.model.Contact;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.operations.IContactAccessOperations;
import todolist.mad.vfh.kfrank.de.todolist.util.ContactListAdapter;
import todolist.mad.vfh.kfrank.de.todolist.util.EmptyAsyncTask;

public class TodoDetailActivity extends AppCompatActivity {

    // RESPONSE CODES
    public static final int SAVE_ITEM_CODE = 1;
    public static final int DELETE_ITEM_CODE = 2;
    public static final int NO_OP_CODE = 3;

    // REQUEST CODES
    public static final int PICK_CONTACT_CODE = 4;

    // VIEWS
    private EditText nameView;
    private EditText descriptionView;
    private EditText dueDateView;
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
        dueDateView = (EditText) findViewById(R.id.dueDate);
        doneView = (Switch) findViewById(R.id.done);
        favouriteView = (Switch) findViewById(R.id.favourite);
        ListView contactListView = (ListView) findViewById(R.id.contactList);

        // fill views with item data
        item = (TodoItem) getIntent().getExtras().get("item");
        nameView.setText(item.getName());
        descriptionView.setText(item.getDescription());
        dueDateView.setText(item.getDueDate() == null ? null : TodoItem.dueDateFormat.format(item.getDueDate()));
        doneView.setChecked(item.isDone());
        favouriteView.setChecked(item.isFavourite());
        contactListAdapter = new ContactListAdapter(this);
        contactListAdapter.addAll(item.getContacts());
        contactListView.setAdapter(contactListAdapter);

        // set listener
        dueDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date dueDate = TodoItem.dueDateFormat.parse(dueDateView.getText().toString());
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(dueDate);
                    showDatePickerDialog(calendar);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDatePickerDialog(final Calendar calendar) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                showTimePickerDialog(calendar);
            }
        };
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, dayOfMonth);
        dialog.show();
    }

    private void showTimePickerDialog(final Calendar calendar) {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                dueDateView.setText(TodoItem.dueDateFormat.format(calendar.getTime()));
            }
        };
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(this, listener, hourOfDay, minute, true);
        dialog.show();
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
        } else if (item.equals(saveTodoItem)) {
            returnToOverview(SAVE_ITEM_CODE);
            return true;
        } else if (item.equals(deleteTodoItem)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Todo löschen");
            builder.setMessage("Soll das Todo wirklich gelöscht werden?");
            builder.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    returnToOverview(DELETE_ITEM_CODE);
                }
            });
            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
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
            // TODO send SMS
        } else if (item.getItemId() == R.id.sendMail) {
            // TODO send mail
        } else if (item.getItemId() == R.id.removeContact) {
            contactListAdapter.remove(currentSelectedContact);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (item.equals(getTodoItemFromGUI())) {
            returnToOverview(NO_OP_CODE);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Todo geändert");
            builder.setMessage("Das Todo wurde geändert. Wie soll mit den Änderungen verfahren werden?");
            builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    returnToOverview(SAVE_ITEM_CODE);
                }
            });
            builder.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Verwerfen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    returnToOverview(NO_OP_CODE);
                }
            });
            builder.create().show();
        }
    }

    private void callContactPickerIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_CODE && resultCode == Activity.RESULT_OK) {
            final Uri contactUri = data.getData();
            new EmptyAsyncTask() {
                @Override
                protected Object doInBackground(Object... params) {
                    return contactAccessOperations.getContactToUri(contactUri);
                }

                @Override
                protected void onPostExecute(Object result) {
                    Contact contact = (Contact) result;
                    if (contactListAdapter.getPosition(contact) < 0) {
                        contactListAdapter.add(contact);
                    }
                }
            };
        }
    }

    private void returnToOverview(int responseCode) {
        Intent intent = new Intent();
        if (Arrays.asList(SAVE_ITEM_CODE, DELETE_ITEM_CODE).contains(responseCode)) {
            item.adoptData(getTodoItemFromGUI());
            intent.putExtra("item", item);
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
        try {
            String dueDateString = dueDateView.getText().toString();
            item.setDueDate((dueDateString == null | dueDateString.trim().isEmpty()) ? null : TodoItem.dueDateFormat.parse(dueDateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        item.setDone(doneView.isChecked());
        item.setFavourite(favouriteView.isChecked());
        for (int i = 0; i < contactListAdapter.getCount(); i++) {
            item.addContact(contactListAdapter.getItem(i));
        }
        return item;
    }
}
