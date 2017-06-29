package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Comparator;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.TodoListApplication;
import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.util.comparators.DateComparator;
import todolist.mad.vfh.kfrank.de.todolist.util.comparators.FavouriteComparator;
import todolist.mad.vfh.kfrank.de.todolist.util.adapters.TodoListAdapter;

public class TodoOverviewActivity extends AppCompatActivity {

    // FLAGS
    private boolean backAlreadyPressed = false;

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
                showDetailViewToItem(item, Codes.Request.UPDATE_ITEM_CODE);
            }
        });

        // initiiere den Comparator
        setComparator(FavouriteComparator.getInstance());
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
        favouriteSortingItem.setIcon(FavouriteComparator.getInstance().equals(currentComparator) ? R.drawable.favourite_checked_icon : R.drawable.favourite_icon);
        dateSortingItem.setIcon(DateComparator.getInstance().equals(currentComparator) ? R.drawable.calendar_checked_icon : R.drawable.calendar_icon);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(addTodoItem)) {
            showDetailViewToItem(new TodoItem(), Codes.Request.NEW_ITEM_CODE);
            return true;
        } else if (item.equals(favouriteSortingItem)) {
            setComparator(FavouriteComparator.getInstance());
            return true;
        } else if (item.equals(dateSortingItem)) {
            setComparator(DateComparator.getInstance());
            return true;
        }
        return false;
    }

    private void showDetailViewToItem(TodoItem item, int requestCode) {
        Intent intent = new Intent(this, TodoDetailActivity.class);
        intent.putExtra(Codes.Extra.ITEM, item);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Codes.Response.NO_OP_CODE) {
            return;
        }
        TodoItem item = (TodoItem) data.getExtras().get(Codes.Extra.ITEM);
        if (resultCode == Codes.Response.SAVE_ITEM_CODE) {
            if (requestCode == Codes.Request.NEW_ITEM_CODE) {
                adapter.add(item);
            } else if (requestCode == Codes.Request.UPDATE_ITEM_CODE) {
                adapter.itemChanged(item);
            }
        } else if (resultCode == Codes.Response.DELETE_ITEM_CODE) {
            adapter.remove(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (backAlreadyPressed) {
            finish();
        } else {
            Toast.makeText(this, getString(R.string.todo_overview_on_back), Toast.LENGTH_SHORT).show();
            backAlreadyPressed = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        backAlreadyPressed = false;
    }

    private void setComparator(Comparator<TodoItem> comparator) {
        if (comparator.equals(currentComparator)) {
            return;
        }
        currentComparator = comparator;
        if (favouriteSortingItem != null) {
            favouriteSortingItem.setIcon(FavouriteComparator.getInstance().equals(currentComparator) ? R.drawable.favourite_checked_icon : R.drawable.favourite_icon);
        }
        if (dateSortingItem != null) {
            dateSortingItem.setIcon(DateComparator.getInstance().equals(currentComparator) ? R.drawable.calendar_checked_icon : R.drawable.calendar_icon);
        }
        adapter.setComparator(comparator);
    }
}
