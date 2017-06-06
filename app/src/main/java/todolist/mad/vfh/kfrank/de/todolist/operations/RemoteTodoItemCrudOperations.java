package todolist.mad.vfh.kfrank.de.todolist.operations;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;

public class RemoteTodoItemCrudOperations extends AbstractRemoteOperations<TodoItem> implements ITodoItemCrudOperations {

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_DONE = "done";
    private static final String FIELD_FAVOURITE = "favourite";
    private static final String FIELD_DUE_DATE = "expiry";

    private static final String TODO_PATH = "/todos";

    public RemoteTodoItemCrudOperations(String url) {
        super(url + TODO_PATH);
    }

    @Override
    public TodoItem createTodoItem(TodoItem item) {
        JsonNode node = sendRequest("", "POST", item);
        if (node == null) {
            return null;
        }
        return convertFromObjectNode((ObjectNode) node);
    }

    @Override
    public List<TodoItem> readAllTodoItems() {
        List<TodoItem> items = new LinkedList<>();
        ArrayNode node = (ArrayNode) sendRequest("", "GET", null);
        if (node == null) {
            return items;
        }
        for (int i = 0; i < node.size(); i++) {
            items.add(convertFromObjectNode((ObjectNode) node.get(i)));
        }
        return items;
    }

    @Override
    public TodoItem readTodoItem(long todoItemId) {
        JsonNode node = sendRequest("/" + todoItemId, "GET", null);
        if (node == null) {
            return null;
        }
        return convertFromObjectNode((ObjectNode) node);
    }

    @Override
    public TodoItem updateTodoItem(TodoItem item) {
        JsonNode node = sendRequest("/" + item.getId(), "PUT", item);
        if (node == null) {
            return null;
        }
        return convertFromObjectNode((ObjectNode) node);
    }

    @Override
    public boolean deleteTodoItem(long todoItemId) {
        JsonNode node = sendRequest("/" + todoItemId, "DELETE", null);
        if (node == null) {
            return false;
        }
        return node.getBooleanValue();
    }

    public boolean hasConnection() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(getUrl()).openConnection();
            connection.setRequestMethod("GET");
            connection.getInputStream();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected ObjectNode convertToObjectNode(TodoItem item) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put(FIELD_ID, item.getId());
        objectNode.put(FIELD_NAME, item.getName());
        objectNode.put(FIELD_DESCRIPTION, item.getDescription());
        objectNode.put(FIELD_DONE, item.isDone());
        objectNode.put(FIELD_FAVOURITE, item.isFavourite());
        objectNode.put(FIELD_DUE_DATE, item.getDueDate().getTime());
        return objectNode;
    }

    private TodoItem convertFromObjectNode(ObjectNode objectNode) {
        long id = objectNode.get(FIELD_ID).getLongValue();
        String name = objectNode.get(FIELD_NAME).getTextValue();
        String description = objectNode.get(FIELD_DESCRIPTION).getTextValue();
        boolean done = objectNode.get(FIELD_DONE).getBooleanValue();
        boolean favourite = objectNode.get(FIELD_FAVOURITE).getBooleanValue();
        long dueDate = objectNode.get(FIELD_DUE_DATE).getLongValue();
        return new TodoItem(id, name, description, done, favourite, new Date(dueDate));
    }
}
