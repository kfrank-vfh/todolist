package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Comparator;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.TodoListApplication;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.util.TodoListAdapter;

public class TodoOverviewActivity extends AppCompatActivity {

    // REQUEST CODES
    private static final int NEW_ITEM_CODE = 1;
    private static final int UPDATE_ITEM_CODE = 2;

    // COMPARATORS
    private static final Comparator<TodoItem> FAVOURITE_COMPARATOR = getFavouriteComparator();
    private static final Comparator<TodoItem> DATE_COMPARATOR = getDateComparator();

    // OPTION MENU ITEMS
    private MenuItem addTodoItem;
    private MenuItem favouriteSortingItem;
    private MenuItem dateSortingItem;

    // OTHER VARIABLES
    private TodoListAdapter adapter;
    private Comparator<TodoItem> currentComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_overview_activity);

        // create TodoItem Adapter
        TodoListApplication application = (TodoListApplication) getApplication();
        adapter = new TodoListAdapter(this, application.getTodoItemCrudOperations());

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

        // initiiere den Comparator
        setComparator(FAVOURITE_COMPARATOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // populate options menu
        getMenuInflater().inflate(R.menu.todo_overview_options_menu, menu);
        // get all option menu items
        addTodoItem = menu.findItem(R.id.addTodo);
        favouriteSortingItem = menu.findItem(R.id.favouriteSorting);
        dateSortingItem = menu.findItem(R.id.dateSorting);
        // set correct icons
        favouriteSortingItem.setIcon(FAVOURITE_COMPARATOR.equals(currentComparator) ? R.drawable.ic_grade_black_24dp_checked : R.drawable.ic_grade_black_24dp);
        dateSortingItem.setIcon(DATE_COMPARATOR.equals(currentComparator) ? R.drawable.ic_date_range_black_24dp_checked : R.drawable.ic_date_range_black_24dp);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(addTodoItem)) {
            showDetailViewToItem(new TodoItem(), NEW_ITEM_CODE);
            return true;
        } else if (item.equals(favouriteSortingItem)) {
            setComparator(FAVOURITE_COMPARATOR);
            return true;
        } else if (item.equals(dateSortingItem)) {
            setComparator(DATE_COMPARATOR);
            return true;
        }
        return false;
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

    private void setComparator(Comparator<TodoItem> comparator) {
        if (comparator.equals(currentComparator)) {
            return;
        }
        currentComparator = comparator;
        if (favouriteSortingItem != null) {
            favouriteSortingItem.setIcon(FAVOURITE_COMPARATOR.equals(currentComparator) ? R.drawable.ic_grade_black_24dp_checked : R.drawable.ic_grade_black_24dp);
        }
        if (dateSortingItem != null) {
            dateSortingItem.setIcon(DATE_COMPARATOR.equals(currentComparator) ? R.drawable.ic_date_range_black_24dp_checked : R.drawable.ic_date_range_black_24dp);
        }
        adapter.setComparator(comparator);
    }

    private static Comparator<TodoItem> getFavouriteComparator() {
        return new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                if (o1.isDone() == o2.isDone()) {
                    if (o1.isFavourite() == o2.isFavourite()) {
                        return (o1.getDueDate() == null || o2.getDueDate() == null) ? 0 : (int) (o1.getDueDate().getTime() - o2.getDueDate().getTime());
                    }
                    return o1.isFavourite() ? -1 : 1;
                }
                return o1.isDone() ? 1 : -1;
            }
        };
    }

    private static Comparator<TodoItem> getDateComparator() {
        return new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                if (o1.isDone() == o2.isDone()) {
                    if (o1.getDueDate() == null || o2.getDueDate() == null || o1.getDueDate().getTime() - o2.getDueDate().getTime() == 0) {
                        return o1.isFavourite() ? -1 : 1;
                    }
                    return (int) (o1.getDueDate().getTime() - o2.getDueDate().getTime());
                }
                return o1.isDone() ? 1 : -1;
            }
        };
    }
}
