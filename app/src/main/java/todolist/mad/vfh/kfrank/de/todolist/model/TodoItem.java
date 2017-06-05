package todolist.mad.vfh.kfrank.de.todolist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kfrank on 03.06.2017.
 */

public class TodoItem implements Serializable {

    private long id;

    private String name;

    private String description;

    private boolean done;

    private boolean favourite;

    private Date dueDate;

    public TodoItem() {
    }

    public TodoItem(long id, String name, String description, boolean done, boolean favourite, Date dueDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
