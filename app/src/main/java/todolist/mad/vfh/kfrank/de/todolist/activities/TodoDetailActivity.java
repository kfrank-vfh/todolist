package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;

public class TodoDetailActivity extends AppCompatActivity {

    // RESPONSE CODES
    public static final int SAVE_ITEM_CODE = 1;
    public static final int NO_OP_CODE = 2;

    // VIEWS
    private EditText nameView;
    private EditText descriptionView;
    private EditText dueDateView;
    private Switch doneView;
    private Switch favouriteView;

    private TodoItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);

        // get all views
        nameView = (EditText) findViewById(R.id.name);
        descriptionView = (EditText) findViewById(R.id.description);
        dueDateView = (EditText) findViewById(R.id.dueDate);
        doneView = (Switch) findViewById(R.id.done);
        favouriteView = (Switch) findViewById(R.id.favourite);

        // fill views with item data
        item = (TodoItem) getIntent().getExtras().get("item");
        nameView.setText(item.getName());
        descriptionView.setText(item.getDescription());
        dueDateView.setText(item.getDueDate() == null ? null : TodoItem.dueDateFormat.format(item.getDueDate()));
        doneView.setChecked(item.isDone());
        favouriteView.setChecked(item.isFavourite());
    }

    @Override
    public void onBackPressed() {
        if (item.equals(getTodoItemFromGUI())) {
            super.onBackPressed();
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

    private void returnToOverview(int responseCode) {
        Intent intent = new Intent();
        if (responseCode == SAVE_ITEM_CODE) {
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
        return item;
    }
}
