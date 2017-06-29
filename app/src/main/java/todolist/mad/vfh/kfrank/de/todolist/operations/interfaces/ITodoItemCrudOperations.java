package todolist.mad.vfh.kfrank.de.todolist.operations.interfaces;

import java.util.List;

import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;

/**
 * Created by kfrank on 03.06.2017.
 */

public interface ITodoItemCrudOperations {

    public TodoItem createTodoItem(TodoItem item);

    public List<TodoItem> readAllTodoItems();

    public TodoItem readTodoItem(long todoItemId);

    public TodoItem updateTodoItem(TodoItem item);

    public boolean deleteTodoItem(long todoItemId);

    public void finalise();
}
