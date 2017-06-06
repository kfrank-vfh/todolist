package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Comparator;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.TodoListApplication;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.operations.ITodoItemCrudOperations;

public class TodoOverviewActivity extends AppCompatActivity {

    private ITodoItemCrudOperations iTodoItemCrudOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_overview_activity);

        TodoListApplication application = (TodoListApplication) getApplication();
        iTodoItemCrudOperations = application.getTodoItemCrudOperations();
        ArrayAdapter<TodoItem> adapter = new ArrayAdapter<TodoItem>(this, R.layout.todo_list_item, iTodoItemCrudOperations.readAllTodoItems()) {

            private boolean sorting = false;

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.todo_list_item, null);
                }
                TextView nameView = (TextView) convertView.findViewById(R.id.name);
                TextView dueDateView = (TextView) convertView.findViewById(R.id.dueDate);
                CheckBox doneBox = (CheckBox) convertView.findViewById(R.id.done);
                CheckBox favouriteBox = (CheckBox) convertView.findViewById(R.id.favourite);
                TodoItem item = getItem(position);
                nameView.setText(item.getName());
                dueDateView.setText(item.getDueDate().toString());
                doneBox.setChecked(item.isDone());
                favouriteBox.setChecked(item.isFavourite());
                return convertView;
            }

            @Override
            public void notifyDataSetChanged() {
                if (!sorting) {
                    sorting = true;
                    sort(getComparator());
                    sorting = false;
                }
                super.notifyDataSetChanged();
            }
        };

        // setze den Adapter auf die Listenansicht
        ListView listView = (ListView) findViewById(R.id.todoList);
        adapter.sort(getComparator());
        listView.setAdapter(adapter);
    }

    private Comparator<TodoItem> getComparator() {
        return new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                if (o1.isDone() == o2.isDone()) {
                    if(o1.isFavourite() == o2.isFavourite()) {
                        o1.getDueDate().compareTo(o2.getDueDate());
                    }
                    return o1.isFavourite() ? -1 : 1;
                }
                return o1.isDone() ? 1 : -1;
            }
        };
    }
}
