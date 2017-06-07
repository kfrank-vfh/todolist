package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Comparator;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.TodoListApplication;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.util.TodoListAdapter;

public class TodoOverviewActivity extends AppCompatActivity {

    private static final int NEW_ITEM_CODE = 1;

    private static final int UPDATE_ITEM_CODE = 2;

    private TodoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_overview_activity);

        TodoListApplication application = (TodoListApplication) getApplication();
        adapter = new TodoListAdapter(this, application.getTodoItemCrudOperations());
        adapter.setComparator(getComparator());

        // setze den Adapter auf die Listenansicht
        ListView listView = (ListView) findViewById(R.id.todoList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = adapter.getItem(position);
                showDetailViewToItem(item, UPDATE_ITEM_CODE);
            }
        });

        // setze click listener für button
        Button button = (Button) findViewById(R.id.addTodo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoItem item = new TodoItem();
                showDetailViewToItem(item, NEW_ITEM_CODE);
            }
        });
    }

    private void showDetailViewToItem(TodoItem item, int requestCode) {
        Intent intent = new Intent(this, TodoDetailActivity.class);
        intent.putExtra("item", item);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TodoDetailActivity.SAVE_ITEM_CODE) {
            TodoItem item = (TodoItem) data.getExtras().get("item");
            if (requestCode == NEW_ITEM_CODE) {
                adapter.add(item);
            } else if (requestCode == UPDATE_ITEM_CODE) {
                adapter.itemChanged(item);
            }
        }
    }

    private Comparator<TodoItem> getComparator() {
        return new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                if (o1.isDone() == o2.isDone()) {
                    if (o1.isFavourite() == o2.isFavourite()) {
                        return (o1.getDueDate() == null || o2.getDueDate() == null) ? 0 : o1.getDueDate().compareTo(o2.getDueDate());
                    }
                    return o1.isFavourite() ? -1 : 1;
                }
                return o1.isDone() ? 1 : -1;
            }
        };
    }
}
