package todolist.mad.vfh.kfrank.de.todolist.operations.interfaces;

import todolist.mad.vfh.kfrank.de.todolist.model.User;

/**
 * Created by kfrank on 05.06.2017.
 */

public interface IAuthenticationOperations {

    public boolean authenticateUser(User user);
}
