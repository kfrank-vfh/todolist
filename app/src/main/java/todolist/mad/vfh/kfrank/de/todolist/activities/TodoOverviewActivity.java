package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.Comparator;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.TodoListApplication;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.util.TodoListAdapter;

public class TodoOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_overview_activity);

        TodoListApplication application = (TodoListApplication) getApplication();
        TodoListAdapter adapter = new TodoListAdapter(this, application.getTodoItemCrudOperations());
        adapter.setComparator(getComparator());

        // setze den Adapter auf die Listenansicht
        ListView listView = (ListView) findViewById(R.id.todoList);
        listView.setAdapter(adapter);
    }

    private Comparator<TodoItem> getComparator() {
        return new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                if (o1.isDone() == o2.isDone()) {
                    if (o1.isFavourite() == o2.isFavourite()) {
                        o1.getDueDate().compareTo(o2.getDueDate());
                    }
                    return o1.isFavourite() ? -1 : 1;
                }
                return o1.isDone() ? 1 : -1;
            }
        };
    }
}
