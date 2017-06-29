package todolist.mad.vfh.kfrank.de.todolist.operations.impls;

import java.util.List;

import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.operations.interfaces.ITodoItemCrudOperations;

/**
 * Created by kfrank on 04.06.2017.
 */

public class DelegateTodoItemCrudOperationsImpl implements ITodoItemCrudOperations {

    private RemoteTodoItemCrudOperations remoteTodoItemCrudOperations;

    private LocalTodoItemCrudOperations localTodoItemCrudOperations;

    public DelegateTodoItemCrudOperationsImpl(RemoteTodoItemCrudOperations remoteTodoItemCrudOperations, LocalTodoItemCrudOperations localTodoItemCrudOperations) {
        this.remoteTodoItemCrudOperations = remoteTodoItemCrudOperations;
        this.localTodoItemCrudOperations = localTodoItemCrudOperations;
        // TodoItems von Datenbank und Webserver abgleichen
        if (remoteTodoItemCrudOperations == null) {
            return;
        }
        List<TodoItem> dbItems = localTodoItemCrudOperations.readAllTodoItems();
        List<TodoItem> webItems = remoteTodoItemCrudOperations.readAllTodoItems();
        if (!dbItems.isEmpty()) {
            // "liegen lokale Todos vor, dann werden alle Todos auf Seiten der Web Applikation gelöscht und die lokalen Todos an die Web Applikation übertragen."
            for (TodoItem item : webItems) {
                remoteTodoItemCrudOperations.deleteTodoItem(item.getId());
            }
            for (TodoItem item : dbItems) {
                remoteTodoItemCrudOperations.createTodoItem(item);
            }
        } else {
            // "liegen keine lokalen Todos vor, dann werden alle Todos von der Web Applikation auf die lokale Datenbank übertragen."
            for (TodoItem item : webItems) {
                localTodoItemCrudOperations.createTodoItem(item);
            }
        }
    }

    public RemoteTodoItemCrudOperations getRemoteTodoItemCrudOperations() {
        return remoteTodoItemCrudOperations;
    }

    public LocalTodoItemCrudOperations getLocalTodoItemCrudOperations() {
        return localTodoItemCrudOperations;
    }

    @Override
    public TodoItem createTodoItem(TodoItem item) {
        item = localTodoItemCrudOperations.createTodoItem(item);
        if (item != null && remoteTodoItemCrudOperations != null) {
            remoteTodoItemCrudOperations.createTodoItem(item);
        }
        return item;
    }

    @Override
    public List<TodoItem> readAllTodoItems() {
        List<TodoItem> result = localTodoItemCrudOperations.readAllTodoItems();
        if (result.isEmpty() && remoteTodoItemCrudOperations != null) {
            result = remoteTodoItemCrudOperations.readAllTodoItems();
        }
        return result;
    }

    @Override
    public TodoItem readTodoItem(long todoItemId) {
        TodoItem result = localTodoItemCrudOperations.readTodoItem(todoItemId);
        if (result == null && remoteTodoItemCrudOperations != null) {
            result = remoteTodoItemCrudOperations.readTodoItem(todoItemId);
        }
        return result;
    }

    @Override
    public TodoItem updateTodoItem(TodoItem item) {
        item = localTodoItemCrudOperations.updateTodoItem(item);
        if (item != null && remoteTodoItemCrudOperations != null) {
            remoteTodoItemCrudOperations.updateTodoItem(item);
        }
        return item;
    }

    @Override
    public boolean deleteTodoItem(long todoItemId) {
        boolean result = localTodoItemCrudOperations.deleteTodoItem(todoItemId);
        if (remoteTodoItemCrudOperations != null) {
            result &= remoteTodoItemCrudOperations.deleteTodoItem(todoItemId);
        }
        return result;
    }
}
