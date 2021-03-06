package todolist.mad.vfh.kfrank.de.todolist;

import android.app.Application;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;
import todolist.mad.vfh.kfrank.de.todolist.operations.impls.DelegateTodoItemCrudOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.interfaces.IAuthenticationOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.interfaces.IContactAccessOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.impls.RemoteAuthenticationOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.impls.RemoteTodoItemCrudOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.interfaces.ITodoItemCrudOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.impls.LocalTodoItemCrudOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.impls.TodoContactAccessOperations;

/**
 * Created by kfrank on 04.06.2017.
 */

public class TodoListApplication extends Application {

    private DelegateTodoItemCrudOperations todoItemCrudOperations;

    private IAuthenticationOperations authenticationOperations;

    private IContactAccessOperations contactAccessOperations;

    @Override
    public void onCreate() {
        super.onCreate();
        // assemble crud access
        contactAccessOperations = new TodoContactAccessOperations(getContentResolver());
        LocalTodoItemCrudOperations localTodoItemCrudOperations = new LocalTodoItemCrudOperations(this, contactAccessOperations);
        todoItemCrudOperations = new DelegateTodoItemCrudOperations(null, localTodoItemCrudOperations);
    }

    public boolean checkForRemoteConnection() {
        String remoteAddress = getWorkingRemoteAddress();
        boolean hasConnection = remoteAddress != null;
        if (hasConnection) {
            LocalTodoItemCrudOperations localTodoItemCrudOperations = todoItemCrudOperations.getLocalTodoItemCrudOperations();
            RemoteTodoItemCrudOperations remoteTodoItemCrudOperations = new RemoteTodoItemCrudOperations(remoteAddress, contactAccessOperations);
            todoItemCrudOperations = new DelegateTodoItemCrudOperations(remoteTodoItemCrudOperations, localTodoItemCrudOperations);
            authenticationOperations = new RemoteAuthenticationOperations(remoteAddress);
        }
        // replaceWithRandomItems(); // TODO remove comment marks for random generated todo items
        return hasConnection;
    }

    private String getWorkingRemoteAddress() {
        for (String url : Arrays.asList("http://192.168.2.101:8080/api")) { // TODO ggfs. URLs hinzufügen, wenn diese nicht funktioniert
            if (new RemoteTodoItemCrudOperations(url, contactAccessOperations).hasConnection()) {
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

    public IContactAccessOperations getContactAccessOperations() {
        return contactAccessOperations;
    }

    private void replaceWithRandomItems() {
        // first delete all Items
        List<TodoItem> list = todoItemCrudOperations.readAllTodoItems();
        for (TodoItem item : list) {
            todoItemCrudOperations.deleteTodoItem(item.getId());
        }
        // then add random items
        for (TodoItem item : generateTodoItems(10)) { // TODO adjust number for more / less random items
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
