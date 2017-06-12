package todolist.mad.vfh.kfrank.de.todolist.operations;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by kfrank on 05.06.2017.
 */

public abstract class AbstractRemoteOperations<T> {

    private String url;

    private ObjectMapper objectMapper;

    private JsonFactory jsonFactory;

    public AbstractRemoteOperations(String url) {
        this.url = url;
        objectMapper = new ObjectMapper();
        jsonFactory = new JsonFactory(objectMapper);
    }

    protected JsonNode sendRequest(String path, String method, T item) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url + path).openConnection();
            connection.setRequestMethod(method);
            if (Arrays.asList("POST", "PUT", "DELETE").contains(method)) {
                connection.setRequestProperty("Content-Type", "application/json");
                if (item != null) {
                    connection.setDoOutput(true);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(convertToJsonString(item).getBytes());
                }
            }
            InputStream inputStream = connection.getInputStream();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readJsonNodeFromInputStream(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertToJsonString(T item) throws IOException {
        ObjectNode node = convertToObjectNode(item);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JsonGenerator generator = jsonFactory.createJsonGenerator(os, JsonEncoding.UTF8);
        generator.writeObject(node);
        return os.toString();
    }

    private JsonNode readJsonNodeFromInputStream(InputStream is) throws IOException {
        return objectMapper.readValue(is, JsonNode.class);
    }

    protected String getUrl() {
        return url;
    }

    protected abstract ObjectNode convertToObjectNode(T item);
}
