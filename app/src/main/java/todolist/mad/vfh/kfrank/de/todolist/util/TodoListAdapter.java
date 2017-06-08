package todolist.mad.vfh.kfrank.de.todolist.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Comparator;
import java.util.Date;

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
                if (comparator != null) {
                    sort(comparator);
                }
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
        notifyDataSetChanged();
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // inflate new content view if null
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.todo_list_item, null);
        }
        // find all views
        final TextView nameView = (TextView) convertView.findViewById(R.id.name);
        final TextView dueDateView = (TextView) convertView.findViewById(R.id.dueDate);
        final CheckBox doneBox = (CheckBox) convertView.findViewById(R.id.done);
        final ImageView favouriteView = (ImageView) convertView.findViewById(R.id.favourite);
        // get todoitem
        TodoItem item = getItem(position);
        // set content of views
        nameView.setText(item.getName());
        dueDateView.setText(item.getDueDate() == null ? null : "Fällig am " + TodoItem.dueDateFormat.format(item.getDueDate()));
        doneBox.setChecked(item.isDone());
        favouriteView.setColorFilter(ContextCompat.getColor(getContext(), item.isFavourite() ? R.color.star_yellow : R.color.star_grey));
        // set background color for over due todos
        int color = (item.isDone() || item.getDueDate() == null || new Date().compareTo(item.getDueDate()) < 0) ? R.color.transparent : R.color.transparent_red;
        convertView.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        // add listener
        if (!favouriteView.hasOnClickListeners()) {
            favouriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TodoItem item = getItem(position);
                    item.setFavourite(!item.isFavourite());
                    favouriteView.setColorFilter(ContextCompat.getColor(getContext(), item.isFavourite() ? R.color.star_yellow : R.color.star_grey));
                    itemChanged(item);
                }
            });
        }
        if (!doneBox.hasOnClickListeners()) {
            doneBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TodoItem item = getItem(position);
                    item.setDone(!item.isDone());
                    doneBox.setChecked(item.isDone());
                    itemChanged(item);
                }
            });
        }
        // return content view
        return convertView;
    }
}
