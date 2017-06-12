package todolist.mad.vfh.kfrank.de.todolist;

import android.app.Application;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.operations.DelegateTodoItemCrudOperationsImpl;
import todolist.mad.vfh.kfrank.de.todolist.operations.IAuthenticationOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.RemoteAuthenticationOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.RemoteTodoItemCrudOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.ITodoItemCrudOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.LocalTodoItemCrudOperations;

/**
 * Created by kfrank on 04.06.2017.
 */

public class TodoListApplication extends Application {

    private DelegateTodoItemCrudOperationsImpl todoItemCrudOperations;

    private IAuthenticationOperations authenticationOperations;

    @Override
    public void onCreate() {
        super.onCreate();
        // assemble crud access
        LocalTodoItemCrudOperations localTodoItemCrudOperations = new LocalTodoItemCrudOperations(this);
        todoItemCrudOperations = new DelegateTodoItemCrudOperationsImpl(null, localTodoItemCrudOperations);
    }

    public boolean checkForRemoteConnection() {
        String remoteAddress = getWorkingRemoteAddress();
        boolean hasConnection = remoteAddress != null;
        if (hasConnection) {
            LocalTodoItemCrudOperations localTodoItemCrudOperations = todoItemCrudOperations.getLocalTodoItemCrudOperations();
            RemoteTodoItemCrudOperations remoteTodoItemCrudOperations = new RemoteTodoItemCrudOperations(remoteAddress);
            todoItemCrudOperations = new DelegateTodoItemCrudOperationsImpl(remoteTodoItemCrudOperations, localTodoItemCrudOperations);
            authenticationOperations = new RemoteAuthenticationOperations(remoteAddress);
        }
        replaceWithRandomItems();
        return hasConnection;
    }

    public String getWorkingRemoteAddress() {
        for (String url : Arrays.asList("http://192.168.2.101:8080/api")) {
            if (new RemoteTodoItemCrudOperations(url).hasConnection()) {
                return url;
            }
        }
        return null;
    }

    public ITodoItemCrudOperations getTodoItemCrudOperations() {
        return todoItemCrudOperations;
    }

    public IAuthenticationOperations getAuthenticationOperations() {
        return authenticationOperations;
    }

    private void replaceWithRandomItems() {
        // first delete all Items
        List<TodoItem> list = todoItemCrudOperations.readAllTodoItems();
        for (TodoItem item : list) {
            todoItemCrudOperations.deleteTodoItem(item.getId());
        }
        // then add random items
        for (TodoItem item : generateTodoItems(10)) {
            todoItemCrudOperations.createTodoItem(item);
        }
    }

    private List<TodoItem> generateTodoItems(int count) {
        List<TodoItem> list = new LinkedList<>();
        for (int i = 1; i <= count; i++) {
            // TODO
            TodoItem item = new TodoItem();
            item.setName("Todo " + i);
            item.setDescription(getRandomText((int) (Math.random() * 50 + 20)));
            item.setDone(Math.random() < 0.5);
            item.setFavourite(Math.random() < 0.5);
            long now = new Date().getTime();
            try {
                Date date = new Date(now + (long) ((Math.random() - 0.5) * 604800000));
                item.setDueDate(TodoItem.dueDateFormat.parse(TodoItem.dueDateFormat.format(date)));
                // durch das Parsing werden die (Milli)Sekunden weg getrimmt
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;
    }

    private String getRandomText(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = (char) (Math.random() * 94 + 32);
            builder.append(c);
        }
        return builder.toString();
    }
}
