package todolist.mad.vfh.kfrank.de.todolist.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Comparator;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.operations.ITodoItemCrudOperations;

/**
 * Created by Kevin Frank on 06.06.2017.
 */

public class TodoListAdapter extends ArrayAdapter<TodoItem> {

    private ITodoItemCrudOperations crud;

    private Comparator<TodoItem> comparator;

    public TodoListAdapter(@NonNull Context context, ITodoItemCrudOperations crud) {
        super(context, R.layout.todo_list_item);
        this.crud = crud;
        new EmptyAsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                addAll(TodoListAdapter.this.crud.readAllTodoItems());
                return null;
            }
        };
    }

    public void setComparator(Comparator<TodoItem> comparator) {
        this.comparator = comparator;
        sort(comparator);
    }

    @Override
    public void add(final TodoItem item) {
        super.add(item);
        if (comparator != null) {
            sort(comparator);
        }
        new EmptyAsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                crud.createTodoItem(item);
                return null;
            }
        };
    }

    @Override
    public void remove(final TodoItem item) {
        super.remove(item);
        new EmptyAsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                crud.deleteTodoItem(item.getId());
                return null;
            }
        };
    }

    public void itemChanged(final TodoItem item) {
        if (comparator != null) {
            sort(comparator);
        }
        new EmptyAsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                crud.updateTodoItem(item);
                return null;
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.todo_list_item, null);
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
}
