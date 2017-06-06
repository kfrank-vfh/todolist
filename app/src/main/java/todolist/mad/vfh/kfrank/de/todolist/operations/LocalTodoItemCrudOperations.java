package todolist.mad.vfh.kfrank.de.todolist.operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;

public class LocalTodoItemCrudOperations implements ITodoItemCrudOperations {

    private static final String TABLE_NAME = "todoitems";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DONE = "done";
    private static final String COLUMN_FAVOURITE = "favourite";
    private static final String COLUMN_DUE_DATE = "dueDate";

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_DONE + " TINYINT, "
            + COLUMN_FAVOURITE + " TINYINT, "
            + COLUMN_DUE_DATE + " BIGINT);";

    private static final String ID_SELECTOR = COLUMN_ID + "=?";

    private SQLiteDatabase dataBase;

    public LocalTodoItemCrudOperations(Context context) {
        context.deleteDatabase("de.kfrank.vfh.mad.todolist"); // TODO Zeile löschen
        this.dataBase = context.openOrCreateDatabase("de.kfrank.vfh.mad.todolist", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        if (this.dataBase.getVersion() == 0) {
            dataBase.setLocale(Locale.getDefault());
            dataBase.setVersion(1);
            dataBase.execSQL(CREATE_TABLE_SQL);
        }
    }

    @Override
    public TodoItem createTodoItem(TodoItem item) {
        long newItemId = this.dataBase.insert(TABLE_NAME, null, todoItem2DataBaseItem(item));
        item.setId(newItemId);
        return item;
    }

    @Override
    public List<TodoItem> readAllTodoItems() {
        List<TodoItem> items = new ArrayList<>();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_NAME);
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_DONE, COLUMN_FAVOURITE, COLUMN_DUE_DATE};
        String order = COLUMN_ID + " ASC";
        Cursor cursor = builder.query(dataBase, columns, null, null, null, null, order);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            items.add(dataBaseItem2TodoItem(cursor));
            cursor.moveToNext();
        }
        return items;
    }

    @Override
    public TodoItem readTodoItem(long todoItemId) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_NAME);
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_DONE, COLUMN_FAVOURITE, COLUMN_DUE_DATE};
        String order = COLUMN_ID + " ASC";
        Cursor cursor = builder.query(dataBase, columns, ID_SELECTOR, new String[] {Long.toString(todoItemId)}, null, null, order);
        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            return null;
        }
        return dataBaseItem2TodoItem(cursor);
    }

    @Override
    public TodoItem updateTodoItem(TodoItem item) {
        this.dataBase.update(TABLE_NAME, todoItem2DataBaseItem(item), ID_SELECTOR, new String[] {Long.toString(item.getId())});
        return item;
    }

    @Override
    public boolean deleteTodoItem(long todoItemId) {
        this.dataBase.delete(TABLE_NAME, ID_SELECTOR, new String[] {Long.toString(todoItemId)});
        return true;
    }

    private ContentValues todoItem2DataBaseItem(TodoItem item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, item.getId());
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_DONE, item.isDone() ? 1 : 0);
        values.put(COLUMN_FAVOURITE, item.isFavourite() ? 1 : 0);
        values.put(COLUMN_DUE_DATE, item.getDueDate().getTime());
        return values;
    }

    private TodoItem dataBaseItem2TodoItem(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
        short done = cursor.getShort(cursor.getColumnIndex(COLUMN_DONE));
        short favourite = cursor.getShort(cursor.getColumnIndex(COLUMN_FAVOURITE));
        long dueDate = cursor.getLong(cursor.getColumnIndex(COLUMN_DUE_DATE));
        return new TodoItem(id, name, description, done == 1, favourite == 1, new Date(dueDate));
    }

    public void closeDataBase() {
        // TODO irgendwo muss das noch aufgerufen werden
        this.dataBase.close();
    }
}
