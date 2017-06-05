package todolist.mad.vfh.kfrank.de.todolist;

import android.app.Application;

import java.util.Arrays;

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

    private boolean hasConnection;

    private ITodoItemCrudOperations todoItemCrudOperations;

    private IAuthenticationOperations authenticationOperations;

    @Override
    public void onCreate() {
        super.onCreate();
        // assemble crud access
        LocalTodoItemCrudOperations localTodoItemCrudOperations = new LocalTodoItemCrudOperations(this);
        RemoteTodoItemCrudOperations remoteTodoItemCrudOperations = null;
        String remoteAddress = getWorkingRemoteAddress();
        hasConnection = remoteAddress != null;
        if (remoteAddress != null) {
            remoteTodoItemCrudOperations = new RemoteTodoItemCrudOperations(remoteAddress);
            authenticationOperations = new RemoteAuthenticationOperations(remoteAddress);
        }
        todoItemCrudOperations = new DelegateTodoItemCrudOperationsImpl(remoteTodoItemCrudOperations, localTodoItemCrudOperations);
    }

    public String getWorkingRemoteAddress() {
        for (String url : Arrays.asList("http://10.0.3.2:8080/api/", "http://10.0.2.2:8080/api/", "http://192.168.178.47:8080/api/")) {
            if (new RemoteTodoItemCrudOperations(url).hasConnection()) {
                return url;
            }
        }
        return null;
    }

    public boolean hasConnection() {
        return hasConnection;
    }

    public ITodoItemCrudOperations getTodoItemCrudOperations() {
        return todoItemCrudOperations;
    }

    public IAuthenticationOperations getAuthenticationOperations() {
        return authenticationOperations;
    }
}
