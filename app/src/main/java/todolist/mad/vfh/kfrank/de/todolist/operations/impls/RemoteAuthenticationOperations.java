package todolist.mad.vfh.kfrank.de.todolist.operations.impls;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import todolist.mad.vfh.kfrank.de.todolist.model.User;
import todolist.mad.vfh.kfrank.de.todolist.operations.abstracts.AbstractRemoteOperations;
import todolist.mad.vfh.kfrank.de.todolist.operations.interfaces.IAuthenticationOperations;

/**
 * Created by kfrank on 05.06.2017.
 */

public class RemoteAuthenticationOperations extends AbstractRemoteOperations<User> implements IAuthenticationOperations {

    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PASSWORD = "pwd";

    private static final String USERS_PATH = "/users";

    public RemoteAuthenticationOperations(String url) {
        super(url + USERS_PATH, null);
    }

    @Override
    public boolean authenticateUser(User user) {
        JsonNode node = sendRequest("/auth", "PUT", user);
        if (node == null) {
            return false;
        }
        return node.getBooleanValue();
    }

    @Override
    protected ObjectNode convertToObjectNode(User item) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put(FIELD_EMAIL, item.getEmail());
        objectNode.put(FIELD_PASSWORD, item.getPassword());
        return objectNode;
    }
}
